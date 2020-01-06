import java.util.stream.IntStream;

public class MergeSort {
    public static void main(String[] args) {
        int[] ar1 = {10,8,2,6,5,1,4,0,9,7,3,-1,-2,-3,0,10,2,4};
        // top-down sort
        topDownMergeSort(ar1);
        IntStream.of(ar1).forEach(System.out::print);

        System.out.println();

        // bottom-up sort
        int [] ar2 = {10,8,2,6,5,1,4,0,9,7,3,-1,-2,-3,0,10,2,4};
        bottomUpMergeSort(ar2);
        for (int i: ar2)
            System.out.print(i);
    }

    private static void bottomUpMergeSort(int[] ar1) {
        int[] aux = new int[ar1.length];
        for (int step = 2; step <= ar1.length * 2; step *= 2) {
            for (int low = 0; low < ar1.length; low += step)
                merge(ar1, aux, low, (low + low + step - 1) / 2, Math.min(low + step - 1, ar1.length - 1));
        }
    }

    private static void topDownMergeSort(int[] ar) {
        int[] aux = new int[ar.length];
        sort(ar, aux, 0, ar.length -1);
        System.arraycopy(aux, 0, ar, 0, aux.length);
    }

    private static void sort(int[] ar, int[] aux, int lo, int hi) {
        if (lo == hi) return;
        int mid = (hi + lo) / 2;
        sort(ar, aux, lo, mid);
        sort(ar, aux, mid + 1, hi);
        merge(ar, aux, lo, mid, hi);
    }

    private static void merge(int[] ar, int[] aux, int lo, int mid, int hi) {
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)                aux[k] = ar[j++];
            else if (j > hi)            aux[k] = ar[i++];
            else if (ar[i] <= ar[j])    aux[k] = ar[i++];
            else                        aux[k] = ar[j++];
        }
        System.arraycopy(aux, lo, ar, lo, hi - lo + 1);
    }
}