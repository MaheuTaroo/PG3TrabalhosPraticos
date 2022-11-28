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
        // TODO - arranjar getValue
        AlgorithmUtils.addAll(new BufferedReader(new FileReader(names)), families, null, Families::surname, supplier);
    }
}
