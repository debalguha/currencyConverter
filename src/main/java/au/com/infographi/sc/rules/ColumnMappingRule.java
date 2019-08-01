package au.com.infographi.sc.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static au.com.infographi.sc.rules.SimpleColumnMapping.sanitizer;

public class ColumnMappingRule implements TransformationRule {
    public final String fileType;
    public final String sourceColumn;
    public final String destinationColumn;
    public final Expression testExpression;
    public final Expression valueExpression;
    public final int ruleIndex;
    private final Function<Map<String, Object>, Map<String, Object>> internalFunc;

    public ColumnMappingRule(int ruleIndex, String fileType, String sourceColumn, String destinationColumn, Expression testExpression, Expression valueExpression) {
        this.ruleIndex = ruleIndex;
        this.fileType = fileType;
        this.sourceColumn = sourceColumn;
        this.destinationColumn = destinationColumn;
        this.testExpression = testExpression;
        this.valueExpression = valueExpression;
        this.internalFunc = inputMap -> {
            Map<String, Object> output = new HashMap<>(inputMap);
            if (Objects.nonNull(testExpression)) {
                if (testExpression(testExpression, output)) {
                    if (Optional.ofNullable(valueExpression).isPresent()) {
                        output.put(destinationColumn, evaluate(valueExpression, output));
                    } else {
                        output.put(destinationColumn, inputMap.get(this.sourceColumn));
                    }
                }
            }
            return output;
        };
    }

    public String getFileType() {
        return fileType;
    }

    public String getSourceColumn() {
        return sourceColumn;
    }

    public String getDestinationColumn() {
        return destinationColumn;
    }

    public Expression getTestExpression() {
        return testExpression;
    }

    public Expression getValueExpression() {
        return valueExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnMappingRule that = (ColumnMappingRule) o;
        return ruleIndex == that.ruleIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruleIndex);
    }

    @Override
    public Map<String, Object> apply(Map<String, Object> inputMap) {
        return this.internalFunc.apply(inputMap);
    }

    private Object evaluate(Expression valueExpression, Map<String, Object> output) {
        output.remove(destinationColumn);
        return valueExpression.expressionType() == ExpressionType.EXPRESSION_JUEL ? JuelExpressionEvaluator.evaluate(valueExpression, output, Object.class, sanitizer())
                : RegexExpressionEvaluator.evaluate(valueExpression, Optional.ofNullable(output.get(sourceColumn)).orElse("").toString());
    }

    private boolean testExpression(Expression testExpression, Map<String, Object> evaluationContext) {
        if (!Optional.ofNullable(testExpression).isPresent() || "TRUE".equalsIgnoreCase(testExpression.expression())) {
            return true;
        }
        Object sourceValue = Optional.ofNullable(evaluationContext.get(sourceColumn)).orElse("");
        return testExpression.expressionType() == ExpressionType.EXPRESSION_REGEX ? RegexExpressionEvaluator.evaluate(testExpression, sourceValue.toString())
                : JuelExpressionEvaluator.evaluate(testExpression, evaluationContext, Boolean.class, sanitizer());
    }

    @Override
    public <V> Function<V, Map<String, Object>> compose(Function<? super V, ? extends Map<String, Object>> before) {
        return this.internalFunc.compose(before);
    }

    @Override
    public <V> Function<Map<String, Object>, V> andThen(Function<? super Map<String, Object>, ? extends V> after) {
        return this.internalFunc.andThen(after);
    }

}
