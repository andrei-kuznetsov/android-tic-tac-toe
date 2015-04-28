package ru.spbstu.icc.kspt.kuznetsov.tictactoe.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class UniformGridView extends GridView{

    public UniformGridView(Context context) {
        super(context);
    }

    public UniformGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UniformGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalMeasureSpec = UniformView.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(finalMeasureSpec, finalMeasureSpec);
    }
}
