package ru.spbstu.icc.kspt.kuznetsov.tictactoe;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.GameModelFragment;

public class GameActivity extends Activity implements GameModelFragment.OnFragmentInteractionListener{
    private static final String FTAG_MODELFRAGMENT = "tag.fragment.gamemodel";
    private GameFieldAdapter adapter;
    private AIAsyncTask ai = null;

    private GameModelFragment modelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_field);

        FragmentManager fm = getFragmentManager();
        modelFragment = (GameModelFragment) fm.findFragmentByTag(FTAG_MODELFRAGMENT);
        if (modelFragment == null){
            modelFragment = GameModelFragment.newInstance((MainActivity.LAUNCH_MODES) getIntent().getSerializableExtra(MainActivity.KEY_LAUNCH_MODE));
            fm.beginTransaction().add(modelFragment, FTAG_MODELFRAGMENT).commit();
        }

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setNumColumns(modelFragment.getModel().getSz());
        adapter = new GameFieldAdapter(this, modelFragment.getModel());
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (modelFragment.getCurrentPlayer()) {
                    case GAME_OVER:
                        Toast.makeText(GameActivity.this, R.string.dialog_game_over, Toast.LENGTH_SHORT).show();
                        break;
                    case COMPUTER:
                        Toast.makeText(GameActivity.this, R.string.wait_your_turn, Toast.LENGTH_SHORT).show();
                        break;
                    case HUMAN:
                        modelFragment.makeMoveHuman(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @Override
    public void onGameOver() {
        new GameOverDialogFragment().show(getFragmentManager(), "game_over");
    }

    @Override
    public void onComputerMoved() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onComputerGiveUp() {
        Toast.makeText(this, R.string.give_up, Toast.LENGTH_SHORT).show();
    }
}
