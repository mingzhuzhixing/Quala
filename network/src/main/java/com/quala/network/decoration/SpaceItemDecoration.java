package com.quala.network.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ClassName: SplaceItemDecoration
 * Description: 分割线
 *
 * @author jiaxiaochen
 * @date 2023/12/20 12:19
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final int[] ATTRS = new int[]{16843284};
    protected Drawable mDivider;
    protected int mOrientation;
    private final Rect mBounds = new Rect();
    /**
     * 是否需要显示最有的一个空格线 true显示 false不显示
     */
    protected boolean mIsShowRightLine = false;

    /**
     * 构造函数
     * @param context 上下文对象
     * @param orientation 方向：横向、竖向
     */
    public SpaceItemDecoration(Context context, int orientation) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
        if (this.mDivider == null) {
            Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }

        a.recycle();
        this.setOrientation(orientation);
    }

    /**
     * 构造函数
     * @param context 上下文对象
     * @param orientation 方向：横向、竖向
     * @param isShowRightLine 是否显示最有一个item分割线
     */
    public SpaceItemDecoration(Context context, int orientation, boolean isShowRightLine) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
        this.mIsShowRightLine = isShowRightLine;
        if (this.mDivider == null) {
            Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
        this.setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        } else {
            this.mOrientation = orientation;
        }
    }

    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        } else {
            this.mDivider = drawable;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null && this.mDivider != null) {
            if (this.mOrientation == 1) {
                this.drawVertical(c, parent);
            } else {
                this.drawHorizontal(c, parent);
            }

        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        int childCount = parent.getChildCount();

        //当最后一个子项的时候去除下划线
        for (int i = 0; i < getChildCount(childCount); ++i) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, this.mBounds);
            int bottom = this.mBounds.bottom + Math.round(child.getTranslationY());
            int top = bottom - this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(canvas);
        }

        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        int childCount = parent.getChildCount();

        for (int i = 0; i < getChildCount(childCount); ++i) {
            View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
            int right = this.mBounds.right + Math.round(child.getTranslationX());
            int left = right - this.mDivider.getIntrinsicWidth();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(canvas);
        }

        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (this.mDivider == null) {
            outRect.set(0, 0, 0, 0);
        } else {
            if (this.mOrientation == VERTICAL) {
                //纵向RecyclerView
                int lastPosition = state.getItemCount() - 1;
                int position = parent.getChildAdapterPosition(view);
                if (mIsShowRightLine || position < lastPosition) {
                    outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            } else {
                //横向RecyclerView
                if (mIsShowRightLine) {
                    outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
                } else {
                    int childAdapterPosition = parent.getChildAdapterPosition(view);
                    int lastCount = parent.getAdapter().getItemCount() - 1;
                    //如果不是最后一条 正常赋值 如果是最后一条 赋值为0
                    if (childAdapterPosition != lastCount) {
                        outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
                    } else {
                        outRect.set(0, 0, 0, 0);
                    }
                }
            }
        }
    }

    private int getChildCount(int childCount) {
        return mIsShowRightLine ? childCount : childCount - 1;
    }
}