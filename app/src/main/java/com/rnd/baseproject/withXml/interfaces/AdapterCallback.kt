package com.rnd.baseproject.withXml.interfaces

import android.view.View

interface AdapterCallback<in T> {
    fun onItemClick(v: View, pos: Int, item: T)

    fun onItemLongClick(v: View, pos: Int, item: T)

}