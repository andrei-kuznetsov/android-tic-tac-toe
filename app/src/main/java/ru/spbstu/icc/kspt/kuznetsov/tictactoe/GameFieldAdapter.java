package ru.spbstu.icc.kspt.kuznetsov.tictactoe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.GameModel;
import ru.spbstu.icc.kspt.kuznetsov.tictactoe.views.UniformImageView;

/**
 * Created by qnf863 on 11.03.2015.
 */
public class GameFieldAdapter extends BaseAdapter {
    private final GameModel model;
    private final Context mContext;

    public GameFieldAdapter(Context context, GameModel model){
        this.model = model;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return model.getSz() * model.getSz();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int x = i % model.getSz();
        int y = i / model.getSz();

        GameModel.VALUES v = GameModel.VALUES.EMPTY;
        try {
            v = model.getSignAt(x, y);
        } catch (GameModel.GameModelException e){

        }

        ImageView imageView;

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new UniformImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        switch (v){
            case X:
                imageView.setImageResource(R.drawable.android_icon);
                break;
            case O:
                imageView.setImageResource(R.drawable.ievil_android_icon);
                break;
            default:
                imageView.setImageResource(R.drawable.empty);
                break;
        }

//        imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
        return imageView;
    }
}
