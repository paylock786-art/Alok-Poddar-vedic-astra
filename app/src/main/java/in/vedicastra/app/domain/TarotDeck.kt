package com.vedicastra.app.domain

data class TarotCard(val name: String, val upright: String, val reversed: String)

object TarotDeck {
    private val major = listOf(
        "The Fool","The Magician","The High Priestess","The Empress","The Emperor","The Hierophant","The Lovers","The Chariot","Strength","The Hermit","Wheel of Fortune","Justice","The Hanged Man","Death","Temperance","The Devil","The Tower","The Star","The Moon","The Sun","Judgement","The World"
    )
    private val suits = listOf("Wands","Cups","Swords","Pentacles")
    private val ranks = listOf("Ace","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Page","Knight","Queen","King")
    private val keywords = mapOf(
        "Wands" to ("initiative, creativity and purposeful action" to "scattered energy, delay and impatience"),
        "Cups" to ("emotions, relationships and intuition" to "emotional imbalance, withdrawal and blocked feelings"),
        "Swords" to ("thought, communication and clear decisions" to "confusion, harsh words and overthinking"),
        "Pentacles" to ("work, resources and practical growth" to "insecurity, poor planning and material strain")
    )
    private val majorMeanings = listOf(
        "नई शुरुआत और खुले मन से सीखना" to "लापरवाही या तैयारी की कमी",
        "कौशल, पहल और उपलब्ध साधनों का उपयोग" to "बिखरा ध्यान या अवसर का दुरुपयोग",
        "अंतर्ज्ञान, धैर्य और भीतर की समझ" to "अंतर्ज्ञान की अनदेखी या छिपी उलझन",
        "सृजन, पोषण और उर्वरता" to "अपनी देखभाल की कमी या रचनात्मक रुकावट",
        "अनुशासन, संरचना और जिम्मेदारी" to "कठोरता या नियंत्रण की अधिकता",
        "परंपरा, अध्ययन और मार्गदर्शन" to "पुराने ढर्रे पर प्रश्न या स्वतंत्र सोच",
        "संबंध, मूल्य और महत्वपूर्ण चुनाव" to "असंगति या निर्णय में दुविधा",
        "दृढ़ संकल्प और दिशा में प्रगति" to "नियंत्रण की कमी या जल्दबाजी",
        "साहस, करुणा और आत्मसंयम" to "आत्म-संदेह या आवेग",
        "आत्मचिंतन और विवेकपूर्ण विराम" to "अलगाव या सलाह से बचना",
        "परिवर्तनशील चक्र और नए अवसर" to "बदलाव का विरोध या अस्थिरता",
        "निष्पक्षता, जवाबदेही और संतुलन" to "पक्षपात या तथ्य अधूरे होना",
        "दृष्टिकोण बदलना और धैर्य" to "अनावश्यक ठहराव या प्रतिरोध",
        "समापन और रूपांतरण" to "बदलाव से चिपके रहना",
        "मध्यमार्ग, संयम और सामंजस्य" to "अतिरेक या असंतुलन",
        "बंधनकारी आदतों को पहचानना" to "बंधन से मुक्ति की शुरुआत",
        "अचानक बदलाव और पुरानी संरचना का टूटना" to "टकराव से बचना या भीतर का तनाव",
        "आशा, प्रेरणा और पुनर्निर्माण" to "निराशा या विश्वास की कमी",
        "कल्पना, अनिश्चितता और अंतर्मन" to "भ्रम कम होना या छिपे तथ्य सामने आना",
        "स्पष्टता, आनंद और जीवन्तता" to "अस्थायी उदासी या अपेक्षाओं का दबाव",
        "आत्ममूल्यांकन और नई पुकार" to "आत्म-संदेह या पुराने अध्याय से न सीखना",
        "पूर्णता, एकीकरण और उपलब्धि" to "अधूरापन या समापन में विलंब"
    )
    val cards: List<TarotCard> = buildList {
        major.forEachIndexed { i, name -> add(TarotCard(name, majorMeanings[i].first, majorMeanings[i].second)) }
        suits.forEach { suit ->
            ranks.forEach { rank ->
                val pair = keywords.getValue(suit)
                add(TarotCard("$rank of $suit", pair.first, pair.second))
            }
        }
    }
    fun draw(count: Int = 3): List<Pair<TarotCard, Boolean>> {
        require(count in 1..cards.size)
        return cards.shuffled().take(count).map { it to (kotlin.random.Random.nextBoolean()) }
    }
}
