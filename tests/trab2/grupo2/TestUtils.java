package trab2.grupo2;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static trab2.grupo2.AlgorithmUtils.*;

public class TestUtils {
    @Test
    public void SampleTest() {
        assertTrue(isOrdered(List.of(5,1,-6), Integer::compare));
    }
}
