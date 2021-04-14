package xyz.wannamaker.listeapp

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import xyz.wannamaker.listeapp.data.dataList
import xyz.wannamaker.listeapp.databinding.ActivityListDetailsBinding

class ListDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListDetailsBinding
    private lateinit var list:dataList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val acquiredList = ListofLists.pickedDataList

        if (acquiredList != null) {
            list = acquiredList
        }
        else {
            //Todo error message
            finish()
        }

        binding.listElements.layoutManager = LinearLayoutManager(this)
        binding.listElements.adapter = ElementAdapter(emptyList())

        ElementManager.instance.onElement = {
            (binding.listElements.adapter as ElementAdapter).update(it)
        }

        ElementManager.instance.load(list)


        binding.title.text = list.title

        binding.addElementbtn.setOnClickListener {
            val element = binding.elementText.text.toString()

            binding.elementText.setText("")

            newElement(element)

            val ipm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            ipm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    fun newElement(element: String) {
        list.elements.add(element)
        ElementManager.instance.newElement()
    }
}