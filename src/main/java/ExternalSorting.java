import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ExternalSorting {
    static class MinHeapNode implements Comparable<MinHeapNode> {
        int element;
        int i; // index of array from which it is picked

        public MinHeapNode(int element, int i) {
            this.element = element;
            this.i = i;
        }

        public int compareTo(MinHeapNode n) {
            return Integer.compare(element, n.element);
        }
    }

    static void mergeKSortedFiles(String outputFileName, Integer k) throws Exception {
        Scanner[] sc = new Scanner[k];
        for (int i = 0; i < k; i++) {
            String fileName = String.format("%d", i);
            sc[i] = new Scanner(new File(fileName));
        }

        MinHeapNode[] minHeapNodeArr = new MinHeapNode[k];
        PriorityQueue<MinHeapNode> minHeap = new PriorityQueue<>();

        int i; // need i for max count, so can break from next while loop
        for (i = 0; i < k; i++) {
            if (!sc[i].hasNext()) {
                break;
            }
            String nextData = sc[i].next();
            minHeapNodeArr[i] = new MinHeapNode(Integer.parseInt(nextData), i);
            minHeap.add(minHeapNodeArr[i]);
        }

        int count = 0;

        // Now one by one get the minimum element from min heap and replace it with the next element
        // run this all filled input files reach EOF
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
        while (count != i) {
            MinHeapNode root = minHeap.poll();
            writer.write(root.element + " ");

            if (!sc[root.i].hasNext()) {
                root.element = Integer.MAX_VALUE;
                count++;
            } else {
                root.element = sc[root.i].nextInt();
            }

            minHeap.add(root);
        }

        // close input output files
        for (int j = 0; j < k; j++) {
            sc[j].close();
        }
        writer.close();

    }

    static int createIntialRuns(String inputFileName, int run_size) throws Exception {
        Scanner sc = new Scanner(new File(inputFileName));
        BufferedWriter writer;

        int num_ways = 0;
        int[] arr = new int[run_size];
        boolean more_input = true;
        int i;

        while (more_input) {
            // write run_size elements into arr from input
            for (i = 0; i < run_size; i++) {
                if (!sc.hasNext()) {
                    more_input = false;
                    break;
                }
                arr[i] = sc.nextInt();
            }
            if (i == 0) break;
            //mergeSort(arr, 0, i - 1);
            Arrays.sort(arr, 0, i);

            String outputFileName = String.format("%d", num_ways);
            writer = new BufferedWriter(new FileWriter(outputFileName));

            // write the records to the appropriate scratch output file
            // can't assume that the loop runs to run_size
            // since the last run's length may be less than run_size

            for (int j = 0; j < i; j++) {
                writer.write(arr[j] + " ");
            }
            num_ways++;
            writer.close();
        }
        // close input and output files
        sc.close();
        return num_ways;
    }

    static void externalSort(String inputFile, String outputFile, int run_size) throws Exception {
        int num_ways = createIntialRuns(inputFile, run_size);
        mergeKSortedFiles(outputFile, num_ways);
    }

    public static void main(String[] args) throws Exception {
        int run_size = 5;
        externalSort("input.txt", "output.txt", 5);
    }
}
