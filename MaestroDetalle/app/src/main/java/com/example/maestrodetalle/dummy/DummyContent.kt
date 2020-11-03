package com.example.maestrodetalle.dummy

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = HashMap()

//    private val COUNT = 3

    init {
        // Add some sample items.
//        for (i in 0..COUNT) {
//             addItem(createDummyItem(i))
//        }
        addItem(createDummyItem(0, "Hide"))
        addItem(createDummyItem(1, "Amazon"))
        addItem(createDummyItem(2, "Google"))
        addItem(createDummyItem(3, "Android"))
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

//    private fun createDummyItem(position: Int): DummyItem {
//        return DummyItem(position.toString(), "Item " + position, makeDetails(position))
//    }

    private fun createDummyItem(position: Int, name: String): DummyItem {
        return DummyItem(position.toString(), name, makeDetails(position))
    }

//    private fun makeDetails(position: Int): String {
//        val builder = StringBuilder()
//        builder.append("Details about Item: ").append(position)
//        for (i in 0..position - 1) {
//            builder.append("\nMore details information here.")
//        }
//        return builder.toString()
//    }

    private fun makeDetails(position: Int) : String{
        var url = ""
        when (position){
            1 -> url = "https://www.amazon.com/-/es/";
            2 -> url = "https://www.google.com/";
            3 -> url = "https://developer.android.com/studio";
        }
        return url
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}