package com.tomsky.androiddemo.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tomsky.androiddemo.NoDoubleClickListener
import com.tomsky.androiddemo.R
import com.tomsky.androiddemo.fragment.BezierAnimFragment
import com.tomsky.androiddemo.fragment.ShareModelFragment
import com.tomsky.androiddemo.util.UIUtils
import com.tomsky.androiddemo.view.ItemDivider
import com.tomsky.androiddemo.viewmodel.GrammarViewModel
import com.tomsky.androiddemo.viewmodel.ShareViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * fragment 列表
 *
 * Created by wangzhitao on 2022/04/20
 *
 **/
class FragmentRecyclerActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null

    private lateinit var viewModel: ShareViewModel
    private val mainScope = MainScope()

    private var currentFragment: Fragment? = null

    companion object {
        const val TAG = "frag_recycle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val instance = ViewModelProvider.AndroidViewModelFactory
            .getInstance(getApplication())
        viewModel = ViewModelProvider(this).get(ShareViewModel::class.java)
        setContentView(R.layout.activity_recycler_fragment)
        initView()

        viewModel.selectValue.observe(this, Observer {
            Log.i(TAG, "selectValue: $it")
        })
    }

    private fun initView() {
        recyclerView = findViewById(R.id.fragment_recyclerview)

        recyclerView?.run {
            layoutManager = LinearLayoutManager(this@FragmentRecyclerActivity, LinearLayoutManager.VERTICAL, false)
            var dataList: MutableList<FragItem> = mutableListOf()
            dataList.add(FragItem("共享ViewModel") { showFragment(ShareModelFragment(), "frag_share_viewmodel") })
            dataList.add(FragItem("贝塞尔漂浮动画") { showFragment(BezierAnimFragment(), "frag_bezier_anim") })


            val dragAdapter = FragAdapter()
            dragAdapter.setData(dataList)
            adapter = dragAdapter
            addItemDecoration(ItemDivider())

        }

    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (currentFragment != null && fragment !== currentFragment) {
            ft.remove(currentFragment!!)
        }
        if (fragment.isAdded && fm.findFragmentByTag(tag) != null) {
            ft.show(fragment)
        } else {
            ft.add(R.id.fragment_container, fragment, tag)
        }
        currentFragment = fragment
        if (!isFinishing) {
            ft.commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
    }

    override fun finish() {
        super.finish()
        mainScope.cancel()
    }

    override fun onBackPressed() {
        if (currentFragment != null) {
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.remove(currentFragment!!)
            if (!isFinishing) {
                ft.commitAllowingStateLoss()
                fm.executePendingTransactions()
            }
            currentFragment = null
            return
        }

        super.onBackPressed()
    }

    class FragViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var titleView: TextView = itemView.findViewById(R.id.grammar_item_title)
        fun updateTitle(title: String) {
            titleView.text = title
        }

    }

    class FragAdapter: RecyclerView.Adapter<FragViewHolder>() {

        private var grammarList: MutableList<FragItem> = mutableListOf()

        private var viewHeight = UIUtils.dp2px(50)

        fun setData(dataList: MutableList<FragItem>) {
            grammarList.clear()
            grammarList.addAll(dataList)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.grammar_item, parent, false)
            var layoutParams: RecyclerView.LayoutParams? = view.layoutParams as RecyclerView.LayoutParams?
            if (layoutParams == null) {
                layoutParams = StaggeredGridLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, viewHeight)
            } else {
                layoutParams.height = viewHeight
            }
            view.layoutParams = layoutParams
            return FragViewHolder(view)
        }

        override fun getItemCount(): Int {
            return grammarList.size
        }

        override fun onBindViewHolder(holder: FragViewHolder, position: Int) {
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

data class FragItem(val title: String, val method:()->Unit)