package com.example.cmsc45111;
/*
Nardos Lemma Project One
July 9, 2023
CMSC451 BenchmarkSorts.java Class
 */

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BenchmarkSorts extends AbstractSort {

    private static final int DATA_SET_COUNT = 40;
    private static final int[] DATA_SET_SIZES = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200};
    private static final int ROW_COUNT = 5;
    private final int[][] bubbleCount;
    private final int[][] mergeCount;
    private final long[][] bubbleTime;
    private final long[][] mergeTime;
    private final BubbleSort bubbleSort;
    private final MergeSort mergeSort;
    private final Object[][] bubbleResultForm;
    private final Object[][] mergeResultForm;
    private final Scanner scanner;
    private final JFileChooser fileChooser;
    private File file;

    BenchmarkSorts() {
        scanner = new Scanner(System.in);
        bubbleTime = new long[DATA_SET_SIZES.length][DATA_SET_COUNT];
        mergeTime = new long[DATA_SET_SIZES.length][DATA_SET_COUNT];
        bubbleCount = new int[DATA_SET_SIZES.length][DATA_SET_COUNT];
        mergeCount = new int[DATA_SET_SIZES.length][DATA_SET_COUNT];
        bubbleResultForm = new Object[DATA_SET_SIZES.length][ROW_COUNT];
        mergeResultForm = new Object[DATA_SET_SIZES.length][ROW_COUNT];
        mergeSort = new MergeSort();
        bubbleSort = new BubbleSort();
        fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
    }

    // Displays the Menu
    public void displayMenu() {
        System.out.println("Welcome:");
        System.out.println("Would you like roceed to tests? 1 for Yes, 2 for No.");
        int answer = scanner.nextInt();
        if (answer == 1) {
            BenchmarkSorts.CriticalOps.JVMWarmUp();
            runSortingMethods();
            outputFile();
            System.out.println("Press 1 again to select which sort you want.");
            answer = scanner.nextInt();
            if (answer == 1) {
                selectFile();
                generateReport();
                showReport();
            }
        }
        System.out.println("Thank you!");
    }

    private void runSortingMethods() {
        //Warm up JVM
        CriticalOps.JVMWarmUp();
        //for all 12 data sizes
        for (int i = 0; i < DATA_SET_SIZES.length; i++) {
            int dataSize = DATA_SET_SIZES[i];
            //run the sort 40 times
            for (int j = 0; j < DATA_SET_COUNT; j++) {
                int[] bubbleArr = CriticalOps.getRandomData(dataSize);
                int[] mergeArr = bubbleArr.clone();

                bubbleSort.sort(bubbleArr);
                boolean check = AbstractSort.isSorted(bubbleArr);
                if (check != true) {
                    System.out.println("Warning, array is not sorted!");
                    if (check == true) {
                        System.out.println("BubbleSort is in order.");
                    }
                    continue;
                }
                mergeSort.sort(mergeArr);
                boolean check2 = AbstractSort.isSorted(mergeArr); // redundant, but will check it anyways
                if (check != true) {
                    System.out.println("Warning, array is not sorted!");
                    if (check2 == true) {
                        System.out.println("Merge Sort is in order.");
                    }
                    continue;
                }
                bubbleCount[i][j] = bubbleSort.getCount();
                mergeCount[i][j] = mergeSort.getCount();
                bubbleTime[i][j] = bubbleSort.getTime();
                mergeTime[i][j] = mergeSort.getTime();
                bubbleSort.resetSort();
                mergeSort.resetSort();
            }
        }
    }

    public void UnsortedException() {
        new Error();
    }

    // output to "ProjectOneOutput.txt" using writeData method
    private void outputFile() {
        System.out.println("File output.");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter
                (new FileOutputStream("ProjectOneOutput.txt"),
                        StandardCharsets.UTF_8))) {
            writeData(writer, bubbleCount, bubbleTime);
            writer.write("\n");//new line to separate bubble and mergesorts
            writeData(writer, mergeCount, mergeTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // write data method to help outputFile write bubble and merge
    // counts and times, loops data set size, puts a space between indexes
    // and will loop through specific set Counts per sizes, to write counts/times
    private void writeData(Writer writer, int[][] counts, long[][] times) throws IOException {
        for (int i = 0; i < DATA_SET_SIZES.length; i++) {
            writer.write(DATA_SET_SIZES[i] + " ");
            for (int j = 0; j < DATA_SET_COUNT; j++) {
                writer.write(counts[i][j] + "," + times[i][j] + " ");
            }
            writer.write("\n");
        }
    }

    private void selectFile() {
        System.out.println("Select file.");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
        }
    }

    // will read the selected file line by line and input, when last line is read, ends.
    private void generateReport() {
        System.out.println("Report is generated!");
        if (file != null) {
            try {
                BufferedReader reader;
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                int index = 0;
                while (line != null) {
                    readStringData(line, index);
                    index++;
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("IO Exception parsing: " + file.getName());
            }
        } else {
            System.err.println("File selected not found.");
        }
    }

    // method to help read data string for first 12 lines, with a space to separate
    // bubble data and merge data
    private void readStringData(String stringLine, int fileLine) {
        String[] stringData = stringLine.split(" ");
        double[] countData = new double[DATA_SET_COUNT];
        double[] timeData = new double[DATA_SET_COUNT];
        if (fileLine < 12) { //BubbleSort Data
            parseLineData(fileLine, stringData, countData, timeData, bubbleResultForm, bubbleResultForm[fileLine]);
            //Merge starts at 13
        } else if (fileLine > 12) { //MergeSort Data
            int mergeIndex = fileLine - 13; //
            parseLineData(mergeIndex, stringData, countData, timeData, mergeResultForm, mergeResultForm[mergeIndex]);
        }
    }

    // helps to calculate averages of counts and times to sort through array
    // and format the results into rows(2,3,4,5)
    private void parseLineData(int index, String[] stringData, double[] countData,
                               double[] timeData, Object[][] resultForm,
                               Object[] resultsRow) {
        resultForm[index][0] = stringData[0];
        for (int i = 1; i <= DATA_SET_COUNT; i++) {
            String val = stringData[i];
            String[] countAndTime = val.split(",");
            countData[i - 1] = Double.parseDouble(countAndTime[0]);
            timeData[i - 1] = Double.parseDouble(countAndTime[1]);
        }
        // get mean for COUNTS
        resultsRow[1] = String.format("%.2f", CriticalOps.getMean(countData));
        // format for % symbol rows 3 and 5
        resultsRow[2] = String.format("%.2f%%", CriticalOps.getCoefficientOfVariance(countData));
        // get mean for TIME
        resultsRow[3] = String.format("%.2f", CriticalOps.getMean(timeData));
        // coefVariance TIME, % symbol for row.
        resultsRow[4] = String.format("%.2f%%", CriticalOps.getCoefficientOfVariance(timeData));
    }

    // Create JFrame for both algo outputs, adjust Bounds as needed until windows
    // look big enough to hold information.
    private void showReport() {
        //create a report table for each sort type
        String[] titles = {"Size", "Avg Count", "Coef Count", "Avg Time", "Coef Time"};
        JFrame bubbleFrame = new JFrame("BubbleSort Report");
        JFrame mergeFrame = new JFrame("MergeSort Report");
        bubbleFrame.setBounds(50, 40, 600, 300);// same height, y, merge x so both visable
        mergeFrame.setBounds(650, 40, 600, 300);
        JScrollPane bubScroll = new JScrollPane(new JTable(bubbleResultForm, titles));
        JScrollPane mergeScroll = new JScrollPane(new JTable(mergeResultForm, titles));
        bubbleFrame.add(bubScroll);
        mergeFrame.add(mergeScroll);
        bubbleFrame.setVisible(true);
        mergeFrame.setVisible(true);
        bubbleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mergeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public long sort(int[] arr) {
        return 0;
    }

    public class CriticalOps {

        public static double getMean(double[] data) {
            double sum = Arrays.stream(data).sum();
            return sum / (double) data.length;
        }

        // using math.random * 1000 to produce random array of int.
        public static int[] getRandomData(int n) {
            int[] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = (int) (Math.random() * 1000.0);
            }
            return array;
        }

        public static double getStandardDeviation(double[] data) {
            double sum = 0;
            for (double datum : data) {
                sum += (datum - getMean(data)) * (datum - getMean(data));
            }
            return Math.sqrt(sum / (data.length - 1));
        }

        public static double getCoefficientOfVariance(double[] data) {
            return getStandardDeviation(data) / getMean(data) * 100.0;
        }

        public static void JVMWarmUp() {
            for (int i = 0; i < 1000000; ++i) {
                JVMTest newTest = new JVMTest();
                newTest.method();
            }
        }

        public static class JVMTest {
            public void method() {
            }
        }
    }
}