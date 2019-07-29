package au.com.infographi.sc.rules;


import com.google.common.base.Strings;

import java.util.regex.Pattern;

public class RegexExpressionEvaluator {
    public static Boolean evaluate(Expression expression, String value) {
        return Strings.isNullOrEmpty(expression.expression()) ? true : Pattern.compile(expression.expression()).matcher(value).matches();
    }

    public static String evaluate(Expression findExpression, Expression replaceExpression, String value) {
        return evaluate(findExpression, value) ?
                value.replaceAll(findExpression.expression(), replaceExpression.expression()) : value;
    }
}
