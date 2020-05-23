package com.example.camprecapp;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpace extends RecyclerView.ItemDecoration {
    int space;
    public VerticalSpace(int space) {
        this.space = space;
    }
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
            outRect.bottom = space;
        }
        outRect.right = space;
        outRect.left = space;
        outRect.bottom = space;
    }
}
