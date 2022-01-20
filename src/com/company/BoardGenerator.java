package com.company;

import java.util.LinkedList;

public class BoardGenerator {

    //Field variables

    private final int threshold;
    private Board board;
    boolean alreadyreturned = false;

    //Constructors

    /**
     * @param size size of board / amount of queens
     * @param threshold Up to which row the generator will generate boards
     */
    public BoardGenerator(int size, int threshold) {

        if (threshold >= size) throw new IllegalArgumentException("Threshold must be less than size");
        this.threshold = threshold;
        this.board = new Board(size);

    }

    //Encapsulation methods

    public Board getBoard() {
        return board;
    }

    //General methods


    private void preGenerate(){
        board.addQueen(3,1);
        board.addQueen(1,2);

        if (threshold < 3) return;

        while (true){
            if (board.anyThreats()){

                while (!board.getQueens().getLast().tryMove(1,0)) board.getQueens().removeLast();

            }
            else{
                if (board.getQueens().size() >= threshold) break;
                board.addQueen(1, board.getQueens().getLast().getY() + 1);
            }
        }
    }

    public Board next() {

        LinkedList<Queen> thequeens = board.getQueens();

        if (thequeens.size() == 0) preGenerate();


        while (true){


            if (board.anyThreats() || alreadyreturned){ //If there are any threats or this version of the board in its current state has already returned then we need to attempt to move the highest queen.
                //Without alreadyreturned the method would get stuck returning the same first valid board on every call of this method.

                /*
                In the case of threats existing the algorithm will try to move the last (most recent, highest row) queen one to the right (X +1).
                As long as moving the latest queen doesn't work (when tryMove returns false because the move would exceed the max coordinate for the queen)
                the latest queen will be gotten rid of and tryMove will be attempted for the queen below it until a queen will move to the right.
                This way if there are multiple queens at the max X coordinate then they will all be removed and the first queen with an non-max X coordinate will
                be moved.
                 */

                while (!thequeens.getLast().tryMove(1 ,0)) {

                    //if (board.getQueens().getFirst().getX() >= ((board.getSize() % 2 == 0) ? board.getSize() / 2 : (board.getSize() /2) +1 )) return null;
                    /*If the position of the queen on the lowest row is past mid point we will return null, since any boards generated past that point will just be mirrors of existing starting boards.
                    This will mainly conserve memory since we won't be generating and storing 'redundant' starting boards
                     */

                    if (thequeens.size() < 2) return null;
                    //If the board has only one queen and that cannot be moved (because tryMove returned false) then the there's no more possible solutions and we will return null.

                    board.tryRemoveTop(); //If tryMove passed false (queen at max X coordinate) and above if statement wasn't triggered then we need to try and remove the top queen
                }


                alreadyreturned = false; //Since the above while loop must have changed the board we will set alreadyreturned to false to indicate that this version of the board hasn't been returned.

            } else{

                if (thequeens.size() == threshold) break; //If there are no conflicts AND the amount of queens is equal to the threshold of the generator then we've found a a valid board and can break out
                board.addQueen(1, thequeens.getLast().getY() + 1); //If however there are no conflicts but we haven't placed the required amount of queens, a queen will be added to the next row of the board

            }
        }


        alreadyreturned = true; //Next we will return this version of the board so set alreadyreturned to true

        try {
            return (Board) board.clone(); //return a clone of the board;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("Error, returning null board!");
        return null;
    }

}
