package com.rnd.baseproject.withXml.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rnd.baseproject.databinding.DragItemBinding

class DragAdapter(var arrayList: ArrayList<String>, context: Context) :
    RecyclerView.Adapter<DragAdapter.MyViewHolder>() {
    class MyViewHolder(var binding: DragItemBinding) : RecyclerView.ViewHolder(binding.root)

    private fun MyViewHolder.bind(item: String) {
        binding.tvHello.text = item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DragItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    var tempList = ArrayList(arrayList)


    fun getFilter(text: String) {
        arrayList.clear()
        if (text.isEmpty()) {
            arrayList.addAll(tempList)
        } else {
            tempList.forEach {
                if (it.startsWith(
                        text,
                        true
                    )
                ) {
                    arrayList.add(it)
                }
            }
        }
        notifyDataSetChanged()
    }

}