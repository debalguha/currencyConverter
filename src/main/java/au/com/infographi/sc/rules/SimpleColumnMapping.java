package au.com.infographi.sc.rules;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleColumnMapping {
    static Pattern[]patternsToReplaceWithEmpty = new Pattern[]{Pattern.compile("_?\\["), Pattern.compile("\\]")};
    static String[] stringPatternsToReplaceWithEmpty = new String[]{"_?\\[", "\\]", "\\.", "\\s+"};
    static Pattern squareParenthesisEncloser = Pattern.compile("[^_]*(_\\[)([a-zA-Z\\.\\s+0-9]*)(\\])[^_]*");

    @Deprecated
    public static Function<String, String> sanitizer() {
        // Sanitizer composition is not associative!!
        return Arrays.stream(patternsToReplaceWithEmpty).map(pattern -> replaceMatchingPatternWithEmptyString(pattern)).reduce((func1, func2) -> func1.andThen(func2)).get()
                .compose(sanitizeVariableEnclosedInSquareBrackets());
    }

    public static Function<String, String> replaceMatchingPatternWithEmptyString(Pattern pattern) {
        return s -> pattern.matcher(s).replaceAll("");
    }

    public static Function<String, String> sanitizeVariableEnclosedInSquareBrackets() {
        return s -> {
            Matcher matcher = squareParenthesisEncloser.matcher(s);
            while(matcher.find()){
                String variableName = replaceDotFunction().andThen(replaceSpaceFunction()).apply(matcher.group(2));
                s = findAndReplaceFirst(matcher.group(1), "").andThen(findAndReplaceFirst(matcher.group(2), variableName))
                        .andThen(findAndReplaceFirst(matcher.group(3), "")).apply(s);
                //s = s.replaceAll(matcher.group(1), "").replaceAll(matcher.group(2), );
            }
            return s;

        };
    }

    public static Function<String, String> newSanitizer() {
        return s -> {
            Matcher matcher = squareParenthesisEncloser.matcher(s);
            StringBuilder builder = new StringBuilder();
            while(matcher.find()){
                String variableName = replaceDotFunction().andThen(replaceSpaceFunction()).apply(matcher.group(2));
                builder.append(findAndReplaceFirst(matcher.group(1), "").andThen(findAndReplaceFirst(matcher.group(2), variableName))
                        .andThen(findAndReplaceFirst(matcher.group(3), "")).apply(matcher.group(0)));
                //s = s.replaceAll(matcher.group(1), "").replaceAll(matcher.group(2), );
            }
            return builder.toString().isEmpty() ? s : builder.toString();

        };
    }

    public static Function<String, String> findAndReplaceFirst(String pattern, String replacement) {
        return s -> s.replaceFirst(escapeIfRequired(pattern), replacement);
    }

    private static String escapeIfRequired(String pattern) {
        return pattern.contains("[")  ? pattern.replaceAll("\\[", "\\\\[") : pattern.contains("]") ? pattern.replaceAll("\\]", "\\\\]") : pattern;
    }

    public static Function<String, String> replaceDotFunction() {
        return s -> s.replaceAll("\\.", "");
    }
    public static Function<String, String> replaceSpaceFunction() {
        return s -> s.replaceAll("\\s+", "");
    }

}
