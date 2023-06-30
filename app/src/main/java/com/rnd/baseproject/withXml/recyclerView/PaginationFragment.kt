package com.rnd.baseproject.withXml.recyclerView

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rnd.baseproject.R
import com.rnd.baseproject.databinding.FragmentPaginationBinding
import com.rnd.baseproject.withXml.BaseXmlFragment
import com.rnd.baseproject.withXml.adapter.PaginationAdapter
import com.rnd.baseproject.withXml.interfaces.AdapterCallback


class PaginationFragment : BaseXmlFragment(R.layout.fragment_pagination), OnClickListener,
    AdapterCallback<String> {

    private lateinit var binding: FragmentPaginationBinding
    private var pageNumber = 1
    private var totalPage = 0
    private var perPage = 20
    private var isLoading = false
    private lateinit var adapter: PaginationAdapter
    val arrayList: ArrayList<String?> = arrayListOf(
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaginationBinding.bind(view)


        //getting data from server

        /**     viewModel.leadResponse.observe(viewLifecycleOwner) {
        if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) when (it.status) {
        Status.LOADING -> {
        DialogUtils.loadingDialog(requireContext())
        }
        Status.SUCCESS -> {
        DialogUtils.cancel()
        if (!isLoading) {
        arrayList = ArrayList()
        } else {
        try {
        arrayList.removeAt(arrayList.size - 1)
        adapter.notifyItemRemoved(arrayList.size - 1)
        isLoading = false
        binding.rvPagination.scrollToPosition(arrayList.size - 3)
        } catch (e: Exception) {
        e.printStackTrace()
        }
        }

        it.data?.let { it1 ->
        arrayList.addAll(it1.data)
        pageNumber = it.data.current_page + 1
        totalPage = it.data.total_pages
        }
        setAdapter()
        }
        Status.ERROR -> {
        DialogUtils.cancel()
        it.message?.let { it1 -> toast(it1) }
        if (!isLoading) {
        arrayList = ArrayList()
        } else {
        try {
        arrayList.removeAt(arrayList.size - 1)
        adapter.notifyItemRemoved(arrayList.size - 1)
        isLoading = false
        binding.rvPagination.scrollToPosition(arrayList.size - 3)
        } catch (e: Exception) {
        e.printStackTrace()
        }
        }
        }
        }
        }*/

//        setLoadMoreListener()
    }


    private fun setAdapter() {
        arrayList.reverse()
        adapter = PaginationAdapter(requireContext(), arrayList, this)
        binding.rvPagination.adapter = adapter

        /** for search view*/
//        binding.svLead.doOnTextChanged { text, _, _, _ ->
//            if (binding.svLead.hasFocus()) {
//                adapter.getFilter(text.toString())
//            }
//        }
    }

    private fun setLoadMoreListener() {
        binding.rvPagination.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                /**Condition to remove load more when search view is active*/
//                if (binding.svLead.text.toString().isEmpty()) if (pageNumber <= totalPage) {

                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == arrayList.size - 1) {
                        loadMore()
                        isLoading = true
                    }
                }
//                }
            }
        })
    }

    private fun loadMore() {
        arrayList.add(null)
        adapter.notifyItemInserted(arrayList.size - 1)
        Handler(Looper.getMainLooper()).postDelayed({ setData() }, 1000)
    }

    private fun setData(fromArgument: Boolean = false) {
        //api call
        /**
         * val param: HashMap<String, Any> = HashMap()
         * param["user_id"] = getUserData().id
         * param["from_date"] = it.first
         * param["to_date"] = it.second
         * param["page_num"] = pageNumber
         * param["per_page"] = perPage
         * apicall(param)
         * */
    }

    override fun onClick(v: View?) {

    }

    override fun onItemClick(v: View, pos: Int, item: String) {

    }

    override fun onItemLongClick(v: View, pos: Int, item: String) {

    }
}