package au.com.infographi.sc.rules;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleColumnMapping {
    static Pattern[]patternsToReplaceWithEmpty = new Pattern[]{Pattern.compile("\\["), Pattern.compile("\\]"), Pattern.compile("_")};
    static Pattern squareParenthesisEncloser = Pattern.compile("\\[.*\\.\\]");
    static Pattern replaceDotPattern = Pattern.compile("\\.");

    public static Function<String, String> sanitizer() {
        // Sanitizer composition is not associative!!
        return Arrays.stream(patternsToReplaceWithEmpty).map(pattern -> replaceMatchingPatternWithEmptyString(pattern)).reduce((func1, func2) -> func1.andThen(func2)).get()
                .compose(replaceEnclosingSquareBrackets().andThen(replaceDotFunction()));
    }

    public static Function<String, String> replaceMatchingPatternWithEmptyString(Pattern pattern) {
        return s -> pattern.matcher(s).replaceAll("");
    }

    public static Function<String, String> replaceEnclosingSquareBrackets() {
        return s -> {
            Matcher matcher = squareParenthesisEncloser.matcher(s);
            return matcher.matches() ? matcher.group(1) : s;

        };
    }

    public static Function<String, String> replaceDotFunction() {
        return s -> {
            Matcher matcher = replaceDotPattern.matcher(s);
            return matcher.matches() ? matcher.replaceAll("") : s;
        };
    }
}
