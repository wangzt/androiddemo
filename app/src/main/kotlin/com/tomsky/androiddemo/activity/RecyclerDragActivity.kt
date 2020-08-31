package com.tomsky.androiddemo.activity

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.util.LogUtils
import com.tomsky.androiddemo.util.UIUtils
import com.tomsky.androiddemo.view.ItemDivider
import java.util.*

/**
 *
 * Created by wangzhitao on 2020/08/31
 *
 **/
class RecyclerDragActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_drag)
        initView()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.drag_recyclerview)

        recyclerView?.run {
            layoutManager = LinearLayoutManager(this@RecyclerDragActivity, LinearLayoutManager.VERTICAL, false)
            var dataList: MutableList<String> = mutableListOf()
            for (i in 0..5) {
                dataList.add("drag item $i")
            }
            val dragAdapter = DragAdapter()
            dragAdapter.setData(dataList)
            adapter = dragAdapter
            addItemDecoration(ItemDivider())

            val itemTouchHelper = ItemTouchHelper(DragItemHelperCallback(dragAdapter))
            itemTouchHelper.attachToRecyclerView(this)
        }

    }

    class DragViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleView: TextView = itemView.findViewById(R.id.drag_item_title)
        fun updateTitle(title: String) {
            titleView.text = title
        }

        fun modifyBg(highlight: Boolean) {
            itemView.setBackgroundColor(if (highlight) Color.GREEN else Color.WHITE)
        }
    }

    class DragAdapter: RecyclerView.Adapter<DragViewHolder>(), ItemTouchHelperAdapter {

        private var dragList: MutableList<String> = mutableListOf()

        private var viewHeight = UIUtils.dp2px(80)

        fun setData(dataList: MutableList<String>) {
            dragList.clear()
            dragList.addAll(dataList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DragViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.drag_item, parent, false)
            var layoutParams: RecyclerView.LayoutParams? = view.layoutParams as RecyclerView.LayoutParams?
            if (layoutParams == null) {
                layoutParams = StaggeredGridLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, viewHeight)
            } else {
                layoutParams.height = viewHeight
            }
            view.layoutParams = layoutParams
            return DragViewHolder(view)
        }

        override fun getItemCount(): Int {
            return dragList.size
        }

        override fun onBindViewHolder(holder: DragViewHolder, position: Int) {
            if (dragList.size <= position || holder == null) {
                return
            }

            holder.updateTitle(dragList[position])
        }

        override fun onItemMove(fromPosition: Int, toPosition: Int):Boolean {
            if (toPosition == 5) return false

            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(dragList, i, i + 1)
                }
            } else {
                for (i in fromPosition until toPosition) {
                    Collections.swap(dragList, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onItemDismiss(position: Int) {
            dragList.removeAt(position)
            notifyItemRemoved(position)
        }

        override fun onChangeFinished() {
            for (item in dragList) {
                LogUtils.i("wzt-drag", "onChangeFinished, $item")
            }
        }
    }

    interface ItemTouchHelperAdapter {
        fun onItemMove(fromPosition: Int, toPosition: Int):Boolean
        fun onItemDismiss(position: Int)
        fun onChangeFinished()
    }

    class DragItemHelperCallback(var adapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback() {
        companion object {
            const val TAG = "wzt-drag"
        }
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            if (viewHolder.adapterPosition == 5) {
                dragFlags = 0
                LogUtils.i(TAG, "getMovementFlags, position 3 not support drag")
            }
            var swipeFlags = 0
//            var swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            LogUtils.i(TAG, "onSelectedChanged, position: ${viewHolder?.adapterPosition}, actionState: $actionState")
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            LogUtils.i(TAG, "clearView, position: ${viewHolder.adapterPosition}")
            super.clearView(recyclerView, viewHolder)
            adapter.onChangeFinished()
        }
    }
}