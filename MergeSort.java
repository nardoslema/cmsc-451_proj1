package com.example.cmsc45111;
class MergeSort extends AbstractSort {

    public long sort(int[] arr) {
        startingSort();
        mergeSort(arr, 0, arr.length - 1);
        endSort();
        return getTime();
    }
    //constructor
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }
    private void merge(int[] arr, int left, int middle, int right) {
        int[] temporary = new int[right - left + 1];
        int i = left;
        int j = middle + 1;
        int k = 0;
        while (i <= middle && j <= right) {
            if (arr[i] < arr[j]) {
                temporary[k++] = arr[i++];
            } else {
                temporary[k++] = arr[j++];
            }
            incrementCount();
        }
        while (i <= middle) {
            temporary[k++] = arr[i++];
        }
        while (j <= right) {
            temporary[k++] = arr[j++];
        }
        for (int m = 0; m < temporary.length; m++) {
            arr[left + m] = temporary[m];
        }
    }
    //this method is a reset method
    @Override
    public void resetSort() {
        this.startingTime = 0;
        this.count = 0;
        this.endingTime = 0;
    }

}