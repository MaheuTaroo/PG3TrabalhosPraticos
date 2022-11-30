package trab2.grupo2;

import java.io.*;
import java.util.*;
import java.util.function.*;
import trab2.grupo2.AlgorithmUtils;

public class Families<C extends Collection<String>> {
    private final Map<String, C> families;
    private final Supplier<C> supplier;

    public Families(Supplier<Map<String, C>> supMap, Supplier<C> supC) {
        families = supMap.get();
        supplier = supC;
    }

    public static String surname(String name) {
        return name.substring(name.lastIndexOf(' '));
    }

    public Set<String> getSurnames() {
        return families.keySet();
    }

    public C getNames(String surname) {
        return families.get(surname);
    }

    public void addNames(File names) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(names))){
            AlgorithmUtils.addAll(br , families, Function.identity(), Families::surname, supplier);
        }
    }

    public void addName(String name) {
        try (BufferedReader br = new BufferedReader(new StringReader(name))) {
            AlgorithmUtils.addAll(br , families, Function.identity(), Families::surname, supplier);
        }
        catch (IOException ignored) {

        }
    }

    public void forEachName(Consumer<String> action) {
        AlgorithmUtils.forEachIf(families, s -> true, action);
    }

    public void forEach( BiConsumer<String, C> action) {
        AlgorithmUtils.forEachIf(families, (s, t) -> true, action);
    }

    public void printFamilies(PrintWriter out, Set<String> except) {
        AlgorithmUtils.forEachIf(families, (s, t) -> !except.contains(s), (s, t) -> {
            out.println(s + ": " + getNames(s).size());
            for (String name : t) {
                out.println("\t" + name);
            }
        });
    }

    public Set<String> getGreaterFamilies() {
        Set<String> res = new TreeSet<>();
        // (e, f) -> e.getValue().size() - f.getValue().size()
        // (e, f) -> Integer.compare(e.getValue().size(), f.getValue().size())
        Map.Entry<String, C> temp = Collections.max(families.entrySet(), Comparator.comparingInt(e -> e.getValue().size()));

        res.add(temp.getKey());

        AlgorithmUtils.forEachIf(families, (k, v) -> v.size() == temp.getValue().size(), (k, v) -> res.add(k));

        return res;
    }
}