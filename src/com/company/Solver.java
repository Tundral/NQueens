package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 * Solver is the abstract superclass of all solvers.
 * Solvers are callable tasks. When called a solver will solve the
 * board it holds and return itself as a result
 * Multiple solvers are implemented because
 */
public abstract class Solver implements Callable<Solver> {

    //Field variables

    private Board board; //The board to be solved
    private LinkedList<Queen> queens;

    private long solveTime;


    //Constructors

    public Solver(Board board){
        this.board = board;
        this.queens = board.getQueens();
    } //Place board variable in constructor

    //Encapsulation methods

    public Board getBoard() {
        return board;
    }

    protected void setBoard(Board board) {
        this.board = board;
        if (board != null) this.queens = board.getQueens();
    }

    public LinkedList<Queen> getQueens() {
        return queens;
    }

    public long getSolveTime() {
        return solveTime;
    }

    //General methods

    /**
     * Solves the board in the field variable board
     * @return Reference to solved board
     */
    public abstract void solve();

    //Override methods

    @Override
    public Solver call() throws Exception {
        long startTime = System.currentTimeMillis();
        solve();
        solveTime = System.currentTimeMillis() - startTime;
        return this;
    }


}
