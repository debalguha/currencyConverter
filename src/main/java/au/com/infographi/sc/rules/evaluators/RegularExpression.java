package au.com.infographi.sc.rules.evaluators;

import au.com.infographi.sc.rules.Expression;
import au.com.infographi.sc.rules.ExpressionType;

import java.util.Map;
import java.util.Objects;

public class RegularExpression implements Expression {

    public final String expression;
    public final ExpressionType expressionType = ExpressionType.EXPRESSION_REGEX;

    public RegularExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegularExpression that = (RegularExpression) o;
        return Objects.equals(expression, that.expression) &&
                expressionType == that.expressionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, expressionType);
    }

    @Override
    public ExpressionType expressionType() {
        return expressionType;
    }

    @Override
    public String expression() {
        return expression;
    }

    @Override
    public <T> T evaluate(Map<String, Object> evaluationContext) {
        return null;
    }
}
