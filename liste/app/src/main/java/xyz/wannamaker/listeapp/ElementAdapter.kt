package xyz.wannamaker.listeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.wannamaker.listeapp.databinding.ElementLayoutBinding
import xyz.wannamaker.listeapp.databinding.ListLayoutBinding

class ElementAdapter(private var elements:List<String>) : RecyclerView.Adapter<ElementAdapter.ViewHolder>() {

    class ViewHolder(val binding: ElementLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (element: String) {
            binding.description.text = element
        }
    }

    override fun getItemCount(): Int = elements.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ElementLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val element = elements[position]
        holder.bind(element)
    }

    fun update(newElement:List<String>) {
        elements = newElement
        notifyDataSetChanged()
    }
}