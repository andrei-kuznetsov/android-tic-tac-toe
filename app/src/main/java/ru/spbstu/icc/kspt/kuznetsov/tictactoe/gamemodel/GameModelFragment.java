package ru.spbstu.icc.kspt.kuznetsov.tictactoe.gamemodel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.spbstu.icc.kspt.kuznetsov.tictactoe.AIAsyncTask;

import static ru.spbstu.icc.kspt.kuznetsov.tictactoe.MainActivity.KEY_LAUNCH_MODE;
import static ru.spbstu.icc.kspt.kuznetsov.tictactoe.MainActivity.LAUNCH_MODES;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameModelFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class GameModelFragment extends Fragment implements AICallback {
//    private static final String KEY_CURRENT_PLAYER = "current.player";
//    private static final String KEY_CURRENT_FIELD = "current.field";

    public static GameModelFragment newInstance(LAUNCH_MODES mode){
        GameModelFragment f = new GameModelFragment();

        Bundle args = new Bundle();
        args.putSerializable(KEY_LAUNCH_MODE, mode);
        f.setArguments(args);

        return f;
    }

    private OnFragmentInteractionListener mListener;

    private static final int fieldSize = 3;
    private GameModel model = new GameModel(fieldSize);

    private LAUNCH_MODES mode;

    public void gameOver() {
        currentPlayer = CURRENT_PLAYER.GAME_OVER;
        if (mListener != null){
            mListener.onGameOver();
        }
    }

    public void nextIsHuman() {
        if (currentPlayer != CURRENT_PLAYER.GAME_OVER) {
            currentPlayer = CURRENT_PLAYER.HUMAN;
        }
    }

    public void nextIsComputer() {
        if (currentPlayer != CURRENT_PLAYER.GAME_OVER) {
            currentPlayer = CURRENT_PLAYER.COMPUTER;
            new AIAsyncTask((AICallback) this).execute(model);
        }
    }

    private void makeMoveAI(int cell){
        if (currentPlayer == CURRENT_PLAYER.COMPUTER) {
            makeMove(cell);
        } /* else Hmmm?! */
    }

    public void makeMoveHuman(int cell){
        if (currentPlayer == CURRENT_PLAYER.HUMAN) {
            makeMove(cell);
        } /* else Hmmm?! */
    }

    private void makeMove(int cell){
        try {
            model.makeMove(cell);
            // shall throw exception if the cell is busy

            if (model.getWinner() != null){
                // game over
                gameOver();
            } else {
                if (mode == LAUNCH_MODES.PLAYER_VS_PLAYER)
                    nextIsHuman();
                else {
                    if (currentPlayer == CURRENT_PLAYER.COMPUTER)
                        nextIsHuman();
                    else if (currentPlayer == CURRENT_PLAYER.HUMAN)
                        nextIsComputer();
                }
            }
        } catch (GameModel.GameModelException e) {

        }
    }

    @Override
    public void onAiCompleted(int cell) {
        makeMoveAI(cell);

        if (mListener != null){
            mListener.onComputerMoved();
        }
    }

    @Override
    public void onAiGiveUp() {
        if (mListener != null) {
            mListener.onComputerGiveUp();
        }
    }

    public static enum CURRENT_PLAYER {HUMAN, COMPUTER, GAME_OVER}

    private CURRENT_PLAYER currentPlayer = CURRENT_PLAYER.HUMAN;

    public GameModelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

//        if (savedInstanceState != null) {
//            model = (GameModel) savedInstanceState.getSerializable(KEY_CURRENT_FIELD);
//            mode = (MainActivity.LAUNCH_MODES) savedInstanceState.getSerializable(MainActivity.KEY_LAUNCH_MODE);
//            currentPlayer = (STATE) savedInstanceState.getSerializable(KEY_CURRENT_PLAYER);
//        } else {
            mode = (LAUNCH_MODES) getArguments().getSerializable(KEY_LAUNCH_MODE);
            if (mode == null) mode = LAUNCH_MODES.PLAYER_VS_COMPUTER;
            if (mode == LAUNCH_MODES.COMPUTER_VS_PLAYER) {
                nextIsComputer();
            } else {
                nextIsHuman();
            }
//        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private boolean testSomeoneWin() {
        GameModel.VALUES winner = model.getWinner();
        if (winner != null){

            return true;
        }

        return false;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putSerializable(KEY_CURRENT_FIELD, model);
//        outState.putSerializable(KEY_CURRENT_PLAYER, currentPlayer);
//        outState.putSerializable(MainActivity.KEY_LAUNCH_MODE, mode);
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public GameModel getModel() {
        return model;
    }

    public CURRENT_PLAYER getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onGameOver();
        public void onComputerMoved();
        public void onComputerGiveUp();
    }

}
