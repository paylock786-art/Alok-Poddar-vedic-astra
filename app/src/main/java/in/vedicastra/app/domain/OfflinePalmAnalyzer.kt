package com.vedicastra.app.domain
import android.graphics.Bitmap
import kotlin.math.abs
import kotlin.math.sqrt
data class PalmImageReport(val width:Int,val height:Int,val brightness:Double,val contrast:Double,val edgeDensity:Double,val horizontalLineScore:Double,val verticalLineScore:Double,val quality:String,val note:String)
/** On-device classical computer vision, not a trained neural-network palm reader. */
object OfflinePalmAnalyzer {
 fun analyze(source:Bitmap):PalmImageReport {
  require(source.width>=160 && source.height>=160) { "Image is too small; use a clear palm photo." }
  val b=Bitmap.createScaledBitmap(source,320,320,true); val g=IntArray(320*320); var sum=0.0
  for(y in 0 until 320) for(x in 0 until 320){ val c=b.getPixel(x,y); val v=(.299*((c shr 16) and 255)+.587*((c shr 8) and 255)+.114*(c and 255)).toInt(); g[y*320+x]=v; sum+=v }
  val mean=sum/g.size; var variance=0.0; var edges=0; var h=0; var v=0; var samples=0
  for(y in 1 until 319) for(x in 1 until 319){
   val i=y*320+x; val z=g[i].toDouble(); variance+=(z-mean)*(z-mean)
   val gx=-g[i-321]+g[i-319]-2*g[i-1]+2*g[i+1]-g[i+321]+g[i+319]
   val gy=-g[i-321]-2*g[i-320]-g[i-319]+g[i+321]+2*g[i+320]+g[i+319]
   if(sqrt((gx*gx+gy*gy).toDouble())>115) edges++
   if(abs(gy)>90 && abs(gx)<150) h++; if(abs(gx)>90 && abs(gy)<150) v++; samples++
  }
  val contrast=sqrt(variance/g.size); val density=edges.toDouble()/samples
  val quality=when { mean<55 || mean>225 -> "Poor lighting"; contrast<18 -> "Low contrast"; density<.025 -> "Low visible detail"; else -> "Usable image" }
  return PalmImageReport(source.width,source.height,mean,contrast,density,h.toDouble()/samples,v.toDouble()/samples,quality,"Image features only; named palm lines and traditional predictions are not identified.")
 }
}