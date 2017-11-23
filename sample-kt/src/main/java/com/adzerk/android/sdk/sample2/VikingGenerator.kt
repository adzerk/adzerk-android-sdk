package com.adzerk.android.sdk.sample2

import android.content.res.Resources
import java.util.*

class VikingGenerator(resources: Resources, val positions: Int = 20, val max: Int = 88) {

    private var vikings: Array<VikingGenerator.Viking?>

    private val names: Array<String>
    private val quotes: Array<String>

    private val random: Random = Random()

    init {
        vikings = arrayOfNulls<VikingGenerator.Viking>(positions)
        names = resources.getStringArray(R.array.viking_names)
        quotes = resources.getStringArray(R.array.viking_quotes)
    }

    class Viking(val url: String, val name: String, val quote: String)

    fun getViking(position: Int): Viking {

        if (position > vikings.size) {
            throw IllegalArgumentException("Position $position exceeds maximum")
        }

        var viking = vikings[position]

        if (viking == null) {
            viking = Viking(createHeadshotUrl(), createName(), createQuote())
            vikings[position] = viking
        }

        return viking
    }

    fun count(): Int {
        return vikings.size
    }

    private fun createHeadshotUrl(): String {
        return String.format("https://api.randomuser.me/portraits/med/women/%d.jpg", random.nextInt(max))
    }

    private fun createName(): String {
        return names[random.nextInt(names.size)]
    }

    private fun createQuote(): String {
        return quotes[random.nextInt(quotes.size)]
    }
}
