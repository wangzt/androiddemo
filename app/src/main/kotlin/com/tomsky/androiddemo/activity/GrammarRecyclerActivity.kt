package com.tomsky.androiddemo.activity

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
import com.tomsky.androiddemo.grammar.*
import com.tomsky.androiddemo.util.UIUtils
import com.tomsky.androiddemo.view.ItemDivider

/**
 * kotlin语法
 *
 * Created by wangzhitao on 2022/04/20
 *
 **/
class GrammarRecyclerActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_grammar)
        initView()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.grammar_recyclerview)

        recyclerView?.run {
            layoutManager = LinearLayoutManager(this@GrammarRecyclerActivity, LinearLayoutManager.VERTICAL, false)
            var dataList: MutableList<GrammarItem> = mutableListOf()
            dataList.add(GrammarItem("协程") { CoroutineGrammar().test() })
            dataList.add(GrammarItem("异步流") { transformFlow() })

            val dragAdapter = GrammarAdapter()
            dragAdapter.setData(dataList)
            adapter = dragAdapter
            addItemDecoration(ItemDivider())

        }

    }

    class GrammarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleView: TextView = itemView.findViewById(R.id.grammar_item_title)
        fun updateTitle(title: String) {
            titleView.text = title
        }

    }

    class GrammarAdapter: RecyclerView.Adapter<GrammarViewHolder>() {

        private var grammarList: MutableList<GrammarItem> = mutableListOf()

        private var viewHeight = UIUtils.dp2px(50)

        fun setData(dataList: MutableList<GrammarItem>) {
            grammarList.clear()
            grammarList.addAll(dataList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrammarViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.grammar_item, parent, false)
            var layoutParams: RecyclerView.LayoutParams? = view.layoutParams as RecyclerView.LayoutParams?
            if (layoutParams == null) {
                layoutParams = StaggeredGridLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, viewHeight)
            } else {
                layoutParams.height = viewHeight
            }
            view.layoutParams = layoutParams
            return GrammarViewHolder(view)
        }

        override fun getItemCount(): Int {
            return grammarList.size
        }

        override fun onBindViewHolder(holder: GrammarViewHolder, position: Int) {
            if (grammarList.size <= position || holder == null) {
                return
            }

            val grammarItem = grammarList[position]
            holder.updateTitle(grammarItem.title)
            holder.itemView.setOnClickListener(object :NoDoubleClickListener() {
                override fun onNoDoubleClick(v: View?) {
                    grammarItem.method.invoke()
                }
            })
        }

    }

}

data class GrammarItem(val title: String, val method:()->Unit)