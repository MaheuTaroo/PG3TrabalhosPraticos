package trab2.grupo2;

import java.util.function.*;
import java.io.*;
import java.util.*;

public class AlgorithmUtils {
    public static <E> boolean isOrdered(Collection<E> seq, Comparator<E> compareValue ) {
        if (seq.isEmpty()) return false;
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
        ArrayList<E> tmp = new ArrayList<>();
        if (seq.isEmpty()) return tmp;
        Iterator<E> iter = seq.iterator();
        E e, temp = iter.next();
        int segs = 1;

        if (n == 1) tmp.add(temp);
        while(iter.hasNext()) {
            e = iter.next();

            if (cmp.compare(e, temp) < 0) {
                segs++;
                if (segs > n) return tmp;
            }

            if (segs == n) tmp.add(e);

            temp = e;
        }

        return tmp;
    }

    public static <K,V,C extends Collection<V>> void addAll(BufferedReader in, Map<K,C> m, Function<String, V> getValue, Function<V, K> getKey, Supplier<C> supC) throws IOException {
        String s;
        while((s = in.readLine()) != null) {
            V value = getValue.apply(s);
            K key = getKey.apply(value);
            C c = m.get(key);
            if(c == null) {
                c = supC.get();
                m.put(key, c);
            }
            c.add(value);
        }
    }

    public static <K,V,C extends Collection<V>> void forEachIf( Map<K,C> m, Predicate<K> pred, Consumer<V> action) {
        m.forEach((key, c) -> {
            if(pred.test(key))
                for (V v : m.get(key))
                    action.accept(v);
        });
    }

    public static <K,V,C extends Collection<V>> void forEachIf( Map<K,C> m, BiPredicate<K,C> p, BiConsumer<K,C> action) {
        m.forEach((key, c) -> {
            if(p.test(key, c))
                action.accept(key, c);
        });
    }
}
