package trab2.grupo1;

import java.io.*;
import java.util.Arrays;
import java.util.function.*;
import java.util.regex.Pattern;

public class StreamUtils {

    public static boolean validate( Reader in ) throws IOException {
        int bracketCounter = 0, quoteCounter = 0, index = 0, blockCom = -1, c;
        char[] buffer = new char[512];
        while ((c = in.read(buffer)) != -1) {
            if (blockCom == -1) blockCom = Arrays.toString(buffer).indexOf("/*");

            for (int i = 0; i < c; i++) {

                if (blockCom > -1) {
                    if (!Arrays.toString(buffer).contains("*/")) break;
                    else {
                        i = Arrays.toString(buffer).indexOf("*/") + 1;
                        blockCom = -1;
                        continue;
                    }
                }

                if (Arrays.toString(buffer).contains("//")) break;

                if (buffer[i] == '"' && (i == 0 || buffer[i - 1] != '\\')) quoteCounter++;

                if (quoteCounter == 0) {
                    if (buffer[i] == '{') bracketCounter++;
                    else if (buffer[i] == '}') {
                        bracketCounter--;
                        if (bracketCounter < 0)
                            return false;
                    }
                }
            }
        }
        return bracketCounter == 0;
    }

    public static void copyCom( BufferedReader in, PrintWriter out ) throws IOException {
        int LOOKING_FOR_CUE = 0, BLOCK_COMMENT = 1, INSIDE_STRING = 2, state = LOOKING_FOR_CUE;

        int index = 0, line = 1, quoteIndex = -1;
        //boolean blockCom = false;
        String s;
        while ((s = in.readLine()) != null) {
            if (s.contains("/*"))
                if (state != INSIDE_STRING)
                    state = BLOCK_COMMENT;

            if (s.contains("\""))
                if (state != BLOCK_COMMENT)
                    state = INSIDE_STRING;

            if (state == BLOCK_COMMENT) {
                if (s.contains("*/")) {
                    if (s.contains("/*") && s.lastIndexOf("/*") < s.lastIndexOf("*/"))
                        state = LOOKING_FOR_CUE;
                }
            }
            else {
                if (state != INSIDE_STRING) {
                    if (s.contains("/*"))
                        state = BLOCK_COMMENT;
                    if (s.split(Pattern.quote("\"")).length % 2 == 0) state = LOOKING_FOR_CUE;
                }

            }

            if ((quoteIndex = s.indexOf("\"")) > -1) {
                if (state < BLOCK_COMMENT) state = INSIDE_STRING;
            }

            if ((index = s.lastIndexOf("//")) != -1 && state == LOOKING_FOR_CUE) {
                String temp = s.substring(0, index).trim();
                int quoteCounter = 0;
                for (int i = 0; i < temp.length(); i++)
                    if (temp.charAt(i) == '"') quoteCounter++;

                if (quoteCounter % 2 == 1) {
                    line++;
                    continue;
                }

                out.write(line + " " + s.substring(index).trim() + "\n");
            }

            line++;
        }
        out.flush();
    }

    public static <T> void mapper( BufferedReader in, Function<String, T> mapping, BiConsumer<String, T> action ) throws IOException {
        String s;
        while ((s = in.readLine()) != null)
            action.accept(s, mapping.apply(s));
    }

    public static Integer evaluate(String expression) {
        if (!expression.endsWith("=") || expression.length() % 2 == 1) return null;

        int res = 0;
        try {
            String exp = expression.trim();
            res = Integer.parseInt(exp.substring(0, 1));
            exp = exp.substring(1);

            while (exp.length() > 0) {
                if (exp.charAt(0) == '=') break;
                switch (exp.charAt(0)) {
                    case '+' -> res += Integer.parseInt(exp.substring(1, 2));
                    case '-' -> res -= Integer.parseInt(exp.substring(1, 2));
                    default -> throw new Exception();
                }
                exp = exp.substring(2);
            }
        }
        catch (Exception e) {
            return null;
        }
        return res;
    }

    public static void evaluate(String filenameIn, BiConsumer<String,Integer> action) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filenameIn))) {
            mapper(br, StreamUtils::evaluate, action);
        }
    }

    public static void copyEvaluate(String filenameIn, String filenameOut) throws IOException {
        try (PrintWriter pw = new PrintWriter(filenameOut)) {
            evaluate(filenameIn, (s, i) -> pw.println(s + (i == null ? " ERROR" : i)));
        }
    }

    public static void copyEvaluate(BufferedReader in, Writer out) throws IOException {
        mapper(in, StreamUtils::evaluate, (s, i) -> {
            try {
                out.write(s + (i == null ? " ERROR" : i) + "\n");
            }
            catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }

    public static String stringEvaluate( String expression ) {
        StringWriter sw = new StringWriter();
        try {
            copyEvaluate(new BufferedReader(new StringReader(expression)), sw);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}