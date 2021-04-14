package xyz.wannamaker.listeapp

import xyz.wannamaker.listeapp.data.dataList

class ElementManager {

    private lateinit var listElement: MutableList<String>

    var onElement: ((List<String>) -> Unit)? = null


    fun load(listname: dataList) {
        //Have not yet implemented firebase functionality, but this is where you would fetch it from the DB

        //Instead lets load it from RAM
        listElement = listname.elements
        onElement?.invoke(listElement)
    }

    fun newElement() {
        onElement?.invoke(listElement)
    }

    companion object {
        val instance = ElementManager()
    }
}