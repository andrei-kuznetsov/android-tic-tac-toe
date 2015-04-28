package ru.spbstu.icc.kspt.kuznetsov.tictactoe;

import android.os.AsyncTask;

import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.AICallback;
import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.GameModel;

/**
 * Created by qnf863 on 15.04.2015.
 */
public class AIAsyncTask extends AsyncTask<GameModel, Void, Integer> {
    private AICallback cb;
    private final int aiDepth = 10;

    public AIAsyncTask(AICallback aiCallback) {
        cb = aiCallback;
    }

    @Override
    protected Integer doInBackground(GameModel... models) {
        int maxRatingCell = 0;
        try {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            final GameModel model = models[0];
            int[] rating = new int[0];
            rating = AI.go2AB(model, aiDepth);
            int maxRating = rating[0];
            for (int i = 0; i < rating.length; i++) {
                if (rating[i] > maxRating) {
                    maxRating = rating[i];
                    maxRatingCell = i;
                }
            }

            if (maxRating == 0) {
                // go random
                int cell = (int) Math.round(Math.random() * 10000) % rating.length;
                int iteration = 0;
                while (!model.canMove(cell) || (rating[cell] < 0 && iteration < 2)) {
                    cell++;
                    if (cell >= rating.length) {
                        cell = 0;
                        iteration++;
                    }
                }
                if (iteration > 1) cb.onAiGiveUp();
                maxRatingCell = cell;
            }
        } catch (GameModel.GameModelException e) {
            e.printStackTrace();
        }
        return maxRatingCell;
    }

    @Override
    protected void onPostExecute(Integer cell) {
        cb.onAiCompleted(cell);
    }
}
