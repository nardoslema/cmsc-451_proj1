package com.example.cmsc45111;

/*
Nardos Lemma Project One
July 9, 2023

 */
public abstract class AbstractSort {
    protected long startingTime;
    protected int count;
    protected long endingTime;
//start of sort
    public void startingSort() {
        startingTime = System.nanoTime();
        count = 0;
    }
    //end of sort
    public void endSort() {
        this.endingTime = System.nanoTime();
    }
    //increments the count
    public void incrementCount() {
        count++;
    }
    //this method gets the time
    public long getTime() {
        return System.nanoTime() - startingTime;
    }
    public int getCount() {
        return count;
    }

    public abstract long sort(int[] arr);
    // boolean to check Array after bubble/merge sorts are called
    public static boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }}
        return true;
    }
    public void resetSort() {
        startingTime = 0;
        count = 0;
        endingTime = 0;
    }

}
