import java.util.stream.IntStream;

public class MergeSort1 {
    public static void main(String[] args) {
        int[] ar = {2,6,3,6,9,5,3,5,8,0,-1,0};
        mergesort(ar);
        IntStream.of(ar).forEach(System.out::print);
    }

    private static void mergesort(int[] ar) {
        sort(ar, new int[ar.length], 0, ar.length - 1);
    }

    private static void sort(int[] ar, int[] aux, int lo, int hi) {
        if (lo == hi) return;
        int mid = (lo + hi) / 2;
        sort(ar, aux, lo, mid);
        sort(ar, aux, mid + 1, hi);
        merge(ar, aux, lo, mid, hi);
    }

    private static void merge(int[] ar, int[] aux, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid)        aux[k] = ar[j++];
            else if (j > hi)    aux[k] = ar[i++];
            else if (ar[j] < ar[i])  aux[k] = ar[j++];
            else                aux[k] = ar[i++];
        }
        System.arraycopy(aux, lo, ar, lo, hi - lo + 1);
    }
}