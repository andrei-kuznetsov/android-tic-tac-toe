package ru.spbstu.icc.kspt.kuznetsov.tictactoe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class UniformImageView extends ImageView{
    public UniformImageView(Context context) {
        super(context);
    }

    public UniformImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UniformImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalMeasureSpec = UniformView.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(finalMeasureSpec, finalMeasureSpec);
    }
}
