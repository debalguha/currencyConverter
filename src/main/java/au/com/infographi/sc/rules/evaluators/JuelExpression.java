package au.com.infographi.sc.rules.evaluators;

import au.com.infographi.sc.rules.Expression;
import au.com.infographi.sc.rules.ExpressionType;

import java.util.Map;

public class JuelExpression implements Expression {

    public final String expression;

    public JuelExpression(String expression) {
        this.expression = expression;
    }

    public final ExpressionType expressionType = ExpressionType.EXPRESSION_JUEL;


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
