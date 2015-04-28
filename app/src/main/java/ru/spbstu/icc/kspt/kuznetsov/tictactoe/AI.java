package ru.spbstu.icc.kspt.kuznetsov.tictactoe;

import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.GameModel;
import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.GameModel.GameModelException;
import ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel.GameModel.VALUES;

public class AI {

    static class Rating {
        int winX;
        int winO;

        public Rating(int winX, int winO) {
            this.winX = winX;
            this.winO = winO;
        }

        public Rating() {
            this(0, 0);
        }

        public void add(Rating other){
            this.winX += other.winX;
            this.winO += other.winO;
        }

        public int get(VALUES what) {
            if (what == VALUES.X){
                return winX;
            } else if (what == VALUES.O){
                return winO;
            } else {
                return 0;
            }
        }
    }

    private static final Rating WIN_X = new Rating(1, 0);
    private static final Rating WIN_O = new Rating(0, 1);
    private static final Rating DEAD_END = new Rating(0, 0);
    private static final Rating R_UNK = new Rating(0, 0);

    public static Rating[] go(GameModel gm, int depth) throws GameModelException{
        int sz = gm.getSz();
        Rating rating[] = new Rating[sz * sz];
        for (int i = 0; i < rating.length; i++){
            rating[i] = new Rating();
        }

        step(gm.dup(), depth, rating);

        return rating;
    }

    public static int[] go3(GameModel gm, int depth) throws GameModelException{
        final int sz = gm.getSz();
        final int max = sz * sz;
        int rating[] = new int[max];

        alphaBeta(gm, 5, -5, depth, gm.getCurrentPlayer(), rating);

        return rating;
    }

    public static int[] go2(GameModel gm, int depth) throws GameModelException{
        final int sz = gm.getSz();
        final int max = sz * sz;
        int rating[] = new int[max];

        max(gm, depth, gm.getCurrentPlayer(), rating);

        return rating;
    }

    public static int[] go2AB(GameModel gm, int depth) throws GameModelException{
        final int sz = gm.getSz();
        final int max = sz * sz;
        int rating[] = new int[max];

        maxAB(gm, depth, -5, 5, gm.getCurrentPlayer(), rating);

        return rating;
    }

    public static int[] go4(GameModel gm, int depth) throws GameModelException{
        final int sz = gm.getSz();
        final int max = sz * sz;
        int rating[] = new int[max];

        maximin(gm, depth, gm.getCurrentPlayer(), rating);

        return rating;
    }

    // http://www.dokwork.ru/2012/11/tictactoe.html
    private static int maximin(GameModel position, int depth, VALUES player, int rating[]) throws GameModelException {
        final VALUES winner = position.getWinner();
        if (winner != null){
            return heuristic(winner, player);
        }

        Integer score = null;
        final int length = position.getSz() * position.getSz();
        for (int i = 0; i < length; i++){
            if (position.canMove(i)){
                GameModel c = position.dup();
                c.makeMove(i);

                int s = -maximin(c, depth - 1, player.invert(), null);
                if (rating != null) rating[i] = s;
                if (score == null || s > score) score = s;
            }
        }

        return score;
    }

    private static int max(GameModel position, int depth, VALUES player, int rating[]) throws GameModelException {
        final VALUES winner = position.getWinner();
        if (winner != null){
            return heuristic(winner, player);
        }

        int score = Integer.MIN_VALUE;
        final int length = position.getSz() * position.getSz();
        for (int i = 0; i < length; i++){
            if (position.canMove(i)){
                GameModel c = position.dup();
                c.makeMove(i);

                int s = min(c, depth - 1, player, null);
                if (rating != null) rating[i] = s;
                if (s > score) score = s;
            }
        }

        return score;
    }

    private static int min(GameModel position, int depth, VALUES player, int rating[]) throws GameModelException {
        final VALUES winner = position.getWinner();
        if (winner != null){
            return heuristic(winner, player);
        }

        int score = Integer.MAX_VALUE;
        final int length = position.getSz() * position.getSz();
        for (int i = 0; i < length; i++){
            if (position.canMove(i)){
                GameModel c = position.dup();
                c.makeMove(i);

                int s = max(c, depth - 1, player, null);
                if (rating != null) rating[i] = s;
                if (s < score) score = s;
            }
        }

        return score;
    }

    private static int maxAB(GameModel position, int depth, int alpha, int beta, VALUES player, int rating[]) throws GameModelException {
        if (depth < 0) {
            return 1;
        }

        final VALUES winner = position.getWinner();
        if (winner != null){
            return heuristic(winner, player);
        }

        int score = alpha;
        final int length = position.getSz() * position.getSz();
        for (int i = 0; i < length; i++){
            if (position.canMove(i)){
                GameModel c = position.dup();
                c.makeMove(i);

                int s = minAB(c, depth - 1, score, beta, player, null);
                if (rating != null) rating[i] = s;
                if (s > score) score = s;
                if (score >= beta) return score;
            }
        }

        return score;
    }

    private static int minAB(GameModel position, int depth, int alpha, int beta, VALUES player, int rating[]) throws GameModelException {
        final VALUES winner = position.getWinner();
        if (winner != null){
            return heuristic(winner, player);
        }

        int score = beta;
        final int length = position.getSz() * position.getSz();
        for (int i = 0; i < length; i++){
            if (position.canMove(i)){
                GameModel c = position.dup();
                c.makeMove(i);

                int s = maxAB(c, depth - 1, alpha, score, player, null);
                if (rating != null) rating[i] = s;
                if (s < score) score = s;
                if (score <= alpha) return score;
            }
        }

        return score;
    }


    // http://www.dokwork.ru/2012/11/tictactoe.html
    private static int alphaBeta(GameModel position, int alpha, int beta, int depth, VALUES player, int rating[]) throws GameModelException {
        final VALUES winner = position.getWinner();
        if (winner != null){
            return heuristic(winner, player);
        }

        int score = beta;
        final int max = position.getSz() * position.getSz();
        for (int i = 0; i < max; i++){
            if (position.canMove(i)){
                GameModel c = position.dup();
                c.makeMove(i);

                int s = -alphaBeta(c, -score, -alpha, depth - 1, player.invert(), null);
                if (rating != null) rating[i] = s;
                if (s > score) score = s;
                if (score <= alpha) return score;
            }
        }

        return score;
    }

    // none = 0, draw = 2, win = 5, loose = -5
    private static int heuristic(VALUES winner, VALUES player) {
        if (winner == player) return 5;
        if (winner == VALUES.EMPTY) return 2;
        if (winner == null) return 0;
        return -5;
    }

    private static Rating step(GameModel gm, int depth, Rating[] rating) throws GameModelException {
        final VALUES winner = gm.getWinner();
        if (winner == null){
            if (depth > 0){
                final int max = gm.getSz() * gm.getSz();
                Rating overallRaiting = new Rating();

                for (int i = 0; i < max; i++){
                    if (gm.canMove(i)){
                        GameModel c = gm.dup();
                        c.makeMove(i);
                        Rating stepRating = step(c, depth - 1, null);
                        overallRaiting.add(stepRating);
                        if (rating != null){
                            rating[i].add(stepRating);
                        }
                    }
                }

                return overallRaiting;
            } else {
                return R_UNK; // unknown
            }
        } else {
            if (winner == VALUES.X) {
                return WIN_X;
            } else if (winner == VALUES.O) {
                return WIN_O;
            } else /* dead end */ {
                return DEAD_END;
            }
        }
    }
}
