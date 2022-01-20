package com.company;

import java.util.LinkedList;

public class RecursiveBackTrackSolver extends Solver {

    //Field variables

    private Board board;

    //Constructors

    public RecursiveBackTrackSolver(int size){
        super(new Board(size));
    }

    //Encapsulation methods


    //General methods

    /**
     * Uses recursion magic to solve this board. Does the exact same thing as backTrackSolver() just in a different, more stack overflowy way.
     * @param x Has to be 0 when initially calling the method
     * @param y Has to be 0 when initially calling the method
     * @return Recursion magic!!
     */
    private boolean recursiveBackTrackSolver(int x, int y) {

        Board theboard = super.getBoard();
        LinkedList<Queen> thequeens = super.getBoard().getQueens();


        if (y < 1) y = 1;

        int newx, newy;

        theboard.addQueen(x, y);

        if (thequeens.size() == theboard.getSize() && !theboard.anyThreats()) return true;


        //This section is just for adding spaces when the method gets one row deeper. does NOTHING to the board
        String spaces = "";
        for (int i = -1; i < y; i++) {
            spaces = spaces + "  ";
        }
        //



        if (theboard.anyThreats()) {
            if (x >= theboard.getSize()) {
                thequeens.removeLast();
                newx = (thequeens.getLast().getX() >= theboard.getSize()) ? theboard.getSize() : thequeens.getLast().getX() + 1;
                newy = thequeens.getLast().getY() - 1;

            }
            else {
                newx = thequeens.getLast().getX() + 1;
                newy = y;
            }
            thequeens.removeLast();
        }else{
            newx = 1;
            newy = y+1;
        }
        return recursiveBackTrackSolver(newx, newy);
    }


    //Override methods
    @Override
    public void solve() {
        recursiveBackTrackSolver(1, 1);
    }
}
