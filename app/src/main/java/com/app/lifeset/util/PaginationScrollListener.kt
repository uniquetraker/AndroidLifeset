package com.app.lifeset.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private val layoutManager: LinearLayoutManager

    init {
        this.layoutManager = layoutManager
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.getChildCount()
        val totalItemCount: Int = layoutManager.getItemCount()
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}