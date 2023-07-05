package com.rnd.baseproject.withXml.adapter

import android.content.Context
import com.rnd.baseproject.databinding.DragItemBinding
import com.rnd.baseproject.withXml.BaseRecyclerAdapter
import com.rnd.baseproject.withXml.interfaces.AdapterCallback

class UseBaseAdapter(
    val context: Context,
    arrayList: ArrayList<String>,// data class here
    var frameName: String,
    var adapterCallback: AdapterCallback<String>
) : BaseRecyclerAdapter<String, DragItemBinding>(//data class and layout binding here
    DragItemBinding::inflate,
    arrayList
) {
    override fun MyViewHolder<DragItemBinding>.bindView(binding: DragItemBinding, item: String) {
        //set item list data
    }
}