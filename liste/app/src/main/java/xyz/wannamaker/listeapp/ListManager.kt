package xyz.wannamaker.listeapp

import xyz.wannamaker.listeapp.data.dataList

class ListManager {

    private lateinit var listDataList: MutableList<dataList>

    var onList: ((List<dataList>) -> Unit)? = null
    var onListUpdate: ((list: dataList) -> Unit)? = null

    fun load() {

        //Have not implemented firebase functionality, but this is where you would fetch it from the DB

        //Instead take an example
        listDataList = mutableListOf(
            dataList("Viktig liste", mutableListOf(
                "Kjøpe egg",
                "Kjøpe mer \$GME",
                "Regne ut den reelle massen til singulariteten")),
            dataList("Ønskeliste", mutableListOf(
                "Carmen Electra",
                "Korona vaksine",
                ">=C på dette prosjektet!"
            ))
        )
        onList?.invoke(listDataList)
    }

    fun newList(dataList: dataList) {
        listDataList.add(dataList)
        onList?.invoke(listDataList)
    }

    companion object {
        val instance = ListManager()
    }
}