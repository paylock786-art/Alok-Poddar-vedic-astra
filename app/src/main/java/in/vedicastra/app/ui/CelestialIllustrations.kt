package com.vedicastra.app.ui
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vedicastra.app.domain.NakshatraCatalog
private val planets=listOf("सूर्य" to Color(0xFFFFA726),"चन्द्र" to Color(0xFFECEFF1),"मंगल" to Color(0xFFB64B3A),"बुध" to Color(0xFF82B879),"गुरु" to Color(0xFFD6A84B),"शुक्र" to Color(0xFFE9C6B8),"शनि" to Color(0xFF78909C),"राहु" to Color(0xFF6D5A8D),"केतु" to Color(0xFF9A795B))
@Composable fun CelestialGalleryScreen(modifier:Modifier=Modifier){
 Column(modifier){Text("ग्रह एवं नक्षत्र चित्रावली",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold);Text("मौलिक vector illustrations • offline उपलब्ध • दूरबीन से ली गई वास्तविक तस्वीरें नहीं।",style=MaterialTheme.typography.bodySmall)
 LazyVerticalGrid(columns=GridCells.Fixed(2),modifier=Modifier.weight(1f),contentPadding=PaddingValues(vertical=10.dp),verticalArrangement=Arrangement.spacedBy(10.dp),horizontalArrangement=Arrangement.spacedBy(10.dp)){
  items(planets){(name,tint)->ElevatedCard{Column(Modifier.padding(10.dp)){Canvas(Modifier.fillMaxWidth().height(112.dp)){
   val r=size.minDimension*.29f; val c=Offset(size.width/2,size.height/2); drawCircle(tint,r,c);drawCircle(Color(0xFFFFE8B0).copy(alpha=.7f),r*1.2f,c,style=Stroke(2.dp.toPx()))
   if(name=="शनि") drawOval(color=Color(0xFFD9C49C),topLeft=Offset(size.width*.19f,size.height*.37f),size=androidx.compose.ui.geometry.Size(size.width*.62f,size.height*.26f),style=Stroke(5.dp.toPx()))
   if(name=="सूर्य") for(i in 0..11){val a=Math.PI*i/6;drawLine(tint,Offset(c.x+(r*1.3*kotlin.math.cos(a)).toFloat(),c.y+(r*1.3*kotlin.math.sin(a)).toFloat()),Offset(c.x+(r*1.65*kotlin.math.cos(a)).toFloat(),c.y+(r*1.65*kotlin.math.sin(a)).toFloat()),3.dp.toPx())}
  };Text(name,fontWeight=FontWeight.Bold)}}}
  items(NakshatraCatalog.all){n->ElevatedCard{Column(Modifier.padding(10.dp)){Canvas(Modifier.fillMaxWidth().height(90.dp)){val c=Offset(size.width/2,size.height/2);drawCircle(Color(0xFFD7A64A),size.minDimension*.36f,c,style=Stroke(2.dp.toPx()));drawCircle(Color(0xFF9C6537),size.minDimension*.19f,c,style=Stroke(1.dp.toPx()));for(i in 0..7){val a=2*Math.PI*i/8;drawCircle(Color(0xFFFFC66D),3.dp.toPx(),Offset(c.x+(size.minDimension*.29*kotlin.math.cos(a)).toFloat(),c.y+(size.minDimension*.29*kotlin.math.sin(a)).toFloat()))}};Text("%02d • %s".format(n.number,n.name),fontWeight=FontWeight.Bold);Text(n.symbol,style=MaterialTheme.typography.bodySmall);Text("देवता: "+n.deity,style=MaterialTheme.typography.labelSmall)}}}
 }
 }
}