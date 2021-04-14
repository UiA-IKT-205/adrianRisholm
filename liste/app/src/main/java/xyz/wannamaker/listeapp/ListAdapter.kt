package xyz.wannamaker.listeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.wannamaker.listeapp.data.dataList
import xyz.wannamaker.listeapp.databinding.ListLayoutBinding

class ListAdapter(private var lists:List<dataList>, private val onListClicked:(dataList) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

        class ViewHolder(val binding: ListLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind (list: dataList, onListClicked: (dataList) -> Unit) {
                binding.title.text = list.title.toString()

                binding.card.setOnClickListener {
                    onListClicked(list)
                }
            }
        }

    override fun getItemCount(): Int = lists.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = lists[position]
        holder.bind(list, onListClicked)
    }

    fun update(newList:List<dataList>){
        lists = newList
        notifyDataSetChanged()
    }

}