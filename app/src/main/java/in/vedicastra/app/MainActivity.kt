package com.vedicastra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import java.time.LocalDate
import com.vedicastra.app.domain.NumerologyEngine
import com.vedicastra.app.domain.NumberMethod
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
private fun VedicAstraApp() {\n    var showNumerology by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf(AppLanguage.HINDI) }
    val modules = listOf(
        AstraModule("☀", mapOf(AppLanguage.HINDI to "वैदिक ज्योतिष", AppLanguage.MAITHILI to "वैदिक ज्योतिष", AppLanguage.ENGLISH to "Vedic Astrology"), mapOf(AppLanguage.HINDI to "जन्मकुंडली, दशा, गोचर और योग", AppLanguage.MAITHILI to "जन्मकुंडली, दशा आ गोचर", AppLanguage.ENGLISH to "Birth chart, dashas, transits and yogas")),
        AstraModule("☾", mapOf(AppLanguage.HINDI to "मिथिला पंचांग", AppLanguage.MAITHILI to "मिथिला पंचांग", AppLanguage.ENGLISH to "Mithila Panchang"), mapOf(AppLanguage.HINDI to "तिथि, नक्षत्र, मुहूर्त और पंचांग तुलना", AppLanguage.MAITHILI to "तिथि, नक्षत्र आ मुहूर्त", AppLanguage.ENGLISH to "Tithi, nakshatra, muhurta and almanac comparison")),
        AstraModule("१", mapOf(AppLanguage.HINDI to "अंक ज्योतिष", AppLanguage.MAITHILI to "अंक ज्योतिष", AppLanguage.ENGLISH to "Numerology"), mapOf(AppLanguage.HINDI to "जन्मांक, भाग्यांक, नामांक और अंक-चक्र", AppLanguage.MAITHILI to "जन्मांक, भाग्यांक आ नामांक", AppLanguage.ENGLISH to "Birth, destiny and name numbers")),
        AstraModule("✋", mapOf(AppLanguage.HINDI to "हस्तरेखा AI", AppLanguage.MAITHILI to "हस्तरेखा AI", AppLanguage.ENGLISH to "Palmistry AI"), mapOf(AppLanguage.HINDI to "हथेली की रेखाओं का पारंपरिक विश्लेषण", AppLanguage.MAITHILI to "हथेली रेखाक पारंपरिक अध्ययन", AppLanguage.ENGLISH to "Traditional interpretation of palm lines")),
        AstraModule("✧", mapOf(AppLanguage.HINDI to "टैरो रीडिंग", AppLanguage.MAITHILI to "टैरो रीडिंग", AppLanguage.ENGLISH to "Tarot Reading"), mapOf(AppLanguage.HINDI to "78 कार्ड और अलग-अलग स्प्रेड", AppLanguage.MAITHILI to "78 कार्ड आ अलग-अलग स्प्रेड", AppLanguage.ENGLISH to "78 cards and multiple spreads")),
        AstraModule("◉", mapOf(AppLanguage.HINDI to "सामुद्रिक शास्त्र", AppLanguage.MAITHILI to "सामुद्रिक शास्त्र", AppLanguage.ENGLISH to "Face Reading"), mapOf(AppLanguage.HINDI to "चेहरे के दृश्य संकेतों का पारंपरिक अध्ययन", AppLanguage.MAITHILI to "चेहराक दृश्य संकेतक पारंपरिक अध्ययन", AppLanguage.ENGLISH to "Traditional study of visible facial features")),
        AstraModule("ॐ", mapOf(AppLanguage.HINDI to "उपाय एवं पूजा", AppLanguage.MAITHILI to "उपाय आ पूजा", AppLanguage.ENGLISH to "Remedies & Puja"), mapOf(AppLanguage.HINDI to "मंत्र, पूजा-विधि, दान और रत्न-संदर्भ", AppLanguage.MAITHILI to "मंत्र, पूजा-विधि, दान आ रत्न", AppLanguage.ENGLISH to "Mantras, rituals, charity and gem traditions")),
        AstraModule("✺", mapOf(AppLanguage.HINDI to "नक्षत्र दर्शन", AppLanguage.MAITHILI to "नक्षत्र दर्शन", AppLanguage.ENGLISH to "Nakshatra Guide"), mapOf(AppLanguage.HINDI to "27 नक्षत्र, प्रतीक और अधिष्ठाता देवता", AppLanguage.MAITHILI to "27 नक्षत्र, प्रतीक आ देवता", AppLanguage.ENGLISH to "27 nakshatras, symbols and presiding deities")),
        AstraModule("◷", mapOf(AppLanguage.HINDI to "समय विश्लेषण", AppLanguage.MAITHILI to "समय विश्लेषण", AppLanguage.ENGLISH to "Time Analysis"), mapOf(AppLanguage.HINDI to "अगले 1, 3, 6, 12 और 24 घंटे", AppLanguage.MAITHILI to "आगाँक 1, 3, 6, 12 आ 24 घंटा", AppLanguage.ENGLISH to "Next 1, 3, 6, 12 and 24 hours")),
        AstraModule("✓", mapOf(AppLanguage.HINDI to "परिणाम सत्यापन", AppLanguage.MAITHILI to "परिणाम जाँच", AppLanguage.ENGLISH to "Prediction Verification"), mapOf(AppLanguage.HINDI to "पूर्वानुमान और वास्तविक परिणाम का रिकॉर्ड", AppLanguage.MAITHILI to "अनुमान आ वास्तविक परिणामक रिकॉर्ड", AppLanguage.ENGLISH to "Track forecasts against observed outcomes"))
    )
    val deepMaroon = Color(0xFF32120F)
    val saffron = Color(0xFFFFC66D)
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF8B3D20), secondary = Color(0xFFB77B32), background = Color(0xFFFFF8ED), surface = Color(0xFFFFFCF6))) {
        Scaffold(containerColor = Color(0xFFFFF8ED), topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFFF8ED)),
                title = { Column { Text("VEDIC ASTRA", fontWeight = FontWeight.ExtraBold); Text("सनातन ज्ञान • खगोलीय गणना", style = MaterialTheme.typography.labelSmall) } },
                actions = {
                    var expanded by remember { mutableStateOf(false) }
                    TextButton(onClick = { expanded = true }) { Text(when(language) { AppLanguage.HINDI -> "हिन्दी"; AppLanguage.MAITHILI -> "मैथिली"; AppLanguage.ENGLISH -> "English" }) }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("हिन्दी") }, onClick = { language = AppLanguage.HINDI; expanded = false })
                        DropdownMenuItem(text = { Text("मैथिली") }, onClick = { language = AppLanguage.MAITHILI; expanded = false })
                        DropdownMenuItem(text = { Text("English") }, onClick = { language = AppLanguage.ENGLISH; expanded = false })
                    }
                }
            )
        }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 14.dp)) {
                Box(
                    Modifier.fillMaxWidth().height(190.dp)
                        .background(Brush.verticalGradient(listOf(Color(0xFF5B2119), deepMaroon, Color(0xFF1E1420))), RoundedCornerShape(22.dp))
                        .padding(18.dp)
                ) {
                    Column(Modifier.align(Alignment.CenterStart).fillMaxWidth(0.70f), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                        Text("॥ ॐ ॥", color = saffron, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                        Text("वेदिक ज्ञान की दिव्य यात्रा", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("ग्रह • नक्षत्र • देवता • पंचांग", color = Color(0xFFFFDCA8), style = MaterialTheme.typography.bodyMedium)
                        Text("श्रद्धा के साथ अध्ययन, गणना में स्पष्टता", color = Color.White.copy(alpha = 0.86f), style = MaterialTheme.typography.labelMedium)
                    }
                    Text("☼\n✧\n☾", Modifier.align(Alignment.CenterEnd), color = saffron, style = MaterialTheme.typography.headlineLarge)
                }
                Spacer(Modifier.height(14.dp))
                Text(when(language) { AppLanguage.HINDI -> "आपका समेकित वैदिक ज्ञान मंच"; AppLanguage.MAITHILI -> "अहाँक समेकित वैदिक ज्ञान मंच"; AppLanguage.ENGLISH -> "Your integrated traditional knowledge platform" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(9.dp))
                LazyVerticalGrid(columns = GridCells.Fixed(2), verticalArrangement = Arrangement.spacedBy(10.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    items(modules) { module ->
                        ElevatedCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(17.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFDF8))) {
                            Column(Modifier.padding(13.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(module.icon, style = MaterialTheme.typography.headlineMedium, color = Color(0xFF9B4A25))
                                Text(module.title[language] ?: module.title[AppLanguage.ENGLISH]!!, fontWeight = FontWeight.Bold)
                                Text(module.description[language] ?: module.description[AppLanguage.ENGLISH]!!, style = MaterialTheme.typography.bodySmall, minLines = 2)
                                Text(when(language) { AppLanguage.HINDI -> "विकास चरण में"; AppLanguage.MAITHILI -> "विकास जारी"; AppLanguage.ENGLISH -> "In development" }, style = MaterialTheme.typography.labelSmall, color = Color(0xFF9A5528))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(when(language) { AppLanguage.HINDI -> "ज्योतिष, हस्तरेखा, टैरो और सामुद्रिक व्याख्याएं पारंपरिक मान्यताएं हैं; इन्हें निश्चित या वैज्ञानिक भविष्यवाणी न मानें।"; AppLanguage.MAITHILI -> "ई सभ पारंपरिक व्याख्या अछि, निश्चित वैज्ञानिक भविष्यवाणी नहि।"; AppLanguage.ENGLISH -> "These are traditional interpretations, not scientifically established predictions." }, style = MaterialTheme.typography.labelSmall, color = Color(0xFF6E6259))
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}
