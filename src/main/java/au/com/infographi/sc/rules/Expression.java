package au.com.infographi.sc.rules;

import java.util.Map;

public interface Expression {
    ExpressionType expressionType();
    String expression();
    <T> T evaluate(Map<String, Object>evaluationContext);
}
