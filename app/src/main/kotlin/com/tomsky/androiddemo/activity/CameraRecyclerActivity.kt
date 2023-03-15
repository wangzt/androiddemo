package com.tomsky.androiddemo.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tomsky.androiddemo.NoDoubleClickListener
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.util.UIUtils
import com.tomsky.androiddemo.view.ItemDivider

/**
 * Camera语法
 *
 * Created by wangzhitao on 2022/04/20
 *
 **/
class CameraRecyclerActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_grammar)
        initView()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.grammar_recyclerview)

        recyclerView?.run {
            layoutManager = LinearLayoutManager(this@CameraRecyclerActivity, LinearLayoutManager.VERTICAL, false)
            var dataList: MutableList<CameraItem> = mutableListOf()
            dataList.add(CameraItem("Camera1", "com.tomsky.androiddemo.activity.CameraActivity"))
            dataList.add(CameraItem("Camera2", "com.tomsky.androiddemo.activity.Camera2Activity"))
            dataList.add(CameraItem("CameraX", "com.tomsky.androiddemo.activity.CameraXActivity"))

            val dragAdapter = CameraAdapter()
            dragAdapter.setData(dataList)
            adapter = dragAdapter
            addItemDecoration(ItemDivider())

        }

    }


    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleView: TextView = itemView.findViewById(R.id.grammar_item_title)
        fun updateTitle(title: String) {
            titleView.text = title
        }

    }

    class CameraAdapter: RecyclerView.Adapter<CameraViewHolder>() {

        private var cameraList: MutableList<CameraItem> = mutableListOf()

        private var viewHeight = UIUtils.dp2px(50)

        fun setData(dataList: MutableList<CameraItem>) {
            cameraList.clear()
            cameraList.addAll(dataList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.grammar_item, parent, false)
            var layoutParams: RecyclerView.LayoutParams? = view.layoutParams as RecyclerView.LayoutParams?
            if (layoutParams == null) {
                layoutParams = StaggeredGridLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, viewHeight)
            } else {
                layoutParams.height = viewHeight
            }
            view.layoutParams = layoutParams
            return CameraViewHolder(view)
        }

        override fun getItemCount(): Int {
            return cameraList.size
        }

        override fun onBindViewHolder(holder: CameraViewHolder, position: Int) {
            if (cameraList.size <= position || holder == null) {
                return
            }

            val item = cameraList[position]
            holder.updateTitle(item.title)
            holder.itemView.setOnClickListener(object :NoDoubleClickListener() {
                override fun onNoDoubleClick(v: View?) {
                    if (item.hasSub) {
                        v?.context?.let {ctx ->
                            val intent = Intent()
                            intent.setClassName(ctx, item.name)
                            ctx.startActivity(intent)
                        }
                    }
                }
            })
        }

    }

}

data class CameraItem(val title: String, val name: String, val hasSub: Boolean = true)