package com.company;

public class Queen implements Cloneable {

    //Field variables

    private int x;
    private int y;
    private final int maxCords;

    //Constructors

    public Queen(int x, int y, int maxCords) {
        this.x = x;
        this.y = y;
        this.maxCords = maxCords;
    }

    //Encapsulation methods

    public int getX() { return x; }

    public int getY() { return y; }


    //General methods

    /**
     * Attempts to move this queen (+/-)x places in the X axis and (+/-)y places in the y
     * axis. If the move would make the piece out of bounds false will be returned and
     * no movement of queen executed
     * @param x How many places to the positive or negative X to try to move
     * @param y How many places to the positive or negative Y to try to move
     * @return If the movement was allowed (and executed)
     */
    public boolean tryMove(int x, int y) {
        if (((this.x + x) > maxCords) || ((this.y + y) > maxCords)) return false; //If Y or X goes out of bounds return false
        else { //Otherwise set values
            this.x += x;
            this.y += y;
            return true;
        }
    }

    /**
     * Checks if this and that queen threaten each other.
     * @param that Queen to check against
     * @return Boolean result of the check
     */
    public boolean threatens(Queen that){

            /*
            Because the anyThreats method will actually check queens against themselves we need below
            condition or otherwise the logic determine that this queen threatens that queen but they're the same queen.
            */
        if (this == that) return false;

            /*
            If Y or X values of this and that are the same they threaten each other 'cause they're either
            horizontally or vertically aligned.
            */
        if ((this.getX() == that.getX()) || (this.getY() == that.getY())) return true;

        //If ΔX and ΔY are equal it means the two pieces are diagonally aligned, meaning they threaten each other.
        if (Math.abs(this.getX() - that.getX()) - Math.abs((this.getY() - that.getY())) == 0) return true;

        return false; //If checks passed no queens threaten each other so return false
    }

    //Override methods


    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Queen(this.x, this.y, this.maxCords);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final Queen other = (Queen) obj;

        if (this.maxCords != other.maxCords) return false;

        if ((this.getX() == other.getX() && this.getY() == other.getY())) return true;

        return false;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
