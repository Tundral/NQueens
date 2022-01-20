package com.company;



import org.omg.CORBA.portable.ApplicationException;
import sun.swing.BakedArrayList;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    /*
        Written report included in the file nqueensreport.docx in the root of the returned zip file.
     */

    public static void main(String[] args) {

        //singleThreadAlgo(30);
        multiThreadAlgo(32, true);

    }


    /**
     * singleThreadAlgo will use a non-parallelized version of Djikstra's backtrack algorithm (implemented in BackTrackSolver) to find and draw a solution to all n queens problems
     * from 4 to param. n queens. Solving of each queen amount is non-parallelized but multiple queen amounts will be solved simultaneously on all available HW threads.
     *
     * @param n highest amount of queens to be solved. (i.e. n = 8 will solve boards with 4, 5, 6, 7 and 8 queens)
     */
    private static void singleThreadAlgo(int n) {

        NumberFormat formatter = new DecimalFormat("#0.0000");

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //Our mighty executor
        CompletionService<Solver> completionService = new ExecutorCompletionService<>(executor); //Our glorious completion service

        int remainingJobs = 0; //Will keep track of the total amount of submitted solving jobs

        Board board = null; //Placeholder value for the boards to be submitted to be solved
        for (int i = 4; i < n + 1; i++) {
            board = new Board(i); //Create the board to be solved

            //Add initial queens to the board
            board.addQueen(1,1);
            board.addQueen(3,2);

            //Submit a solver with the generated board to the completion service
            completionService.submit(new BackTrackSolver(board));

            remainingJobs++;
        }

        int totalJobs = remainingJobs; //Record the total amount of jobs that were submitted

        Future<Solver> resultFuture;       //Placeholder variable for the completed future
        Solver resultingSolver;            //Placeholder variable for the completed solver (from the future)

        while(remainingJobs > 0){ //Repeat loop while there are still futures uncompleted


            try {
                resultFuture = completionService.take();    //Will wait for and take a completed future
                resultingSolver = resultFuture.get();       //Get the completed solver from the future
                remainingJobs--;                            //Reduce the amount of remaining jobs

                Drawer.draw(resultingSolver.getBoard());    //Draw the solved board

                System.out.println("Size of board: " + resultingSolver.getBoard().getSize());

                System.out.println("Board solving time was " + formatter.format((resultFuture.get().getSolveTime()) / 1000d) + " seconds");

                System.out.print("Quuens of above board: ");

                //Print positions of all queens on the solved board
                resultingSolver.getBoard().getQueens().forEach(new Consumer<Queen>() {
                    @Override
                    public void accept(Queen queen) {
                        System.out.print(queen.toString() + " ");
                    }
                });
                //

                System.out.println();
                System.out.println("Boards solved: " + (totalJobs - remainingJobs) + " / " + totalJobs);
                System.out.println();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(750); //Sleep so all the small boards in the beginning don't just explode onto the console
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdownNow();
        }

    }

    /**
     * multiThreadAlgo will use a parallelized version of Djikstra's backtrack algorithm to
     * find one or more solutions to a single n queens problem.
     * Found solutions are not checked for rotations or mirrors of already found solutions.
     * @param n The amount of queens we want to solve. (also the size of the board)
     * @param stopafterfirst If true, the method will stop after first valid board is found
     */
    static void multiThreadAlgo(int n, boolean stopafterfirst) {

        System.out.print("Solving " + n + " queens and ");
        System.out.println((stopafterfirst) ? "stopping after a solution is found" : "printing all found solutions");

        if (n < 4) throw new IllegalArgumentException("Such a glorious algorithm shall not be used to solve such measly problems!");
        NumberFormat formatter = new DecimalFormat("#0.00");

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());    //Our mighty executor
        CompletionService<Solver> completionService = new ExecutorCompletionService<Solver>(executor);              //Our glorious completion service

        //Board generator to generate starting point boards for the solvers to start working on
        BoardGenerator gen = new BoardGenerator(n, Helpers.rowPredictor(n)); //rowPredictor(n) guesstimates the amount of rows for us to pre-generate

        /*  StartingBoards will hold the generated boards to be solved.
            This list is only necessary because the starting boards are to be given to the executor in random order
         */
        ArrayList<Board> startingBoards = new ArrayList<>();

        ArrayList<Board> resultingBoards = new ArrayList<>();


        long start = System.currentTimeMillis(); //Get start time


        System.out.println("Generating boards");
        /*
        Below loop will get all the possible starting boards from the board generator (end of possible starting boards is indicated
        by the generator returning a null value) and place them into the startingBoards list.
         */
        Board val; //Placeholder variable for the generated boards
        while (true) {
            val = gen.next();
            if (val == null) break;
            startingBoards.add(val); //Add the generated board to the list of starting boards
        }

        System.out.println("Done generating " + startingBoards.size() +  " boards");
        System.out.println("Lighting up threads!");

        int remainingJobs = 0; //Will keep track of the total amount of submitted solving jobs


        /*
        Below loop will (in random order) get all the boards to be solved from startingBoards list,
        submit them to the completionService and remove them from startingBoards
         */
        Random random = new Random();
        int index;
        while(!startingBoards.isEmpty()) {
            index = random.nextInt(startingBoards.size());
            completionService.submit(new PartialBackTrackSolver(startingBoards.get(index)));
            startingBoards.remove(index);
            remainingJobs++;
        }


        int totalJobs = remainingJobs;  //Record the total amount of jobs that were submitted
        int rejected = 0;               //Value to record hypothetical rejected boards.
        /*
        In this programs current form the rejected jobs variable is not actually used since all threads will eventually stumble upon
        a solution. If the program was to check if a found board is a mirror/rotation of an already found board then this value could
        be used to keep track of how many of such boards were found
         */

        Future<Solver> resultFuture;       //Placeholder variable for the completed future
        Solver resultingSolver;            //Placeholder variable for the completed solver
        Board resultingBoard;              //Placeholder variable for the found board
        boolean match = false;

        do{

            try {
                resultFuture = completionService.take(); //Get result from the completionService


                if ((resultingBoard = resultFuture.get().getBoard()) == null) {
                    rejected++;
                    remainingJobs--;
                    continue;
                }

                resultingBoards.add(resultingBoard);

                remainingJobs--; //Reduce the amount of remaining jobs

                Drawer.draw(resultingBoard); //Draw the found board

                System.out.println("Size of board: " + resultingBoard.size);

                System.out.println("Solver execution time was " + formatter.format((resultFuture.get().getSolveTime()) / 1000d) + " seconds");
                System.out.print("Queens of above board: ");

                resultingBoard.getQueens().forEach(new Consumer<Queen>() {
                    @Override
                    public void accept(Queen queen) {
                        System.out.print(queen.toString() + " ");
                    }
                });

                System.out.println();

                if (stopafterfirst) break;

                System.out.println("=====================================");
                System.out.println("Solutions found: " + (totalJobs - remainingJobs));
                System.out.println("Rejected starting boards: " + rejected);
                System.out.println("Remaining starting boards " + (remainingJobs) + " / " + totalJobs );
                System.out.println("=====================================");
                System.out.println();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } while (remainingJobs > 0);


        long end = System.currentTimeMillis(); //Get end time

        executor.shutdownNow();

        System.out.println();

        System.out.println("Total execution time for multiThreadAlgo was " + formatter.format((end - start) / 1000d) + " seconds");


    }
}
