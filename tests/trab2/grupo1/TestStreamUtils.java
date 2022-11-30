package trab2.grupo1;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static trab2.grupo1.StreamUtils.*;

public class TestStreamUtils {
    public final String code1 = "package test;\n\n" +
            "// classe de teste\n" +
            "public class Test {\n\n" +
            "/* método\n" +
            "* principal */\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\t\tString text = \"this is a test string // and this is a test comment\";\n\n" +
            "\t\t// envia o texto para o ecrã\n" +
            "\t\tSystem.out.println(text);\n" +
            "\t}\n" +
            "}", code2 = "package test;\n\n" +
            "public class TestTester {\n" +
            "\tpublic static void main(String[] args) {}\n" +
            "\t\tSystem.out.println(\"This is a test that tests a test class\");" +
            "\t}" +
            "}";

    @Test
    public void TestValidate() {
        try {
            assertThrows(IOException.class, () -> validate(new FileReader("")));

            assertTrue(validate(new StringReader(code1)));

            assertFalse(validate(new StringReader(code2)));

            String code3 = "//\t\t     \t       \" {        ";
            assertTrue(validate(new StringReader(code3)));

            code3 = "/*  {     */";
            assertTrue(validate(new StringReader(code3)));

            code3 += "asdfasdfghjkghjklço// {}}\n{}{";
            assertFalse(validate(new StringReader(code3)));

            code3 = "{}{//}\n}";
            assertTrue(validate(new StringReader(code3)));
        }
        catch (IOException ignored) {

        }
    }

    @Test
    public void TestExpression() {
        String expression = "";
        assertNull(evaluate(expression));

        expression = "4";
        assertNull(evaluate(expression));

        expression += '=';
        assertEquals(4, evaluate(expression));

        expression = "6+5=";
        assertEquals(11, evaluate(expression));

        expression = '1' + expression;
        assertNull(evaluate(expression));

        expression = "6*5=";
        assertNull(evaluate(expression));
    }

    @Test
    public void TestCopyCom() {
        String expected = "3 // classe de teste\n11 // envia o texto para o ecrã\n";

        assertDoesNotThrow(() -> copyCom(new BufferedReader(new StringReader(code1)), new PrintWriter(System.out)));

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            copyCom(new BufferedReader(new StringReader(code1)), new PrintWriter(baos));

            assertEquals(expected, baos.toString());

            baos.reset();

            copyCom(new BufferedReader(new StringReader(code2)), new PrintWriter(baos));

            assertEquals("", baos.toString());
        }
        catch (Exception ignored) {

        }
    }

    @Test
    public void TestStringExpression() {
        String test = "6+5\n9+6=\n4=\n23*5=\n4*8=\n=\n55=", expected = "6+5 ERROR\n9+6=15\n4=4\n23*5= ERROR\n4*8= ERROR\n= ERROR\n55= ERROR\n";
        assertEquals(expected, stringEvaluate(test));
    }
}
