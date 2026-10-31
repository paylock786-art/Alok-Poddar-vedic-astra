package com.vedicastra.app.ui
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vedicastra.app.domain.OfflinePalmAnalyzer
@Composable fun PalmistryOfflineScreen(modifier:Modifier=Modifier,language:AstraLanguage=AstraLanguage.HINDI){
 val context= LocalContext.current; var uri by remember { mutableStateOf<Uri?>(null) }; var report by remember { mutableStateOf<com.vedicastra.app.domain.PalmImageReport?>(null) }; var error by remember { mutableStateOf<String?>(null) }
 val picker= rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ selected-> uri=selected; report=null; error=null; if(selected!=null) try { val bmp=context.contentResolver.openInputStream(selected)?.use{BitmapFactory.decodeStream(it)}?:error("Image unreadable"); report=OfflinePalmAnalyzer.analyze(bmp) } catch(e:Exception){error=e.message?: "Image analysis failed"} }
 Column(modifier.verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)){
  Text(AstraStrings.text("palm_title",language),style=MaterialTheme.typography.headlineSmall)
  Text(AstraStrings.text("palm_note",language)); Text("स्पष्ट हथेली, समान रोशनी और पूरी हथेली फ्रेम में रखें।",style=MaterialTheme.typography.bodySmall)
  Button(onClick={picker.launch("image/*")}){Text(AstraStrings.text("palm_pick",language))}
  uri?.let{u-> val bmp= remember(u){context.contentResolver.openInputStream(u)?.use{BitmapFactory.decodeStream(it)}}; bmp?.let{Image(it.asImageBitmap(),null,Modifier.fillMaxWidth().heightIn(max=320.dp))} }
  report?.let{r-> ElevatedCard(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){
   Text(AstraStrings.text("palm_quality",language),style=MaterialTheme.typography.titleMedium); Text(r.quality)
   Text("Brightness: %.1f • Contrast: %.1f".format(r.brightness,r.contrast)); Text("Edge density: %.3f".format(r.edgeDensity))
   Text("Horizontal feature score: %.3f".format(r.horizontalLineScore)); Text("Vertical feature score: %.3f".format(r.verticalLineScore)); Text(r.note,style=MaterialTheme.typography.bodySmall)
  }}}
  error?.let{Text(it,color=MaterialTheme.colorScheme.error)}
  Text("सीमा: इस संस्करण में प्रशिक्षित neural-network hand-landmark model नहीं है। यह वास्तविक AI रेखा-पहचान नहीं करता; केवल ऑफलाइन image features मापता है।",style=MaterialTheme.typography.labelSmall)
 }
}
@Composable fun SamudrikaVisualScreen(modifier:Modifier=Modifier,language:AstraLanguage=AstraLanguage.HINDI){
 val context=LocalContext.current; var uri by remember{mutableStateOf<Uri?>(null)}; val picker=rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){uri=it}
 val bmp= remember(uri){uri?.let{context.contentResolver.openInputStream(it)?.use{stream->BitmapFactory.decodeStream(stream)}}}
 Column(modifier.verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)){
  Text(AstraStrings.text("sam_title",language),style=MaterialTheme.typography.headlineSmall); Text(AstraStrings.text("sam_note",language),style=MaterialTheme.typography.bodySmall)
  Text(AstraStrings.text("privacy",language),style=MaterialTheme.typography.bodySmall); Button(onClick={picker.launch("image/*")}){Text(AstraStrings.text("choose_face",language))}
  bmp?.let{Image(it.asImageBitmap(),null,Modifier.fillMaxWidth().heightIn(max=340.dp))}
  ElevatedCard(Modifier.fillMaxWidth()){Column(Modifier.padding(14.dp),verticalArrangement=Arrangement.spacedBy(7.dp)){
   Text("दृश्य निरीक्षण बिंदु / Visible observation guide",style=MaterialTheme.typography.titleMedium)
   listOf("Face outline / चेहरे की रूपरेखा","Forehead / ललाट","Eyebrow and eye position / भौंह व आंखें","Nose bridge and tip / नासिका","Mouth and chin / मुख व ठोड़ी","Lighting and camera angle / प्रकाश व कैमरा कोण").forEach{Text("• "+it)}
   Text("ये केवल दृश्य भागों के नाम हैं; स्वचालित landmark detection या चरित्र-निर्णय नहीं होता।",style=MaterialTheme.typography.labelSmall)
  }}
 }
}