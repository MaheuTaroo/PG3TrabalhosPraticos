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

        if(seq.isEmpty()) return false;
        if(seq.size() == 1) return true;
        Iterator<E> iter = seq.iterator();
        E e = iter.next(), tmp = iter.next();
        boolean ascending = false, descending = false;

        while(iter.hasNext()) {
            if(compareValue.compare(e, tmp) < 0) ascending = true;
            else if(compareValue.compare(e, tmp) > 0) descending = true;
            if(ascending && descending) return false;
            e = tmp;
            tmp = iter.next();

            if(!iter.hasNext()) {
                if(compareValue.compare(e, tmp) < 0) ascending = true;
                else if(compareValue.compare(e, tmp) > 0) descending = true;
                if(ascending && descending) return false;
                break;
            }
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

        if(n < 1) throw new IllegalArgumentException();
        if(seq.isEmpty()) return List.of();
        if(seq.size() == 1 && n == 1) return new ArrayList<>(seq);
        Iterator<E> iter = seq.iterator();
        E e = iter.next(), temp = iter.next();
        int segs = 1;
        ArrayList<E> tmp = new ArrayList<>();


        if(seq.size() == 2) {
            if(cmp.compare(e,temp) < 0) {
                if(segs == n) {
                    tmp.add(e);
                    tmp.add(temp);
                } else {
                    segs++;
                    tmp.add(temp);
                }
            } else if(cmp.compare(e, temp) > 0) {
                if(n == 1) {
                    segs++;
                    tmp.add(e);
                }
                else if(n == 2) {
                    segs++;
                    tmp.add(temp);
                }
            }
        }

        while(iter.hasNext()) {
            if(cmp.compare(e, temp) > 0) {
                if(segs == n) {
                    tmp.add(e);
                    break;
                }
                tmp = new ArrayList<>();
                segs++;
                e = temp;
                temp = iter.next();
                if(!iter.hasNext()) {
                    if(cmp.compare(e, temp) < 0) {
                        tmp.add(e);
                        tmp.add(temp);
                        break;
                    }
                }
                continue;
            }

            tmp.add(e);
            e = temp;
            temp = iter.next();
            if(!iter.hasNext()) {
                if(cmp.compare(e, temp) < 0) {
                    tmp.add(e);
                    tmp.add(temp);
                    break;
                }
                if(segs + 1 == n && cmp.compare(e,temp) > 0) {
                    segs++;
                    tmp = new ArrayList<>();
                    tmp.add(temp);
                    break;
                }
                tmp.add(e);
                break;
            }
        }

        if (subSeqs < n) return List.of();
        return tmp;
    }

    public static void main(String[] args) {
        List<Integer> l = List.of(1,2,3,2,4,5,3,6,3);

        System.out.println(getSubSequences(l, 4, Integer::compare));
    public static <K,V,C extends Collection<V>> void addAll(BufferedReader in, Map<K,C> m, Function<String, V> getValue, Function<V, K> getKey, Supplier<C> supC)  throws IOException {
        String s;
        while((s = in.readLine()) != null) {
            V v = getValue.apply(s);
            K k = getKey.apply(v);
        }
    }

    public static <K,V,C extends Collection<V>> void forEachIf(Map<K,C> m, Predicate<K> pred, Consumer<V> action) {

    }
}
