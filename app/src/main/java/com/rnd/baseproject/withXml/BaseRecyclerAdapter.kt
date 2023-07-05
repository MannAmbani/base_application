package com.rnd.baseproject.withXml

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseRecyclerAdapter<T, B : ViewBinding>(
    val binding: (LayoutInflater) -> B, var arrayList: ArrayList<T>
) : RecyclerView.Adapter<BaseRecyclerAdapter.MyViewHolder<B>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder<B> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = binding.invoke(layoutInflater)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: MyViewHolder<B>, position: Int) {
        holder.bindView(holder.binding, arrayList[position])
    }

    abstract fun MyViewHolder<B>.bindView(binding: B, item: T)

    class MyViewHolder<B : ViewBinding>(var binding: B) : RecyclerView.ViewHolder(binding.root)
}