package ru.spbstu.icc.kspt.kuznetsov.tictactoe.views;

import android.view.View;

public class UniformView {
    static public int onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int size;
        if(widthMode == View.MeasureSpec.EXACTLY && widthSize > 0){
            size = widthSize;
        }
        else if(heightMode == View.MeasureSpec.EXACTLY && heightSize > 0){
            size = heightSize;
        }
        else{
            size = widthSize < heightSize ? widthSize : heightSize;
        }

        int finalMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
        return finalMeasureSpec;
    }
}
