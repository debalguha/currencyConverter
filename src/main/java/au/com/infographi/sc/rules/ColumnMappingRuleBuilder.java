package au.com.infographi.sc.rules;

public final class ColumnMappingRuleBuilder {
    public String fileType;
    public String sourceColumn;
    public String destinationColumn;
    public Expression testExpression;
    public Expression valueExpression;
    public int ruleIndex;

    private ColumnMappingRuleBuilder() {
    }

    public static ColumnMappingRuleBuilder aColumnMappingRule() {
        return new ColumnMappingRuleBuilder();
    }

    public ColumnMappingRuleBuilder withFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public ColumnMappingRuleBuilder withSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
        return this;
    }

    public ColumnMappingRuleBuilder withDestinationColumn(String destinationColumn) {
        this.destinationColumn = destinationColumn;
        return this;
    }

    public ColumnMappingRuleBuilder withTestExpression(Expression testExpression) {
        this.testExpression = testExpression;
        return this;
    }

    public ColumnMappingRuleBuilder withValueExpression(Expression valueExpression) {
        this.valueExpression = valueExpression;
        return this;
    }

    public ColumnMappingRuleBuilder withRuleIndex(int ruleIndex) {
        this.ruleIndex = ruleIndex;
        return this;
    }

    public ColumnMappingRule build() {
        return new ColumnMappingRule(ruleIndex, fileType, sourceColumn, destinationColumn, testExpression, valueExpression);
    }
}
