package com.jinghan.app.mvp.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jinghan.core.R
import com.jinghan.core.databinding.FgGuideViewBinding

/**
 * @author liuzeren
 * @time 2017/11/9    下午6:41
 * @mail lzr319@163.com
 */
class GuideAdapter(context: Context, resDraws: Array<Int>,var itemClick:View.OnClickListener?=null) : PagerAdapter() {
    private val pageViews = ArrayList<View>()

    init {
        pageViews.clear()

        for (index in resDraws.indices) {

            val resDraw = resDraws[index]

            val binding = DataBindingUtil.inflate<FgGuideViewBinding>(LayoutInflater.from(context), R.layout.fg_guide_view, null, false)
            binding.bg = resDraw
            binding.visible = index == resDraws.size - 1

            pageViews.add(binding.root)
        }
    }

    override fun getCount(): Int {
        return pageViews?.size ?: 0
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun instantiateItem(viewGroup: ViewGroup, position: Int): Any {

        val view = pageViews[position]
        viewGroup.addView(view)

        val viewEnter = view.findViewById<View>(R.id.ivEnter)
        if (viewEnter.visibility == View.VISIBLE) {
            viewEnter.setOnClickListener { v ->
                itemClick?.onClick(v)
            }
        }
        return pageViews[position]
    }

    override fun destroyItem(v: ViewGroup, position: Int, arg2: Any) {
        v.removeView(pageViews[position])
    }
}