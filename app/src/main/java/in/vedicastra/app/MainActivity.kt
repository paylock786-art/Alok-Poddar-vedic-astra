package in.vedicastra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
    val modules = listOf(
        AstraModule("☀", mapOf(AppLanguage.HINDI to "वैदिक ज्योतिष", AppLanguage.MAITHILI to "वैदिक ज्योतिष", AppLanguage.ENGLISH to "Vedic Astrology"),
            mapOf(AppLanguage.HINDI to "जन्मकुंडली, दशा, गोचर और योग", AppLanguage.MAITHILI to "जन्मकुंडली, दशा आ गोचर", AppLanguage.ENGLISH to "Birth chart, dashas, transits and yogas")),
        AstraModule("☾", mapOf(AppLanguage.HINDI to "मिथिला पंचांग", AppLanguage.MAITHILI to "मिथिला पंचांग", AppLanguage.ENGLISH to "Mithila Panchang"),
            mapOf(AppLanguage.HINDI to "तिथि, नक्षत्र, मुहूर्त और पंचांग तुलना", AppLanguage.MAITHILI to "तिथि, नक्षत्र आ मुहूर्त", AppLanguage.ENGLISH to "Tithi, nakshatra, muhurta and almanac comparison")),
        AstraModule("१", mapOf(AppLanguage.HINDI to "अंक ज्योतिष विज्ञान", AppLanguage.MAITHILI to "अंक ज्योतिष", AppLanguage.ENGLISH to "Numerology"),
            mapOf(AppLanguage.HINDI to "जन्मांक, भाग्यांक, नामांक और अंक-चक्र", AppLanguage.MAITHILI to "जन्मांक, भाग्यांक आ नामांक", AppLanguage.ENGLISH to "Birth, destiny and name numbers")),
        AstraModule("✋", mapOf(AppLanguage.HINDI to "हस्तरेखा AI", AppLanguage.MAITHILI to "हस्तरेखा AI", AppLanguage.ENGLISH to "Palmistry AI"),
            mapOf(AppLanguage.HINDI to "हथेली की रेखाओं का पारंपरिक विश्लेषण", AppLanguage.MAITHILI to "हथेली रेखाक पारंपरिक अध्ययन", AppLanguage.ENGLISH to "Traditional interpretation of palm lines")),
        AstraModule("▣", mapOf(AppLanguage.HINDI to "टैरो रीडिंग", AppLanguage.MAITHILI to "टैरो रीडिंग", AppLanguage.ENGLISH to "Tarot Reading"),
            mapOf(AppLanguage.HINDI to "78 कार्ड और अलग-अलग स्प्रेड", AppLanguage.MAITHILI to "78 कार्ड आ अलग-अलग स्प्रेड", AppLanguage.ENGLISH to "78 cards and multiple spreads")),
        AstraModule("◉", mapOf(AppLanguage.HINDI to "सामुद्रिक शास्त्र", AppLanguage.MAITHILI to "सामुद्रिक शास्त्र", AppLanguage.ENGLISH to "Face Reading"),
            mapOf(AppLanguage.HINDI to "चेहरे के दृश्य संकेतों का पारंपरिक अध्ययन", AppLanguage.MAITHILI to "चेहराक दृश्य संकेतक पारंपरिक अध्ययन", AppLanguage.ENGLISH to "Traditional study of visible facial features")),
        AstraModule("ॐ", mapOf(AppLanguage.HINDI to "उपाय एवं पूजा", AppLanguage.MAITHILI to "उपाय आ पूजा", AppLanguage.ENGLISH to "Remedies & Puja"),
            mapOf(AppLanguage.HINDI to "मंत्र, पूजा-विधि, दान और रत्न-संदर्भ", AppLanguage.MAITHILI to "मंत्र, पूजा-विधि, दान आ रत्न", AppLanguage.ENGLISH to "Mantras, rituals, charity and gem traditions")),
        AstraModule("◷", mapOf(AppLanguage.HINDI to "समय विश्लेषण", AppLanguage.MAITHILI to "समय विश्लेषण", AppLanguage.ENGLISH to "Time Analysis"),
            mapOf(AppLanguage.HINDI to "अगले 1, 3, 6, 12 और 24 घंटे", AppLanguage.MAITHILI to "आगाँक 1, 3, 6, 12 आ 24 घंटा", AppLanguage.ENGLISH to "Next 1, 3, 6, 12 and 24 hours")),
        AstraModule("✓", mapOf(AppLanguage.HINDI to "परिणाम सत्यापन", AppLanguage.MAITHILI to "परिणाम जाँच", AppLanguage.ENGLISH to "Prediction Verification"),
            mapOf(AppLanguage.HINDI to "पूर्वानुमान और वास्तविक परिणाम का रिकॉर्ड", AppLanguage.MAITHILI to "अनुमान आ वास्तविक परिणामक रिकॉर्ड", AppLanguage.ENGLISH to "Track forecasts against observed outcomes"))
    )
    MaterialTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                Column {
                    Text("VEDIC ASTRA", fontWeight = FontWeight.Bold)
                    Text("Ancient Wisdom • Astronomical Precision", style = MaterialTheme.typography.labelSmall)
                }
            }, actions = {
                var expanded by remember { mutableStateOf(false) }
                TextButton(onClick = { expanded = true }) { Text(language.name) }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text("हिन्दी") }, onClick = { language = AppLanguage.HINDI; expanded = false })
                    DropdownMenuItem(text = { Text("मैथिली") }, onClick = { language = AppLanguage.MAITHILI; expanded = false })
                    DropdownMenuItem(text = { Text("English") }, onClick = { language = AppLanguage.ENGLISH; expanded = false })
                }
            })
        }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
                Text(when(language) {
                    AppLanguage.HINDI -> "आपका समेकित वैदिक ज्ञान मंच"
                    AppLanguage.MAITHILI -> "अहाँक समेकित वैदिक ज्ञान मंच"
                    AppLanguage.ENGLISH -> "Your integrated traditional knowledge platform"
                }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2), verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
                    items(modules) { module ->
                        ElevatedCard(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                                Text(module.icon, style = MaterialTheme.typography.headlineMedium)
                                Text(module.title[language] ?: module.title[AppLanguage.ENGLISH]!!, fontWeight = FontWeight.Bold)
                                Text(module.description[language] ?: module.description[AppLanguage.ENGLISH]!!, style = MaterialTheme.typography.bodySmall)
                                Text(when(language) {
                                    AppLanguage.HINDI -> "विकास चरण में"
                                    AppLanguage.MAITHILI -> "विकास जारी"
                                    AppLanguage.ENGLISH -> "In development"
                                }, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
                Text(when(language) {
                    AppLanguage.HINDI -> "ध्यान दें: ज्योतिष, हस्तरेखा, टैरो और सामुद्रिक व्याख्याएं पारंपरिक मान्यताएं हैं; इन्हें निश्चित या वैज्ञानिक भविष्यवाणी न मानें।"
                    AppLanguage.MAITHILI -> "ई सभ पारंपरिक व्याख्या अछि, निश्चित वैज्ञानिक भविष्यवाणी नहि।"
                    AppLanguage.ENGLISH -> "Astrology, palmistry, tarot and physiognomy are traditional interpretations, not scientifically established predictions."
                }, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
