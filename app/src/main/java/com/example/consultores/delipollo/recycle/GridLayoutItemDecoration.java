package com.example.consultores.delipollo.recycle;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class GridLayoutItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public GridLayoutItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);

        /// Only for GridLayoutManager
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();

        if (parent.getChildLayoutPosition(view) < manager.getSpanCount())
            outRect.top = space;

        if (position % 2 != 0) {
            outRect.right = space;
        }

        outRect.left = space;
        outRect.bottom = space;
    }
}