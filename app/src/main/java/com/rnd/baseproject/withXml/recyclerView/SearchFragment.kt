package com.rnd.baseproject.withXml.recyclerView

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rnd.baseproject.R
import com.rnd.baseproject.databinding.FragmentSearchBinding
import com.rnd.baseproject.withXml.BaseXmlFragment
import com.rnd.baseproject.withXml.adapter.DragAdapter
import java.util.Collections


class SearchFragment : BaseXmlFragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: DragAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        val arrayList = arrayListOf(
            "Mann",
            "Harsh",
            "Jaimin",
            "Vipul",
            "Hardip",
            "Ravi",
            "Jignesh",
            "Hitesh",
            "Raj",
            "Deep"
        )
        adapter = DragAdapter(arrayList, requireContext())
        binding.rvDragAndDrop.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.rvDragAndDrop.adapter = adapter

        binding.searchView.doOnTextChanged { text, _, _, _ ->
            if (binding.searchView.hasFocus()) {
                adapter.getFilter(text.toString())
            }
        }
        val ithCallBack = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeFlag(
                    ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(arrayList, viewHolder.adapterPosition, target.adapterPosition)
                // and notify the adapter that its dataset has changed
                adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

        }

        val ith = ItemTouchHelper(ithCallBack)
        ith.attachToRecyclerView(binding.rvDragAndDrop)

    }

}