package com.company;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public class Helpers {

    static int rowPredictor(int size){
        int retVal = 1;
        while ((Math.pow(size, retVal) / 2) < 1000) {
            if (size - retVal > 1 ) {
                retVal++;
            }
            else break;
        }
        return retVal;

    }

    public static Predicate<Future<Board>> notNull = new Predicate<Future<Board>>() {
        @Override
        public boolean test(Future<Board> boardFuture) {
            Board board = null;
            try {
                board = boardFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return board != null;
        }
    };
}
