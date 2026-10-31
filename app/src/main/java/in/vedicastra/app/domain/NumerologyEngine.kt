package in.vedicastra.app.domain

import java.time.LocalDate

enum class NumberMethod { INDIAN_TRADITIONAL, CHALDEAN, PYTHAGOREAN }

data class NumerologyReport(
    val birthNumber: Int,
    val destinyNumber: Int,
    val nameNumber: Int?,
    val compoundNameNumber: Int?,
    val method: NumberMethod,
    val explanation: List<String>
)

object NumerologyEngine {
    private val chaldean = mapOf(
        'A' to 1,'I' to 1,'J' to 1,'Q' to 1,'Y' to 1,
        'B' to 2,'K' to 2,'R' to 2,
        'C' to 3,'G' to 3,'L' to 3,'S' to 3,
        'D' to 4,'M' to 4,'T' to 4,
        'E' to 5,'H' to 5,'N' to 5,'X' to 5,
        'U' to 6,'V' to 6,'W' to 6,
        'O' to 7,'Z' to 7,
        'F' to 8,'P' to 8
    )
    private val pythagorean = mapOf(
        'A' to 1,'J' to 1,'S' to 1, 'B' to 2,'K' to 2,'T' to 2,
        'C' to 3,'L' to 3,'U' to 3, 'D' to 4,'M' to 4,'V' to 4,
        'E' to 5,'N' to 5,'W' to 5, 'F' to 6,'O' to 6,'X' to 6,
        'G' to 7,'P' to 7,'Y' to 7, 'H' to 8,'Q' to 8,'Z' to 8,
        'I' to 9,'R' to 9
    )

    fun calculate(date: LocalDate, fullName: String, method: NumberMethod): NumerologyReport {
        val birth = reduce(date.dayOfMonth)
        val destiny = reduce(date.year.toString().map { it.digitToInt() }.sum() +
            date.monthValue.toString().map { it.digitToInt() }.sum() +
            date.dayOfMonth.toString().map { it.digitToInt() }.sum())
        val map = when (method) {
            NumberMethod.CHALDEAN -> chaldean
            NumberMethod.PYTHAGOREAN -> pythagorean
            NumberMethod.INDIAN_TRADITIONAL -> pythagorean
        }
        val total = fullName.uppercase().filter { it in 'A'..'Z' }.sumOf { map[it] ?: 0 }
        val nameNumber = if (total > 0) reduce(total) else null
        val notes = listOf(
            "जन्मांक जन्म के दिन (दिनांक) के अंकों से निकाला गया है।",
            "भाग्यांक पूरी जन्मतिथि के सभी अंकों के योग से निकाला गया है।",
            when (method) {
                NumberMethod.INDIAN_TRADITIONAL -> "नामांक के लिए इस संस्करण में 1–9 अक्षर-मान वाली पाइथागोरियन mapping उपयोग हुई है; इसे भारतीय परंपरा का एकमात्र मानक न समझें।"
                NumberMethod.CHALDEAN -> "नामांक चाल्डियन अक्षर mapping से निकाला गया है।"
                NumberMethod.PYTHAGOREAN -> "नामांक पाइथागोरियन अक्षर mapping से निकाला गया है।"
            },
            "यह अंक-गणना परंपरागत अंक-ज्योतिष पद्धति है; इससे भविष्य या व्यक्तित्व की वैज्ञानिक पुष्टि नहीं होती।"
        )
        return NumerologyReport(birth, destiny, nameNumber, if (total > 0) total else null, method, notes)
    }

    fun reduce(value: Int): Int {
        require(value >= 0) { "Value must not be negative" }
        var n = value
        while (n > 9) n = n.toString().sumOf { it.digitToInt() }
        return n
    }
}
