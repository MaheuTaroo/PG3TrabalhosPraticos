package trab2.grupo2;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static trab2.grupo2.AlgorithmUtils.*;

public class TestAlgorithmUtils {
    @Test
    public void testIsOrdered() {
        assertTrue(isOrdered(List.of(1), Integer::compareTo));
        assertTrue(isOrdered(List.of(1, 2), Integer::compareTo));
        assertTrue(isOrdered(List.of(2, 1), Integer::compareTo));
        assertTrue(isOrdered(List.of(1, 2, 3), Integer::compareTo));
        assertTrue(isOrdered(List.of(3, 2, 1), Integer::compareTo));
        assertTrue(isOrdered(List.of(1, 2, 3, 4), Integer::compareTo));
        assertTrue(isOrdered(List.of(4, 3, 2, 1), Integer::compareTo));
        assertFalse(isOrdered(List.of(1, 3, 2), Integer::compareTo));
        assertFalse(isOrdered(List.of(1, 2, 3, 5, 4), Integer::compareTo));
        assertFalse(isOrdered(List.of(5, 2, 3), Integer::compareTo));
        assertFalse(isOrdered(List.of(5, 4, 3, 1, 2), Integer::compareTo));
        assertFalse(isOrdered(List.of(), Integer::compareTo));
        assertTrue(isOrdered(List.of("aa", "bb", "cc"), String::compareTo));
        assertTrue(isOrdered(List.of("cc", "bb", "aa"), String::compareTo));
        assertTrue(isOrdered(List.of("aa", "bb", "cc", "dd"), String::compareTo));
        assertTrue(isOrdered(List.of("dd", "cc", "bb", "aa"), String::compareTo));
        assertFalse(isOrdered(List.of("aa", "cc", "bb"), String::compareTo));
        assertFalse(isOrdered(List.of("cc", "aa", "bb"), String::compareTo));
        assertFalse(isOrdered(List.of("aa", "bb", "dd", "cc"), String::compareTo));
        assertFalse(isOrdered(List.of("dd", "aa", "cc", "bb"), String::compareTo));
    }

    @Test
    public void testGetSubSequences() {
        assertEquals(List.of(1), getSubSequences(List.of(1), 1, Integer::compareTo));
        assertEquals(List.of(1, 2),getSubSequences(List.of(1, 2), 1, Integer::compareTo));
        assertEquals(List.of(2),getSubSequences(List.of(2, 1), 1, Integer::compareTo));
        assertEquals(List.of(1),getSubSequences(List.of(2, 1), 2, Integer::compareTo));
        assertEquals(List.of(1, 2, 3),getSubSequences(List.of(1, 2, 3, 1, 5, 7, 2, 3, 8), 1, Integer::compareTo));
        assertEquals(List.of(1, 5, 7),getSubSequences(List.of(1, 2, 3, 1, 5, 7, 2, 3, 8), 2, Integer::compareTo));
        assertEquals(List.of(2, 3, 8),getSubSequences(List.of(1, 2, 3, 1, 5, 7, 2, 3, 8), 3, Integer::compareTo));
        assertEquals(List.of(2, 3),getSubSequences(List.of(1, 2, 3, 1, 5, 7, 2, 3, 1), 3, Integer::compareTo));
        assertEquals(List.of(3),getSubSequences(List.of(1, 2, 3, 1, 5, 3, 2, 3, 8), 3, Integer::compareTo));
        assertEquals(List.of(2, 3, 8),getSubSequences(List.of(1, 2, 3, 1, 5, 3, 2, 3, 8), 4, Integer::compareTo));
        assertEquals(List.of(),getSubSequences(List.of(), 4, Integer::compareTo));
        assertEquals(List.of(),getSubSequences(List.of(1, 2, 3, 2, 3, 6), 3, Integer::compareTo));
        assertEquals(List.of(10, 20, 30),getSubSequences(List.of(10, 20, 30, 12, 13, 8, 1, 2, 3 ), 1, Integer::compareTo));
        assertEquals(List.of(12, 13),getSubSequences(List.of(10, 20, 30, 12, 13, 8, 1, 2, 3 ), 2, Integer::compareTo));
        assertEquals(List.of(8),getSubSequences(List.of(10, 20, 30, 12, 13, 8, 1, 2, 3 ), 3, Integer::compareTo));
        assertEquals(List.of(1, 2, 3),getSubSequences(List.of(10, 20, 30, 12, 13, 8, 1, 2, 3 ), 4, Integer::compareTo));
        assertEquals(List.of(),getSubSequences(List.of(10, 20, 30, 12, 13, 8, 1, 2, 3 ), 5, Integer::compareTo));
        assertEquals(List.of("aa", "bb", "cc"),getSubSequences(List.of("aa", "bb", "cc", "aa", "bb", "dd"), 1, String::compareTo));
        assertEquals(List.of("aa", "bb", "dd"),getSubSequences(List.of("aa", "bb", "cc", "aa", "bb", "dd"), 2, String::compareTo));
        assertEquals(List.of("aa"),getSubSequences(List.of("aa", "bb", "cc", "aa", "bb", "aa"), 3, String::compareTo));
        assertEquals(List.of("aa", "bb"),getSubSequences(List.of("aa", "bb", "cc", "aa", "bb", "aa"), 2, String::compareTo));
    }
}
