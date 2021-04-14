package xyz.wannamaker.listeapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.wannamaker.listeapp.databinding.ActivityMainBinding
import xyz.wannamaker.listeapp.data.dataList


class ListofLists{

    companion object{
        var pickedDataList:dataList? = null
    }
}
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listLists.layoutManager = LinearLayoutManager(this)
        binding.listLists.adapter = ListAdapter(emptyList<dataList>(), this::onListClicked)

        ListManager.instance.onList = {
            (binding.listLists.adapter as ListAdapter).update(it)
        }

        ListManager.instance.load()

        binding.newBtn.setOnClickListener {
            val title = binding.title.text.toString()

            binding.title.setText("")

            newList(title)

            //automatically hide keyboard upon click but idc if this is the easiest way to do it
            val ipm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ipm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }



    }

    private fun newList(title: String) {
        val list = dataList(title, mutableListOf())
        ListManager.instance.newList(list)
    }

    private fun onListClicked(dataList: dataList) {
        ListofLists.pickedDataList = dataList

        val intent = Intent(this, ListDetailsActivity::class.java)

        startActivity(intent)
    }

}