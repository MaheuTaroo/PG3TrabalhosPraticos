package trab2.grupo1;

import java.io.*;
import java.util.function.*;

public class StreamUtils {

    public static boolean validate( Reader in ) throws IOException {
        final int LOOKING_FOR_CUE = 0, BLOCK_COMMENT = 1, INSIDE_STRING = 2, INSIDE_BRACKETS = 3;
        int state = LOOKING_FOR_CUE, bracketCounter = 0, c;
        boolean escape = false, asterisk = false, startOfComment = false, lineComment = false;

        while ((c = in.read()) != -1) {
            if (lineComment) {
                if (c == '\n') lineComment = false;
                continue;
            }

            if (c == '/') {
                if (startOfComment) {
                    lineComment = true;
                    startOfComment = false;
                }
                else startOfComment = true;
            }

            if (c == '\\')
                escape = !escape;

            switch (state) {
                case INSIDE_STRING:
                    if (c == '"' && !escape) state = LOOKING_FOR_CUE;
                    else continue;
                    break;

                case BLOCK_COMMENT:
                    if (c == '/' && asterisk) state = LOOKING_FOR_CUE;
                    else {
                        asterisk = c == '*';
                        continue;
                    }
                    break;

                case INSIDE_BRACKETS:
                    if (c == '}') {
                        if (--bracketCounter == 0)
                            state = LOOKING_FOR_CUE;
                    }
                    if (c == '{') bracketCounter++;
                    break;

                case LOOKING_FOR_CUE:

                    switch (c) {
                        case '{':
                            bracketCounter++;
                            state = INSIDE_BRACKETS;
                            break;

                        case '}':
                            return false;

                        case '*':
                            if (startOfComment) state = BLOCK_COMMENT;
                            else asterisk = true;
                            break;

                        case '"':
                            if (!escape) state = INSIDE_STRING;
                            break;

                        default:
                            /*if (startOfComment) startOfComment = false;
                            if (asterisk) asterisk = false;
                            if (escape) escape = false;*/
                            startOfComment = asterisk = escape = false;
                    }

                    break;

            }
        }
        return state == LOOKING_FOR_CUE;
    }

    public static void copyCom( BufferedReader in, PrintWriter out ) throws IOException {
        final int LOOKING_FOR_CUE = 0, BLOCK_COMMENT = 1, INSIDE_STRING = 2;
        int state = LOOKING_FOR_CUE, line = 1, character;
        boolean escape = false, asterisk = false, startOfComment = false, lineComment = false;

        while ((character = in.read()) != -1) {
            if (lineComment) {
                out.write(character);
                if (character == '\n') {
                    lineComment = false;
                    line++;
                }
                continue;
            }

            if (character == '\\')
                escape = !escape;

            switch (state) {
                case INSIDE_STRING:
                    if (character == '"' && !escape) state = LOOKING_FOR_CUE;
                    else continue;
                    break;

                case BLOCK_COMMENT:
                    if (character == '/' && asterisk) state = LOOKING_FOR_CUE;
                    else {
                        if (character == '\n') line++;
                        asterisk = character == '*';
                        continue;
                    }
                    break;

                case LOOKING_FOR_CUE:

                    switch (character) {
                        case '/':
                            if (startOfComment) {
                                lineComment = true;
                                out.write(line + " //");
                                //startOfComment = false;
                            }
                            startOfComment = !startOfComment;
                            //else startOfComment = true;
                            break;

                        case '*':
                            if (startOfComment) state = BLOCK_COMMENT;
                            else asterisk = true;
                            break;

                        case '"':
                            if (!escape) state = INSIDE_STRING;
                            break;

                        default:
                            /*if (startOfComment) startOfComment = false;
                            if (asterisk) asterisk = false;
                            if (escape) escape = false;*/
                            startOfComment = asterisk = escape = false;
                    }

                    break;

            }

            if (character == '\n') line++;
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

        int res;
        try {
            String exp = expression.trim();
            res = Integer.parseInt(String.valueOf(exp.charAt(0)));

            for (int i = 1; i < exp.length(); i += 2) {
                if (exp.charAt(i) == '=') break;

                int num = Integer.parseInt(String.valueOf(exp.charAt(i + 1)));
                switch (exp.charAt(i)) {
                    case '+' -> res += num;
                    case '-' -> res -= num;
                    default -> throw new Exception();
                }
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