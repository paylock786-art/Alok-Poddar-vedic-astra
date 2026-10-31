package com.vedicastra.app.ui
enum class AstraLanguage { HINDI, MAITHILI, ENGLISH }
object AstraStrings {
 private val values=mapOf(
 "palm_title" to listOf("हस्तरेखा • ऑफलाइन चित्र विश्लेषण","हस्तरेखा • ऑफलाइन चित्र विश्लेषण","Palmistry • Offline image analysis"),
 "palm_pick" to listOf("हथेली की तस्वीर चुनें","हथेलीक तस्वीर चुनू","Choose palm photo"),
 "palm_quality" to listOf("चित्र गुणवत्ता","चित्र गुणवत्ता","Image quality"),
 "palm_note" to listOf("विश्लेषण इसी डिवाइस पर होता है; तस्वीर सर्वर पर नहीं भेजी जाती।","विश्लेषण एहि डिवाइस पर होइत अछि; फोटो सर्वर पर नहि जाइत अछि।","Analysis runs on this device; the photo is not uploaded."),
 "sam_title" to listOf("सामुद्रिक शास्त्र • दृश्य अध्ययन","सामुद्रिक शास्त्र • दृश्य अध्ययन","Samudrika Shastra • Visual study"),
 "sam_note" to listOf("यह केवल दृश्य संरचनाओं का शैक्षिक/पारंपरिक अध्ययन है। चेहरे से व्यक्तित्व, स्वास्थ्य या भविष्य का वैज्ञानिक निष्कर्ष नहीं निकाला जाता।","ई केवल दृश्य संरचनाक शैक्षिक/पारंपरिक अध्ययन अछि। चेहरा सँ व्यक्तित्व, स्वास्थ्य वा भविष्यक वैज्ञानिक निष्कर्ष नहि निकालल जाइत अछि।","Educational/traditional visual study only. No personality, health or future inference is made from a face."),
 "choose_face" to listOf("चेहरे की तस्वीर चुनें","चेहराक तस्वीर चुनू","Choose face photo"),
 "privacy" to listOf("तस्वीर चुनने से पहले संबंधित व्यक्ति की सहमति लें।","फोटो चुनबाक पहिने संबंधित व्यक्तिक सहमति लिअ।","Obtain the person's consent before selecting a photo.")
 )
 fun text(key:String,language:AstraLanguage):String=values[key]?.get(language.ordinal)?:key
}