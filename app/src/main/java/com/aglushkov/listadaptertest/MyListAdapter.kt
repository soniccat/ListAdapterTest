package com.aglushkov.listadaptertest

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Item (
    val id: Int,
    val data: String
)

class MyListAdapter: ListAdapter<Item, ViewHolder>(CALLBACK) {
    private var dataList = listOf<Item>()

    override fun submitList(list: List<Item>?) {
        submitList(list, null)
    }

//    suspend fun submitListSuspended(list: List<Item>?, commitCallback: Runnable?) {
//        dataList = list.orEmpty().toList()
//        delay(1000)
//        super.submitList(dataList, commitCallback)
//        Log.d("ListAdapter", "submitted " + dataList.size)
//    }

//    suspend fun submitListSuspended(list: List<Item>?, scope: CoroutineScope, commitCallback: Runnable?) {
//        super.submitList(dataList) {
//            scope.launch {
//                delay(1000)
//                dataList = list.orEmpty().toList()
//                commitCallback?.run()
//            }
//        }
//        Log.d("ListAdapter", "submitted " + dataList.size)
//    }

    suspend fun submitListSuspended(list: List<Item>?, scope: CoroutineScope, commitCallback: Runnable?) {
        super.submitList(list, commitCallback)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Item>,
        currentList: MutableList<Item>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        dataList = currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            ).apply {
                setPadding(30, 30, 30, 30)
            }
        }
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as TextView).text = if (position < dataList.size) {
            dataList[position].data
        } else {
            "err: $position >= ${dataList.size}"
        }
    }

    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.data == newItem.data
            }
        }
    }
}

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
}