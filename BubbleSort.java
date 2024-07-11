package com.example.cmsc45111;


class BubbleSort extends AbstractSort {
    @Override
    public long sort(int[] arr) {
        // while swap
        startingSort();
        int n = arr.length;
        boolean isSwapped = true;
        int i = 0;
        while (isSwapped) {
            isSwapped = false;
            i++; // for j less than length -1 , ++
            for (int j = 0; j < n - i; j++) {// for index of array in sort,
                if (arr[j] > arr[j + 1]) {// if > than next index of arr. swap
                    swap(arr, j, j + 1);
                    incrementCount();
                    isSwapped = true;
                }
            }
        }
        endSort();
        return getTime();
    }
    //this method resets the sort
    @Override
    public void resetSort() {
        this.startingTime = 0;
        this.count = 0;
        this.endingTime = 0;
    }
    //this method is swap
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}