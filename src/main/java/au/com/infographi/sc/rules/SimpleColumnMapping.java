package au.com.infographi.sc.rules;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleColumnMapping {
    static Pattern[]patternsToReplaceWithEmpty = new Pattern[]{Pattern.compile("_?\\["), Pattern.compile("\\]")};
    static Pattern squareParenthesisEncloser = Pattern.compile("[^_]*_\\[([a-zA-Z\\.\\s+]*)\\][^_]*");

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
                s = s.replaceAll(matcher.group(1), replaceDotFunction().compose(replaceSpaceFunction()).apply(matcher.group(1)));
            }
            return s;

        };
    }

    public static Function<String, String> replaceDotFunction() {
        return s -> s.replaceAll("\\.", "");
    }
    public static Function<String, String> replaceSpaceFunction() {
        return s -> s.replaceAll("\\s+", "");
    }
}
