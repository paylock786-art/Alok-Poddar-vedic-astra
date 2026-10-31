package com.vedicastra.app.domain

import io.github.cosinekitty.astronomy.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.math.*

data class GrahaPosition(val name: String, val longitude: Double, val signIndex: Int, val degreesInSign: Double, val retrograde: Boolean? = null, val house: Int? = null)
data class PanchangResult(
    val dateTime: String,
    val ayanamsha: Double,
    val sunLongitude: Double,
    val moonLongitude: Double,
    val tithi: String,
    val paksha: String,
    val nakshatra: String,
    val yoga: String,
    val karana: String,
    val moonPhasePercent: Double,
    val sunrise: String?,
    val sunset: String?
)
data class BirthChartResult(
    val ascendant: GrahaPosition,
    val planets: List<GrahaPosition>,
    val ayanamsha: Double,
    val houseSystem: String = "Whole Sign (राशि आधारित)"
)

/**
 * Offline calculations using Astronomy Engine (MIT) for geocentric vectors,
 * with Lahiri-style sidereal offset and traditional panchanga indexing.
 * Always retain location, timezone and ayanamsha with exported chart data.
 */
object FreeVedicAstronomy {
    private val signs = listOf("मेष","वृषभ","मिथुन","कर्क","सिंह","कन्या","तुला","वृश्चिक","धनु","मकर","कुम्भ","मीन")
    private val nakshatras = listOf("अश्विनी","भरणी","कृत्तिका","रोहिणी","मृगशिरा","आर्द्रा","पुनर्वसु","पुष्य","आश्लेषा","मघा","पूर्वा फाल्गुनी","उत्तरा फाल्गुनी","हस्त","चित्रा","स्वाती","विशाखा","अनुराधा","ज्येष्ठा","मूल","पूर्वाषाढ़ा","उत्तराषाढ़ा","श्रवण","धनिष्ठा","शतभिषा","पूर्वा भाद्रपदा","उत्तरा भाद्रपदा","रेवती")
    private val tithis = listOf("प्रतिपदा","द्वितीया","तृतीया","चतुर्थी","पञ्चमी","षष्ठी","सप्तमी","अष्टमी","नवमी","दशमी","एकादशी","द्वादशी","त्रयोदशी","चतुर्दशी","पूर्णिमा")
    private val yogas = listOf("विष्कम्भ","प्रीति","आयुष्मान","सौभाग्य","शोभन","अतिगण्ड","सुकर्मा","धृति","शूल","गण्ड","वृद्धि","ध्रुव","व्याघात","हर्षण","वज्र","सिद्धि","व्यतीपात","वरीयान","परिघ","शिव","सिद्ध","साध्य","शुभ","शुक्ल","ब्रह्म","इन्द्र","वैधृति")
    private val karanas = listOf("किंस्तुघ्न","बव","बालव","कौलव","तैतिल","गर","वणिज","विष्टि (भद्रा)")

    fun normalize(degrees: Double): Double = ((degrees % 360.0) + 360.0) % 360.0

    // Approximate Lahiri/Chitrapaksha ayanamsha, suitable for an initial
    // sidereal framework; production values must be cross-checked to an almanac.
    fun lahiriAyanamsha(julianDay: Double): Double {
        val centuries = (julianDay - 2451545.0) / 36525.0
        return 23.8530556 + 1.3969713 * centuries + 0.0003086 * centuries * centuries
    }

    private fun julianDay(epochMillis: Long): Double = epochMillis / 86400000.0 + 2440587.5

    private fun longitude(body: Body, time: Time): Double {
        val vector = geoVector(body, time, Aberration.Corrected)
        return normalize(equatorialToEcliptic(vector).elon)
    }

    fun panchang(epochMillis: Long, latitude: Double, longitudeEast: Double, zoneId: String): PanchangResult {
        require(latitude in -90.0..90.0 && longitudeEast in -180.0..180.0)
        val time = Time.fromMillisecondsSince1970(epochMillis)
        val jd = julianDay(epochMillis)
        val ayan = lahiriAyanamsha(jd)
        val sun = normalize(longitude(Body.Sun, time) - ayan)
        val moon = normalize(longitude(Body.Moon, time) - ayan)
        val elongation = normalize(moon - sun)
        val tithiIndex = floor(elongation / 12.0).toInt().coerceIn(0,29)
        val yogaIndex = floor(normalize(sun + moon + 2.0 * ayan) / (360.0 / 27.0)).toInt().coerceIn(0,26)
        val karanaIndex = floor(elongation / 6.0).toInt().coerceIn(0,59)
        val karana = when (karanaIndex) {
            0 -> karanas[0]
            57 -> karanas[7]
            58 -> karanas[0]
            59 -> karanas[7]
            else -> karanas[1 + ((karanaIndex - 1) % 7)]
        }
        val paksha = if (tithiIndex < 15) "शुक्ल पक्ष" else "कृष्ण पक्ष"
        val tithiName = if (tithiIndex == 14) "पूर्णिमा" else if (tithiIndex == 29) "अमावस्या" else tithis[tithiIndex % 15]
        val observer = Observer(latitude, longitudeEast, 0.0)
        val rise = searchRiseSet(Body.Sun, observer, Direction.Rise, time, 2.0)
        val set = searchRiseSet(Body.Sun, observer, Direction.Set, time, 2.0)
        val local = Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of(zoneId)).toString()
        val illumination = illumination(Body.Moon, time).phaseFraction * 100.0
        return PanchangResult(local, ayan, sun, moon, tithiName, paksha,
            nakshatras[floor(moon / (360.0/27.0)).toInt().coerceIn(0,26)],
            yogas[yogaIndex], karana, illumination, rise?.toString(), set?.toString())
    }

    fun birthChart(epochMillis: Long, latitude: Double, longitudeEast: Double): BirthChartResult {
        require(latitude in -66.0..66.0 && longitudeEast in -180.0..180.0) {
            "Whole-sign ascendant is not supported above 66° latitude."
        }
        val time = Time.fromMillisecondsSince1970(epochMillis)
        val jd = julianDay(epochMillis)
        val t = (jd - 2451545.0) / 36525.0
        val ayan = lahiriAyanamsha(jd)
        val gmst = normalize(280.46061837 + 360.98564736629 * (jd - 2451545.0) + 0.000387933*t*t - t*t*t/38710000.0)
        val lst = Math.toRadians(normalize(gmst + longitudeEast))
        val obliquity = Math.toRadians(23.439291 - 0.0130042*t)
        val lat = Math.toRadians(latitude)
        val ascTropical = normalize(Math.toDegrees(atan2(-cos(lst), sin(obliquity)*tan(lat) + cos(obliquity)*sin(lst))))
        val ascSidereal = normalize(ascTropical - ayan)
        val ascSign = floor(ascSidereal / 30.0).toInt()
        fun position(name: String, tropical: Double): GrahaPosition {
            val sidereal = normalize(tropical - ayan)
            val sign = floor(sidereal / 30.0).toInt()
            return GrahaPosition(name, sidereal, sign, sidereal % 30.0, house = ((sign - ascSign + 12) % 12) + 1)
        }
        val asc = GrahaPosition("लग्न", ascSidereal, ascSign, ascSidereal % 30.0, house = 1)
        val bodies = listOf(
            "सूर्य" to Body.Sun, "चन्द्र" to Body.Moon, "बुध" to Body.Mercury,
            "शुक्र" to Body.Venus, "मंगल" to Body.Mars, "गुरु" to Body.Jupiter, "शनि" to Body.Saturn
        )
        val planets = bodies.map { (name, body) -> position(name, longitude(body, time)) }.toMutableList()
        val node = normalize(125.04452 - 1934.136261*t + 0.0020708*t*t + t*t*t/450000.0)
        val rahu = normalize(node - ayan)
        planets.add(position("राहु", node))
        planets.add(position("केतु", normalize(node + 180.0)))
        return BirthChartResult(asc, planets, ayan)
    }

    fun signName(index: Int): String = signs[index.mod(12)]
}
