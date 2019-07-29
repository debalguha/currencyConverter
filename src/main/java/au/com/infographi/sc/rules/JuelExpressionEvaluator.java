package au.com.infographi.sc.rules;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.Map;
import java.util.function.Function;

public class JuelExpressionEvaluator {

    public static <T> T evaluate(Expression expression, Map<String, Object> ruleContext, Class<T> clazz, Function<String, String> columnSanitizer) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext ctx = buildContextFrom(ruleContext, factory, columnSanitizer);
        return (T) factory.createValueExpression(ctx, columnSanitizer.apply(expression.expression()), clazz).getValue(ctx);
    }

    private static SimpleContext buildContextFrom(Map<String, Object> ruleContext, ExpressionFactory factory, Function<String, String> columnSanitizer) {
        SimpleContext ctx = new SimpleContext();
        ruleContext.entrySet().stream().forEach((stringObjectEntry -> ctx.setVariable(columnSanitizer.apply(stringObjectEntry.getKey()), createValueExpression(stringObjectEntry.getValue(), factory))));
        return ctx;
    }

    private static ValueExpression createValueExpression(Object value, ExpressionFactory factory) {
        return factory.createValueExpression(value, value.getClass());
    }
}
