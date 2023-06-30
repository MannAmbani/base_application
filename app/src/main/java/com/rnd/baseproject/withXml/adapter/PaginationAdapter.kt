package com.rnd.baseproject.withXml.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rnd.baseproject.databinding.DragItemBinding
import com.rnd.baseproject.databinding.ItemLoadingBinding
import com.rnd.baseproject.withXml.interfaces.AdapterCallback

class PaginationAdapter(
    var context: Context,
    var arrayList: ArrayList<String?>,
    var adapterCallback: AdapterCallback<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewTypeItem = 0
    private var viewTypeLoading = 1

    class MyViewHolder(val binding: DragItemBinding) : ViewHolder(binding.root)
    class MyLoadingViewHolder(val binding: ItemLoadingBinding) : ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (arrayList[position] == null) viewTypeLoading else viewTypeItem
    }

    private fun MyViewHolder.bind(item: String) {
        binding.tvHello.text = item
        binding.root.setOnClickListener {
            adapterCallback.onItemClick(it, adapterPosition, item)//absoluteAdapterPosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == viewTypeItem) MyViewHolder(
            DragItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        else MyLoadingViewHolder(
            ItemLoadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount() = arrayList.size

    private fun setItemVie(holder: MyViewHolder, position: Int) {
        holder.bind(arrayList[position]!!)
    }

    private fun setLoadingView() {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            setItemVie(holder, position)
        } else if (holder is MyLoadingViewHolder) {
            setLoadingView()
        }
    }

}