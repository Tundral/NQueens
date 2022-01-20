package com.company;


public class Drawer {

    /*
    This whole class is really just for testing purposes and thus everything here is just quickly scribbled up
    without much thought for the efficiency of the code.
     */

    /**
     * Draws Board board onto the console
     * @param board Board to draw
     */
    public static synchronized void draw(Board board){


        //Draw the top edge of the board
        System.out.println(
                "╔═" +
                        repeatString("╦═", board.getSize() - 1) +
                        "╗");


        //Outer loop for drawing each row
        for (int y = board.getSize() ; y > 0 ; y--) { //Note: iteration max y to 0 so from up to down

            //Inner loop for drawing each position x in row y
            for (int x = 1; x <= (board.getSize()); x++) {

                //Use isPresent to find out if given position is occupied by a queen
                if (queenPresent(board, x, y)){
                    System.out.print("║█");//if so draw line and solid square
                }else{
                    System.out.print("║ ");//if not draw line and empty space
                }
            }

            //Vertical bar at the end of each line
            System.out.println("║");
        }

        ////Draw the bottom edge of the board
        System.out.println(
                "╚═" +
                        repeatString("╩═", board.getSize() - 1) +
                        "╝");

    }

    /**
     * Repeats and concatenates a string n amount of times and returns the resulting string
     * @param string String to repeat
     * @param n Times to repeat
     * @return resulting String
     */
    public static String repeatString(String string, int n){
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < n; i++) {
            retVal.append(string);
        }
        return retVal.toString();
    }


    /**
     * Checks if Board board has a queen in location x y and returns boolean based on the result
     * @param board Board to check in
     * @param x x coordinate to check at
     * @param y y coordinate to check at
     * @return Boolean representing if there is a queen at x location
     */
    public static boolean queenPresent(Board board, int x, int y){
        for (Queen q : board.getQueens()) {
            if (q.getX() == x && q.getY() == y) return true;
        }
        return false;
    }
}
