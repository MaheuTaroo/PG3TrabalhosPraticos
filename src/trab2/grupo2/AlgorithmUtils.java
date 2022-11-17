package trab2.grupo2;

import java.util.*;

public class AlgorithmUtils {
    public static <E> boolean isOrdered(Collection<E> seq, Comparator<E> compareValue ) {
        if (seq.isEmpty()) return false;

        boolean ascending = false, descending = false;
        E[] array = (E[])seq.toArray();
        for (int i = 0; i < array.length - 1; i++) {
            if (compareValue.compare(array[i], array[i + 1]) < 0) ascending = true;
            if (compareValue.compare(array[i], array[i + 1]) > 0) descending = true;
            if (ascending && descending) return false;
        }

        return true;
    }

    public static <E> List<E> getSubSequences(Collection<E> seq, int n, Comparator<E> cmp ) {
        if (n < 1) throw new IllegalArgumentException();
        if (seq.isEmpty()) return List.of();

        int subSeqs = 1;

        E[] array = (E[])seq.toArray();
        ArrayList<E> tmp = new ArrayList<>();

        for (int i = 0; i < array.length - 1; i++) {
            if (cmp.compare(array[i], array[i + 1]) > 0) {
                if (subSeqs == n) {
                    tmp.add(array[i]);
                    break;
                }
                tmp = new ArrayList<>();
                subSeqs++;
                continue;
            }

            tmp.add(array[i]);
            if (i == array.length - 2) tmp.add(array[i + 1]);
        }

        if (subSeqs < n) return List.of();
        return tmp;
    }

    public static void main(String[] args) {
        List<Integer> l = List.of(1,2,3,2,4,5,3,6,3);

        System.out.println(getSubSequences(l, 4, Integer::compare));
    }

}
