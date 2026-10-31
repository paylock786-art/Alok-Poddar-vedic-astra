package com.vedicastra.app

import android.os.Bundle
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.util.Locale
import android.location.Geocoder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vedicastra.app.domain.NumerologyEngine
import com.vedicastra.app.domain.NumberMethod
import com.vedicastra.app.domain.NumerologyReport
import com.vedicastra.app.domain.NakshatraCatalog
import com.vedicastra.app.domain.TarotDeck
import com.vedicastra.app.domain.TarotCard
import com.vedicastra.app.domain.RemedyCatalog
import com.vedicastra.app.domain.PredictionRepository
import com.vedicastra.app.domain.FreeVedicAstronomy
import com.vedicastra.app.ui.AstraLanguage
import com.vedicastra.app.ui.PalmistryOfflineScreen
import com.vedicastra.app.ui.SamudrikaVisualScreen
import com.vedicastra.app.ui.CelestialGalleryScreen
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate

private enum class AppLanguage { HINDI, MAITHILI, ENGLISH }
private data class AstraModule(val icon: String, val title: Map<AppLanguage,String>, val description: Map<AppLanguage,String>)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { VedicAstraApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VedicAstraApp() {
    var language by remember { mutableStateOf(AppLanguage.HINDI) }
    var showNumerology by remember { mutableStateOf(false) }
    var showNakshatra by remember { mutableStateOf(false) }
    var showTarot by remember { mutableStateOf(false) }
    var showRemedies by remember { mutableStateOf(false) }
    var showVerification by remember { mutableStateOf(false) }
    var showPanchang by remember { mutableStateOf(false) }
    var showBirthChart by remember { mutableStateOf(false) }
    var showPalmistry by remember { mutableStateOf(false) }
    var showSamudrika by remember { mutableStateOf(false) }
    var showCelestial by remember { mutableStateOf(false) }
    val modules = listOf(
        AstraModule("☀", mapOf(AppLanguage.HINDI to "वैदिक ज्योतिष", AppLanguage.MAITHILI to "वैदिक ज्योतिष", AppLanguage.ENGLISH to "Vedic Astrology"), mapOf(AppLanguage.HINDI to "जन्मकुंडली, दशा, गोचर और योग", AppLanguage.MAITHILI to "जन्मकुंडली, दशा आ गोचर", AppLanguage.ENGLISH to "Birth chart, dashas, transits and yogas")),
        AstraModule("☾", mapOf(AppLanguage.HINDI to "मिथिला पंचांग", AppLanguage.MAITHILI to "मिथिला पंचांग", AppLanguage.ENGLISH to "Mithila Panchang"), mapOf(AppLanguage.HINDI to "तिथि, नक्षत्र, मुहूर्त और पंचांग तुलना", AppLanguage.MAITHILI to "तिथि, नक्षत्र आ मुहूर्त", AppLanguage.ENGLISH to "Tithi, nakshatra, muhurta and almanac comparison")),
        AstraModule("१", mapOf(AppLanguage.HINDI to "अंक ज्योतिष", AppLanguage.MAITHILI to "अंक ज्योतिष", AppLanguage.ENGLISH to "Numerology"), mapOf(AppLanguage.HINDI to "जन्मांक, भाग्यांक, नामांक और अंक-चक्र", AppLanguage.MAITHILI to "जन्मांक, भाग्यांक आ नामांक", AppLanguage.ENGLISH to "Birth, destiny and name numbers")),
        AstraModule("✋", mapOf(AppLanguage.HINDI to "हस्तरेखा AI", AppLanguage.MAITHILI to "हस्तरेखा AI", AppLanguage.ENGLISH to "Palmistry AI"), mapOf(AppLanguage.HINDI to "हथेली की रेखाओं का पारंपरिक विश्लेषण", AppLanguage.MAITHILI to "हथेली रेखाक पारंपरिक अध्ययन", AppLanguage.ENGLISH to "Traditional interpretation of palm lines")),
        AstraModule("✧", mapOf(AppLanguage.HINDI to "टैरो रीडिंग", AppLanguage.MAITHILI to "टैरो रीडिंग", AppLanguage.ENGLISH to "Tarot Reading"), mapOf(AppLanguage.HINDI to "78 कार्ड और अलग-अलग स्प्रेड", AppLanguage.MAITHILI to "78 कार्ड आ अलग-अलग स्प्रेड", AppLanguage.ENGLISH to "78 cards and multiple spreads")),
        AstraModule("◉", mapOf(AppLanguage.HINDI to "सामुद्रिक शास्त्र", AppLanguage.MAITHILI to "सामुद्रिक शास्त्र", AppLanguage.ENGLISH to "Face Reading"), mapOf(AppLanguage.HINDI to "चेहरे के दृश्य संकेतों का पारंपरिक अध्ययन", AppLanguage.MAITHILI to "चेहराक दृश्य संकेतक पारंपरिक अध्ययन", AppLanguage.ENGLISH to "Traditional study of visible facial features")),
        AstraModule("ॐ", mapOf(AppLanguage.HINDI to "उपाय एवं पूजा", AppLanguage.MAITHILI to "उपाय आ पूजा", AppLanguage.ENGLISH to "Remedies & Puja"), mapOf(AppLanguage.HINDI to "मंत्र, पूजा-विधि, दान और रत्न-संदर्भ", AppLanguage.MAITHILI to "मंत्र, पूजा-विधि, दान आ रत्न", AppLanguage.ENGLISH to "Mantras, rituals, charity and gem traditions")),
        AstraModule("✺", mapOf(AppLanguage.HINDI to "नक्षत्र दर्शन", AppLanguage.MAITHILI to "नक्षत्र दर्शन", AppLanguage.ENGLISH to "Nakshatra Guide"), mapOf(AppLanguage.HINDI to "27 नक्षत्र, प्रतीक और अधिष्ठाता देवता", AppLanguage.MAITHILI to "27 नक्षत्र, प्रतीक आ देवता", AppLanguage.ENGLISH to "27 nakshatras, symbols and presiding deities")),
        AstraModule("✦", mapOf(AppLanguage.HINDI to "ग्रह चित्रावली", AppLanguage.MAITHILI to "ग्रह चित्रावली", AppLanguage.ENGLISH to "Celestial Gallery"), mapOf(AppLanguage.HINDI to "ग्रहों और 27 नक्षत्रों के मौलिक चित्र", AppLanguage.MAITHILI to "ग्रह आ 27 नक्षत्रक चित्र", AppLanguage.ENGLISH to "Original illustrations of planets and 27 nakshatras")),
        AstraModule("◷", mapOf(AppLanguage.HINDI to "समय विश्लेषण", AppLanguage.MAITHILI to "समय विश्लेषण", AppLanguage.ENGLISH to "Time Analysis"), mapOf(AppLanguage.HINDI to "अगले 1, 3, 6, 12 और 24 घंटे", AppLanguage.MAITHILI to "आगाँक 1, 3, 6, 12 आ 24 घंटा", AppLanguage.ENGLISH to "Next 1, 3, 6, 12 and 24 hours")),
        AstraModule("✓", mapOf(AppLanguage.HINDI to "परिणाम सत्यापन", AppLanguage.MAITHILI to "परिणाम जाँच", AppLanguage.ENGLISH to "Prediction Verification"), mapOf(AppLanguage.HINDI to "पूर्वानुमान और वास्तविक परिणाम का रिकॉर्ड", AppLanguage.MAITHILI to "अनुमान आ वास्तविक परिणामक रिकॉर्ड", AppLanguage.ENGLISH to "Track forecasts against observed outcomes"))
    )
    val saffron = Color(0xFFFFC66D)
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF8B3D20), secondary = Color(0xFFB77B32), background = Color(0xFFFFF8ED), surface = Color(0xFFFFFCF6))) {
        Scaffold(containerColor = Color(0xFFFFF8ED), topBar = {
            TopAppBar(title = { Column { Text("VEDIC ASTRA", fontWeight = FontWeight.ExtraBold); Text("सनातन ज्ञान • खगोलीय गणना", style = MaterialTheme.typography.labelSmall) } },
                navigationIcon = { if (showNumerology || showNakshatra || showTarot || showRemedies || showVerification || showPanchang || showBirthChart || showPalmistry || showSamudrika || showCelestial) TextButton(onClick = { showNumerology = false; showNakshatra = false; showTarot = false; showRemedies = false; showVerification = false; showPanchang = false; showBirthChart = false; showPalmistry = false; showSamudrika = false; showCelestial = false }) { Text("←") } },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    TextButton(onClick = { expanded = true }) { Text(when(language) { AppLanguage.HINDI -> "हिन्दी"; AppLanguage.MAITHILI -> "मैथिली"; AppLanguage.ENGLISH -> "English" }) }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("हिन्दी") }, onClick = { language = AppLanguage.HINDI; expanded = false })
                        DropdownMenuItem(text = { Text("मैथिली") }, onClick = { language = AppLanguage.MAITHILI; expanded = false })
                        DropdownMenuItem(text = { Text("English") }, onClick = { language = AppLanguage.ENGLISH; expanded = false })
                    }
                })
        }) { padding ->
            when {
                showNumerology -> NumerologyScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showNakshatra -> NakshatraScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showTarot -> TarotScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showRemedies -> RemediesScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showVerification -> VerificationScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showPanchang -> PanchangScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showBirthChart -> BirthChartScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                showPalmistry -> PalmistryOfflineScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp), AstraLanguage.valueOf(language.name))
                showSamudrika -> SamudrikaVisualScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp), AstraLanguage.valueOf(language.name))
                showCelestial -> CelestialGalleryScreen(Modifier.fillMaxSize().padding(padding).padding(16.dp))
                else -> {
                Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 14.dp)) {
                    Box(Modifier.fillMaxWidth().height(190.dp).background(Brush.verticalGradient(listOf(Color(0xFF5B2119), Color(0xFF32120F), Color(0xFF1E1420))), RoundedCornerShape(22.dp)).padding(18.dp)) {
                        Column(Modifier.align(Alignment.CenterStart).fillMaxWidth(0.75f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                            Text("॥ ॐ ॥", color = saffron, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            Text("वेदिक ज्ञान की दिव्य यात्रा", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("ग्रह • नक्षत्र • देवता • पंचांग", color = Color(0xFFFFDCA8))
                            Text("श्रद्धा के साथ अध्ययन, गणना में स्पष्टता", color = Color.White.copy(alpha = 0.86f), style = MaterialTheme.typography.labelMedium)
                        }
                        Text("☼\n✧\n☾", Modifier.align(Alignment.CenterEnd), color = saffron, style = MaterialTheme.typography.headlineLarge)
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(when(language) { AppLanguage.HINDI -> "आपका समेकित वैदिक ज्ञान मंच"; AppLanguage.MAITHILI -> "अहाँक समेकित वैदिक ज्ञान मंच"; AppLanguage.ENGLISH -> "Your integrated traditional knowledge platform" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(9.dp))
                    LazyVerticalGrid(columns = GridCells.Fixed(2), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                        items(modules) { module ->
                            val key = module.title[AppLanguage.ENGLISH]
                            val enabled = key in listOf("Numerology", "Nakshatra Guide", "Tarot Reading", "Remedies & Puja", "Prediction Verification", "Mithila Panchang", "Vedic Astrology", "Palmistry AI", "Face Reading", "Celestial Gallery")
                            ElevatedCard(Modifier.fillMaxWidth().clickable(enabled = enabled) {
                                when (key) {
                                    "Numerology" -> showNumerology = true
                                    "Nakshatra Guide" -> showNakshatra = true
                                    "Tarot Reading" -> showTarot = true
                                    "Remedies & Puja" -> showRemedies = true
                                    "Prediction Verification" -> showVerification = true
                                    "Mithila Panchang" -> showPanchang = true
                                    "Vedic Astrology" -> showBirthChart = true
                                    "Palmistry AI" -> showPalmistry = true
                                    "Face Reading" -> showSamudrika = true
                                    "Celestial Gallery" -> showCelestial = true
                                }
                            }, shape = RoundedCornerShape(17.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFDF8))) {
                                Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(module.icon, style = MaterialTheme.typography.headlineMedium, color = Color(0xFF9B4A25))
                                    Text(module.title[language] ?: module.title[AppLanguage.ENGLISH]!!, fontWeight = FontWeight.Bold)
                                    Text(module.description[language] ?: module.description[AppLanguage.ENGLISH]!!, style = MaterialTheme.typography.bodySmall, minLines = 2)
                                    Text(if (enabled) "खोलें / Open" else when(language) { AppLanguage.HINDI -> "विकास चरण में"; AppLanguage.MAITHILI -> "विकास जारी"; AppLanguage.ENGLISH -> "In development" }, style = MaterialTheme.typography.labelSmall, color = Color(0xFF9A5528))
                                }
                            }
                        }
                    }
                    Text(when(language) { AppLanguage.HINDI -> "ज्योतिष, हस्तरेखा, टैरो और सामुद्रिक व्याख्याएं पारंपरिक मान्यताएं हैं; इन्हें निश्चित या वैज्ञानिक भविष्यवाणी न मानें।"; AppLanguage.MAITHILI -> "ई सभ पारंपरिक व्याख्या अछि, निश्चित वैज्ञानिक भविष्यवाणी नहि।"; AppLanguage.ENGLISH -> "These are traditional interpretations, not scientifically established predictions." }, style = MaterialTheme.typography.labelSmall, color = Color(0xFF6E6259))
                    Spacer(Modifier.height(6.dp))
                }
                }
            }
        }
    }
}

@Composable
private fun NumerologyScreen(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var method by remember { mutableStateOf(NumberMethod.CHALDEAN) }
    var report by remember { mutableStateOf<NumerologyReport?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(11.dp)) {
        Text("अंक ज्योतिष विश्लेषण", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("जन्मतिथि YYYY-MM-DD प्रारूप में भरें। नामांक के लिए अंग्रेज़ी वर्तनी उपयोग होती है।", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(value = name, onValueChange = { name = it; report = null }, label = { Text("पूरा नाम (English spelling)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = dob, onValueChange = { dob = it; report = null }, label = { Text("जन्मतिथि (YYYY-MM-DD)") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii), modifier = Modifier.fillMaxWidth())
        Text("नामांक पद्धति", fontWeight = FontWeight.SemiBold)
        NumberMethod.values().forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = method == option, onClick = { method = option; report = null })
                Text(when(option) { NumberMethod.CHALDEAN -> "चाल्डियन"; NumberMethod.PYTHAGOREAN -> "पाइथागोरियन"; NumberMethod.INDIAN_TRADITIONAL -> "भारतीय पारंपरिक (सीमित mapping)" })
            }
        }
        Button(onClick = {
            try { report = NumerologyEngine.calculate(LocalDate.parse(dob.trim()), name.trim(), method); error = null }
            catch (_: Exception) { error = "कृपया सही जन्मतिथि (YYYY-MM-DD) और नाम भरें।"; report = null }
        }, modifier = Modifier.fillMaxWidth()) { Text("गणना करें") }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        report?.let { data ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("आपकी अंक रिपोर्ट", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("जन्मांक: ${data.birthNumber}", style = MaterialTheme.typography.titleMedium)
                    Text("भाग्यांक: ${data.destinyNumber}", style = MaterialTheme.typography.titleMedium)
                    Text("नामांक: ${data.nameNumber ?: "उपलब्ध नहीं"}", style = MaterialTheme.typography.titleMedium)
                    Text("संयुक्त नामांक: ${data.compoundNameNumber ?: "—"}")
                    HorizontalDivider()
                    data.explanation.forEach { Text("• $it", style = MaterialTheme.typography.bodySmall) }
                }
            }
        }
    }
}

@Composable
private fun NakshatraScreen(modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("२७ नक्षत्र दर्शन", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("प्रत्येक नक्षत्र के पारंपरिक अधिष्ठाता देवता और प्रतीक। वास्तविक जन्म-नक्षत्र निकालने के लिए जन्म समय, स्थान और सटीक खगोलीय गणना आवश्यक है।", style = MaterialTheme.typography.bodySmall)
        NakshatraCatalog.all.forEach { item ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(item.number.toString().padStart(2,'0'), color = Color(0xFF9B4A25), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Bold)
                        Text("अधिष्ठाता: ${item.deity}", style = MaterialTheme.typography.bodySmall)
                        Text("प्रतीक: ${item.symbol}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF765B45))
                    }
                }
            }
        }
    }
}

@Composable
private fun TarotScreen(modifier: Modifier = Modifier) {
    var drawn by remember { mutableStateOf(emptyList<Pair<TarotCard, Boolean>>()) }
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("टैरो रीडिंग • 78 कार्ड", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("तीन कार्ड का सामान्य Past–Present–Future spread। कार्ड यादृच्छिक रूप से चुने जाते हैं; इसे आत्मचिंतन/मनोरंजन की पारंपरिक विधि मानें, निश्चित भविष्यवाणी नहीं।", style = MaterialTheme.typography.bodySmall)
        Button(onClick = { drawn = TarotDeck.draw(3) }, modifier = Modifier.fillMaxWidth()) { Text("तीन कार्ड चुनें") }
        val positions = listOf("अतीत / Past", "वर्तमान / Present", "आगे की दिशा / Next")
        drawn.forEachIndexed { index, pair ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(positions[index], color = Color(0xFF9B4A25), fontWeight = FontWeight.Bold)
                    Text(pair.first.name + if (pair.second) " • उल्टा / Reversed" else " • सीधा / Upright", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(if (pair.second) pair.first.reversed else pair.first.upright)
                }
            }
        }
    }
}

@Composable
private fun RemediesScreen(modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("पूजा-पाठ एवं पारंपरिक उपाय", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("नीचे सामान्य धार्मिक परंपराओं का परिचय है। व्यक्तिगत मुहूर्त/ज्योतिषीय उपाय का दावा नहीं किया जा रहा।", style = MaterialTheme.typography.bodySmall)
        RemedyCatalog.all.forEach { remedy ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(remedy.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(remedy.tradition, color = Color(0xFF9B4A25), style = MaterialTheme.typography.labelMedium)
                    Text(remedy.practice, style = MaterialTheme.typography.bodyMedium)
                    Text("ध्यान दें: ${remedy.caution}", style = MaterialTheme.typography.bodySmall, color = Color(0xFF765B45))
                }
            }
        }
    }
}

@Composable
private fun VerificationScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val repository = remember { PredictionRepository(context.applicationContext) }
    var system by remember { mutableStateOf("वैदिक ज्योतिष") }
    var forecast by remember { mutableStateOf("") }
    var dueAt by remember { mutableStateOf("") }
    var records by remember { mutableStateOf(repository.all()) }
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("पूर्वानुमान सत्यापन", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("हर पूर्वानुमान को पहले दर्ज करें और समय पूरा होने पर वास्तविक परिणाम जोड़ें। इससे बाद में निष्पक्ष समीक्षा संभव होगी।", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(value = system, onValueChange = { system = it }, label = { Text("पद्धति / स्रोत") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = forecast, onValueChange = { forecast = it }, label = { Text("पूर्वानुमान (स्पष्ट और मापने योग्य)") }, minLines = 2, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = dueAt, onValueChange = { dueAt = it }, label = { Text("जाँच की तारीख (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            if (forecast.isNotBlank() && dueAt.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) {
                repository.add(system.trim(), forecast.trim(), dueAt.trim())
                forecast = ""; dueAt = ""; records = repository.all()
            }
        }, modifier = Modifier.fillMaxWidth()) { Text("पूर्वानुमान सुरक्षित करें") }
        Text("दर्ज रिकॉर्ड: ${records.size}", fontWeight = FontWeight.Bold)
        records.forEach { record ->
            var outcome by remember(record.id, record.outcome) { mutableStateOf(record.outcome) }
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(record.system + " • " + record.dueAt, color = Color(0xFF9B4A25), fontWeight = FontWeight.Bold)
                    Text(record.forecast)
                    OutlinedTextField(value = outcome, onValueChange = { outcome = it }, label = { Text("वास्तविक परिणाम / प्रमाण") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { repository.updateOutcome(record.id, outcome.trim()); records = repository.all() }, enabled = outcome.isNotBlank()) { Text("परिणाम अपडेट करें") }
                    if (record.outcome.isNotBlank()) Text("पहले दर्ज परिणाम: ${record.outcome}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}


@Composable
private fun PanchangScreen(modifier: Modifier = Modifier) {
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var time by remember { mutableStateOf("12:00") }
    var latitude by remember { mutableStateOf("25.61") }
    var longitude by remember { mutableStateOf("85.14") }
    var zone by remember { mutableStateOf("Asia/Kolkata") }
    var result by remember { mutableStateOf<com.vedicastra.app.domain.PanchangResult?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("मिथिला पंचांग • खगोलीय गणना", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("तिथि, नक्षत्र, योग और करण के लिए स्थान व समय भरें। स्थान के निर्देशांक बदल सकते हैं।", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(date, { date = it; result = null }, label = { Text("तारीख YYYY-MM-DD") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(time, { time = it; result = null }, label = { Text("समय HH:MM (24-hour)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(latitude, { latitude = it }, label = { Text("अक्षांश (Latitude)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(longitude, { longitude = it }, label = { Text("देशांतर (Longitude, East +)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(zone, { zone = it }, label = { Text("Time zone (उदा. Asia/Kolkata)") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            try {
                val instant = LocalDateTime.parse("${date.trim()}T${time.trim()}").atZone(ZoneId.of(zone.trim())).toInstant()
                result = FreeVedicAstronomy.panchang(instant.toEpochMilli(), latitude.toDouble(), longitude.toDouble(), zone.trim())
                error = null
            } catch (e: Exception) { error = "तारीख, समय, निर्देशांक या time zone जाँचें: ${e.message}"; result = null }
        }, modifier = Modifier.fillMaxWidth()) { Text("पंचांग निकालें") }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        result?.let { p ->
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text(p.dateTime, fontWeight = FontWeight.Bold)
                    Text("तिथि: ${p.tithi} • ${p.paksha}")
                    Text("नक्षत्र: ${p.nakshatra}")
                    Text("योग: ${p.yoga}")
                    Text("करण: ${p.karana}")
                    Text("चन्द्रमा का प्रकाशित भाग: ${"%.1f".format(p.moonPhasePercent)}%")
                    Text("सूर्योदय: ${p.sunrise ?: "उपलब्ध नहीं"}")
                    Text("सूर्यास्त: ${p.sunset ?: "उपलब्ध नहीं"}")
                    Text("राहुकाल: ${p.rahuKaal ?: "उपलब्ध नहीं"}")
                    Text("यमगण्ड: ${p.yamagand ?: "उपलब्ध नहीं"}")
                    Text("लाहिड़ी अयनांश (प्रारंभिक अनुमान): ${"%.4f".format(p.ayanamsha)}°", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Text("नोट: यह प्रारंभिक offline गणना है। मिथिला पंचांग के प्रकाशित संस्करण से तिथि-सीमा, स्थानीय नियम और अयनांश का सत्यापन आवश्यक है।", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun BirthChartScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var date by remember { mutableStateOf("1984-02-02") }
    var time by remember { mutableStateOf("23:00") }
    var place by remember { mutableStateOf("Patna, Bihar, India") }
    var latitude by remember { mutableStateOf("25.61") }
    var longitude by remember { mutableStateOf("85.14") }
    var zone by remember { mutableStateOf("Asia/Kolkata") }
    var placeChoices by remember { mutableStateOf<List<android.location.Address>>(emptyList()) }
    var searching by remember { mutableStateOf(false) }
    var placeMessage by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<com.vedicastra.app.domain.BirthChartResult?>(null) }
    var birthEpochMillis by remember { mutableStateOf<Long?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    Column(modifier.verticalScroll(androidx.compose.foundation.rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("जन्मकुंडली • विस्तृत लग्न chart एवं फलादेश", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("जन्म तारीख और स्थानीय जन्म समय भरें। जन्मस्थान का नाम खोजने पर अक्षांश-देशांतर अपने-आप भरेंगे।", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(date, { date = it; result = null }, label = { Text("जन्म तारीख YYYY-MM-DD") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(time, { time = it; result = null }, label = { Text("जन्म समय HH:MM (24-hour)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(place, { place = it; placeMessage = null }, label = { Text("जन्मस्थान खोजें (शहर / गाँव)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Button(enabled = place.isNotBlank() && !searching, onClick = {
            scope.launch {
                searching = true
                placeMessage = null
                placeChoices = emptyList()
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val found = withContext(Dispatchers.IO) {
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocationName(place.trim(), 6) ?: emptyList()
                    }
                    placeChoices = found
                    if (found.isEmpty()) placeMessage = "स्थान नहीं मिला। नाम के साथ जिला/राज्य भी लिखें।"
                    else if (found.size == 1) {
                        val a = found.first()
                        latitude = a.latitude.toString()
                        longitude = a.longitude.toString()
                        if (a.countryCode.equals("IN", true)) zone = "Asia/Kolkata"
                        place = listOfNotNull(a.locality, a.subAdminArea, a.adminArea, a.countryName).distinct().joinToString(", ")
                        placeMessage = "स्थान चुना गया। निर्देशांक भर दिए गए हैं।"
                        placeChoices = emptyList()
                    } else placeMessage = "सही स्थान चुनें:"
                } catch (e: Exception) {
                    placeMessage = "स्थान खोज उपलब्ध नहीं है। इंटरनेट/Geocoder सेवा जाँचें या नीचे निर्देशांक भरें।"
                } finally { searching = false }
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (searching) "खोज रहे हैं…" else "स्थान खोजें") }
        placeMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = Color(0xFF8B3D20)) }
        placeChoices.forEach { address ->
            TextButton(onClick = {
                latitude = address.latitude.toString()
                longitude = address.longitude.toString()
                if (address.countryCode.equals("IN", true)) zone = "Asia/Kolkata"
                place = address.getAddressLine(0) ?: place
                placeChoices = emptyList()
                placeMessage = "स्थान चुना गया • निर्देशांक अपडेट"
                result = null
            }, modifier = Modifier.fillMaxWidth()) {
                Text(address.getAddressLine(0) ?: "${address.latitude}, ${address.longitude}")
            }
        }
        OutlinedTextField(latitude, { latitude = it; result = null }, label = { Text("अक्षांश (स्वतः / manual)") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(longitude, { longitude = it; result = null }, label = { Text("देशांतर (स्वतः / manual)") }, modifier = Modifier.fillMaxWidth())
        Text("समय क्षेत्र: $zone", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Text("भारत के स्थानों के लिए Asia/Kolkata स्वतः लगाया जाता है। अन्य देशों में अभी डिवाइस का समय क्षेत्र लागू हो सकता है; जाँच लें।", style = MaterialTheme.typography.labelSmall)
        Button(onClick = {
            try {
                val instant = LocalDateTime.parse("${date.trim()}T${time.trim()}").atZone(ZoneId.of(zone.trim())).toInstant()
                birthEpochMillis = instant.toEpochMilli()
                result = FreeVedicAstronomy.birthChart(instant.toEpochMilli(), latitude.toDouble(), longitude.toDouble())
                error = null
            } catch (e: Exception) { error = "जन्म विवरण जाँचें: ${e.message}"; result = null }
        }, modifier = Modifier.fillMaxWidth()) { Text("विस्तृत लग्न कुंडली बनाएँ") }
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        result?.let { chart ->
            val signNames = (0..11).map { FreeVedicAstronomy.signName(it) }
            val planetsByHouse = chart.planets.groupBy { it.house ?: 1 }
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("जन्म विवरण", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("$place • $date • $time ($zone)")
                    Text("लग्न: ${signNames[chart.ascendant.signIndex]} • ${"%.2f".format(chart.ascendant.degreesInSign)}°", fontWeight = FontWeight.Bold, color = Color(0xFF8B3D20))
                    Text("अयनांश: ${"%.4f".format(chart.ayanamsha)}° • भाव पद्धति: राशि-आधारित Whole Sign", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text("द्वादश भाव लग्न कुंडली", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            (1..12).chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEach { house ->
                        val signIndex = (chart.ascendant.signIndex + house - 1) % 12
                        val occupants = planetsByHouse[house].orEmpty()
                        ElevatedCard(Modifier.weight(1f)) {
                            Column(Modifier.padding(11.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("$house भाव", fontWeight = FontWeight.Bold, color = Color(0xFF8B3D20))
                                Text(signNames[signIndex], fontWeight = FontWeight.SemiBold)
                                Text(if (occupants.isEmpty()) "कोई ग्रह नहीं" else occupants.joinToString(" • ") { it.name }, style = MaterialTheme.typography.bodySmall)
                                occupants.forEach { Text("${"%.1f".format(it.degreesInSign)}°", style = MaterialTheme.typography.labelSmall) }
                            }
                        }
                    }
                }
            }
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("ग्रहों की विस्तृत स्थिति", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    chart.planets.forEach { p ->
                        Text("${p.name}: ${signNames[p.signIndex]} ${"%.2f".format(p.degreesInSign)}° • भाव ${p.house}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Text("भावानुसार पारंपरिक फलादेश", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            val topics = listOf("स्वभाव, शरीर, व्यक्तित्व और जीवन-दिशा","धन, वाणी, परिवार और संचय","साहस, पराक्रम, भाई-बहन और संचार","माता, घर, सुख, भूमि और वाहन","विद्या, बुद्धि, संतान, सृजन और मंत्र","सेवा, ऋण, रोग और प्रतिस्पर्धा","विवाह, साझेदारी और सार्वजनिक संबंध","आयु, परिवर्तन, गूढ़ विषय और साझा संसाधन","भाग्य, धर्म, गुरु, उच्च शिक्षा और यात्रा","कर्म, पेशा, प्रतिष्ठा और उत्तरदायित्व","लाभ, मित्र, नेटवर्क और आकांक्षाएँ","व्यय, एकांत, विदेश और आध्यात्मिक साधना")
            (1..12).forEach { house ->
                val occupants = planetsByHouse[house].orEmpty()
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("$house भाव • ${signNames[(chart.ascendant.signIndex + house - 1) % 12]}", fontWeight = FontWeight.Bold)
                        Text("मुख्य विषय: ${topics[house-1]}", style = MaterialTheme.typography.bodySmall)
                        if (occupants.isEmpty()) {
                            Text("इस भाव में कोई ग्रह स्थित नहीं है। पारंपरिक ज्योतिष में इसका फल केवल खाली होने से तय नहीं होता; भावेश, दृष्टि, युति और दशा भी देखी जाती है।", style = MaterialTheme.typography.bodySmall)
                        } else {
                            occupants.forEach { p ->
                                val reading = when(p.name) {
                                    "सूर्य" -> "आत्मविश्वास, नेतृत्व, अधिकार और पिता/प्रशासन से जुड़े विषयों पर जोर माना जाता है।"
                                    "चन्द्र" -> "मन, भावनाएँ, माता, जनसंपर्क और मानसिक अनुकूलन के विषय प्रमुख माने जाते हैं।"
                                    "मंगल" -> "ऊर्जा, साहस, भूमि, प्रतिस्पर्धा और त्वरित कार्रवाई के संकेत माने जाते हैं।"
                                    "बुध" -> "बुद्धि, संवाद, गणना, व्यापार और सीखने की प्रवृत्ति से जोड़ा जाता है।"
                                    "गुरु" -> "ज्ञान, मार्गदर्शन, विस्तार, नैतिकता और परंपरागत शुभता से जोड़ा जाता है।"
                                    "शुक्र" -> "संबंध, कला, सुविधा, सौंदर्य और भौतिक सुख के विषयों से जोड़ा जाता है।"
                                    "शनि" -> "अनुशासन, विलंब, श्रम, जिम्मेदारी और दीर्घकालिक प्रयास के विषय माने जाते हैं।"
                                    "राहु" -> "असामान्य महत्वाकांक्षा, नवीनता, विदेशी/अपरंपरागत विषय और तीव्र इच्छा से जोड़ा जाता है।"
                                    else -> "वैराग्य, शोध, आंतरिक परिवर्तन और आध्यात्मिकता के विषयों से पारंपरिक रूप में जोड़ा जाता है।"
                                }
                                Text("${p.name}: $reading", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            val moon = chart.planets.firstOrNull { it.name == "चन्द्र" }?.longitude
            val birthMs = birthEpochMillis
            if (moon != null && birthMs != null) {
                val dashas = FreeVedicAstronomy.vimshottariMahadashas(birthMs, moon, 18)
                val now = System.currentTimeMillis()
                Text("विंशोत्तरी महादशा एवं अंतर्दशा", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                dashas.take(9).forEach { period ->
                    val active = now >= period.startMillis && now < period.endMillis
                    Text((if (active) "▶ " else "") + "${period.lord}: ${java.time.Instant.ofEpochMilli(period.startMillis).atZone(ZoneId.of(zone)).toLocalDate()} – ${java.time.Instant.ofEpochMilli(period.endMillis).atZone(ZoneId.of(zone)).toLocalDate()}" + if (active) " (वर्तमान)" else "")
                    if (active) FreeVedicAstronomy.vimshottariAntardashas(period).forEach { sub ->
                        val subActive = now >= sub.startMillis && now < sub.endMillis
                        Text("   ${if (subActive) "▶ " else ""}${period.lord}/${sub.lord}: ${java.time.Instant.ofEpochMilli(sub.startMillis).atZone(ZoneId.of(zone)).toLocalDate()} – ${java.time.Instant.ofEpochMilli(sub.endMillis).atZone(ZoneId.of(zone)).toLocalDate()}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Text("वर्तमान गोचर (जन्म लग्न से भाव)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            FreeVedicAstronomy.currentTransits(System.currentTimeMillis(), chart.ascendant.signIndex).forEach { transit ->
                Text("${transit.name}: ${signNames[transit.signIndex]} • भाव ${transit.house}")
            }
        }
        Text("महत्वपूर्ण: यह पारंपरिक ज्योतिषीय व्याख्या है, निश्चित या वैज्ञानिक भविष्यवाणी नहीं। अभी यह प्रारंभिक D1 राशि-आधारित चार्ट है; नवांश, ग्रह-दृष्टि, भावेश बल, वक्री/अस्त स्थिति और मिथिला पंचांग से स्वतंत्र सत्यापन आगे जोड़ना/जाँचना बाकी है।", style = MaterialTheme.typography.labelSmall)
    }
}
