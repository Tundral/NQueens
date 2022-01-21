package com.company;

import java.util.LinkedList;

public class Board implements Cloneable {

    //Field variables
    protected final int size; //The size of the board which will also be the amount of queens required for the board to be solved

    protected final LinkedList<Queen> queens; //List for the queens on the board
    //EXTRA comments/thoughts on data structure selection at *1 at the bottom of this file


    //Constructors
    public Board(int size){

        this.size = size;
        this.queens = new LinkedList<>();
    }

    public Board(int size, LinkedList<Queen> queens){

        this.size = size;
        this.queens = queens;
    }

    //Encapsulation methods

    public int getSize() {
        return size;
    }

    public LinkedList<Queen> getQueens() {
        return queens;
    }


    //General methods

    /**
     * Checks if any queen on this board threatens another return appropriate boolean value
     * @return True = threatenings present, False = no threatenings present
     */
    public boolean anyThreats(){

        if (this.queens.size() < 2) return false; //If there's one queen or less there cannot be threats

        for (Queen queen : this.queens) { //Check all queens on the board...
            for (Queen otherQueen: this.queens) { //...against all other queens on the board
                if (queen.threatens(otherQueen)) return true; //If any threaten each other return true
            }
        }
        return false; //Loop finished so no queens threaten any other queens so return false
    }

    public void addQueen(int x, int y){
        if (
                x < 1 ||
                x > this.getSize()||
                y < 1 ||
                y > this.getSize()
        ) throw new IllegalArgumentException("Attempting to add Queen out of bounds: " + x + " " + y);

        this.queens.add(new Queen(x,y, size));
    }

    public boolean tryRemoveTop(){

        if (this.queens.size() < 2) return false;
        this.queens.removeLast();
        return true;
    }

    public Board horizontalMirror(){
        Board retVal = new Board(this.size);

        for (Queen q :
                this.queens) {
            retVal.addQueen(this.size - q.getX() + 1 , q.getY());
        }

        return retVal;
    }

    public Board verticalMirror(){
        Board retVal = new Board(this.size);

        for (Queen q :
                this.queens) {
            retVal.addQueen(q.getX(), this.size - (Math.abs(q.getY() - 1)));
        }

        return retVal;
    }

    public Board rotate90(){
        Board retVal = new Board(this.size);

        for (Queen q :
                this.queens) {
            retVal.addQueen((q.getY()),size - q.getX() + 1);
        }

        return retVal;
    }

    public boolean anyRotationMirror(Board that){

        if (this.equals(that)) return false;
        Board rotation = this;
        LinkedList<Board> rotations = new LinkedList<>();
        LinkedList<Board> rotationsAndMirrors = new LinkedList<>();

        for (int i = 0; i < 3; i++) {

            rotation = rotation.rotate90();
            rotations.add(rotation);
        }
        for (Board b :
                rotations) {
            rotationsAndMirrors.add(b.horizontalMirror());
            rotationsAndMirrors.add(b.verticalMirror());
            rotationsAndMirrors.add(b.horizontalMirror().horizontalMirror());
        }

        for (Board rotationmirror:
             rotationsAndMirrors) {
            if (rotationmirror.equals(that)) return true;
        }

        return false;
    }

    //Override methods

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Board other = (Board) obj;

        if(this.hashCode() == other.hashCode()) return true;

        if (this.getQueens().size() != other.getQueens().size()) return false;

        for (int i = 0; i < other.getQueens().size(); i++) {
            if (!(this.getQueens().get(i).equals(other.getQueens().get(i)))) return false;
        }

        return true;
    }

    @Override
    public Board clone() throws CloneNotSupportedException {
        LinkedList<Queen> clonedQueens = new LinkedList<>();
        queens.forEach(q -> clonedQueens.add(new Queen(q.getX(),q.getY(), this.size)));
        return new Board(this.size, clonedQueens);
    }
}
