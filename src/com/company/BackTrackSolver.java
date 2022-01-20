package com.company;

import java.util.LinkedList;

public class BackTrackSolver extends Solver {

    //Field variables

    //Constructors

    public BackTrackSolver(Board board){

        super(board);
        if (!(board.getQueens().size() > 0)) throw new IllegalArgumentException("BackTrackSolver requires non-empty board");
    }




    //Encapsulation methods


    //General methods


    /**
     * Uses Djikstra's backtracking algorithm to solve N queens problem on the given board.
     */
    public void backTrackSolver(){

        while (true){

            if (getBoard().anyThreats()){

                /*
                In the case of threats existing the algorithm will try to move the last (most recent, highest row) queen one to the right (X +1).
                As long as moving the latest queen doesn't work (when tryMove returns false because the move would exceed the max coordinate for the queen)
                the latest queen will be gotten rid of and the same will be attempted for the queen below until a queen will move to the right.
                This way if there are multiple queens at the max X coordinate then they will all be removed and the first queen with an non-max X coordinate will
                be moved.
                */
                while (!getQueens().getLast().tryMove(1 ,0)) { getBoard().tryRemoveTop(); }

            } else{

                if (getQueens().size() == getBoard().getSize()) break; //If there are no conflicts AND the amount of queens is equal to the size of the board then we've found the solution and can break out
                getBoard().addQueen(1, getQueens().getLast().getY() + 1); //If however there are no conflicts but we haven't placed the required amount of queens then a queen will be added to the next row of the board

            }
        }
        //Breaking out of loop means we've found the right solution and the method will end
    }

    //Override methods

    @Override
    public void solve() {
        backTrackSolver();
    }

}
