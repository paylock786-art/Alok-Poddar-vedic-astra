package com.vedicastra.app.domain

data class NakshatraInfo(val number: Int, val name: String, val deity: String, val symbol: String)

object NakshatraCatalog {
    val all = listOf(
        NakshatraInfo(1,"अश्विनी","अश्विनी कुमार","घोड़े का सिर"),
        NakshatraInfo(2,"भरणी","यम","योनि"),
        NakshatraInfo(3,"कृत्तिका","अग्नि","चाकू / अग्निशिखा"),
        NakshatraInfo(4,"रोहिणी","ब्रह्मा / प्रजापति","रथ / बैलगाड़ी"),
        NakshatraInfo(5,"मृगशिरा","सोम","मृग का सिर"),
        NakshatraInfo(6,"आर्द्रा","रुद्र","आंसू / हीरा"),
        NakshatraInfo(7,"पुनर्वसु","अदिति","तरकश"),
        NakshatraInfo(8,"पुष्य","बृहस्पति","कमल / पुष्प"),
        NakshatraInfo(9,"आश्लेषा","नाग","सर्प"),
        NakshatraInfo(10,"मघा","पितृगण","राजसिंहासन"),
        NakshatraInfo(11,"पूर्वा फाल्गुनी","भग","झूले का अगला पाया"),
        NakshatraInfo(12,"उत्तरा फाल्गुनी","अर्यमा","झूले का पिछला पाया"),
        NakshatraInfo(13,"हस्त","सविता","हाथ"),
        NakshatraInfo(14,"चित्रा","त्वष्टा / विश्वकर्मा","मणि"),
        NakshatraInfo(15,"स्वाती","वायु","अंकुर / मूंगा"),
        NakshatraInfo(16,"विशाखा","इन्द्र-अग्नि","तोरण"),
        NakshatraInfo(17,"अनुराधा","मित्र","कमल"),
        NakshatraInfo(18,"ज्येष्ठा","इन्द्र","कुंडल"),
        NakshatraInfo(19,"मूल","निरृति","जड़ों का गुच्छा"),
        NakshatraInfo(20,"पूर्वाषाढ़ा","आपः","पंखा / हाथी-दांत"),
        NakshatraInfo(21,"उत्तराषाढ़ा","विश्वेदेव","हाथी का दांत"),
        NakshatraInfo(22,"श्रवण","विष्णु","कान"),
        NakshatraInfo(23,"धनिष्ठा","अष्ट वसु","ढोल"),
        NakshatraInfo(24,"शतभिषा","वरुण","सौ औषधियां / वृत्त"),
        NakshatraInfo(25,"पूर्वा भाद्रपदा","अज एकपाद","दो मुख वाला पुरुष"),
        NakshatraInfo(26,"उत्तरा भाद्रपदा","अहिर्बुध्न्य","जुड़वां / शय्या"),
        NakshatraInfo(27,"रेवती","पूषा","मछली")
    )
}
