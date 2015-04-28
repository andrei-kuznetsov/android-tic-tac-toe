package ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel;

/**
 * Created by qnf863 on 16.04.2015.
 */
public interface AICallback {
    public void onAiCompleted(int cell);
    public void onAiGiveUp();
}
