package com.company;

import java.util.LinkedList;

public class PartialBackTrackSolver extends Solver {


    //Field variables

    private final int lowestrow;

    //Constructors

    public PartialBackTrackSolver(Board board) {
        super(board);
        if (!(board.getQueens().size() > 0)) throw new IllegalArgumentException(this.getClass().getSimpleName() + " requires non-empty board");
        this.lowestrow = board.getQueens().size();
    }


    //Encapsulation methods



    //General methods

    /**
     * Uses Djikstra's backtracking algorithm to find a valid board from the starting configuration
     * of the field variable board. If the given board has queens (1,1) and (3,2) solveAbove will all possibilities
     * of the queens above row 3 without moving the two initial quuens. If a solution is found it is returned, if not then
     * null is returned
     * @return Reference to the solved board (or null if no valid board is found)
     */
    public Board solveAbove(){

        Board theboard = super.getBoard();
        LinkedList<Queen> thequeens = super.getBoard().getQueens();

        while (true){

            if (Thread.interrupted()) {
                return null;
            }

            if (theboard.anyThreats()){

                while (!thequeens.getLast().tryMove(1 ,0)) {
                    if (!theboard.tryRemoveTop()) return null;
                }

            } else{

                if (thequeens.size() == theboard.getSize()) break; //If there are no conflicts AND the amount of queens is equal to the size of the board then we've found the solution and can break out
                theboard.addQueen(1, thequeens.getLast().getY() + 1); //If however there are no conflicts but we haven't placed the required amount of queens then a queen will be added to the next row of the board

            }
        }
        return theboard; //A.K.A the field variable board.
    }


    //Override methods

    @Override
    public void solve() {
        if (solveAbove() == null) super.setBoard(null);
    }

}
