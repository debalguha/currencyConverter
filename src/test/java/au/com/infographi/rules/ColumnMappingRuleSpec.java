package au.com.infographi.rules;

import au.com.infographi.sc.DataMapBuilder;
import au.com.infographi.sc.rules.ColumnMappingRule;
import au.com.infographi.sc.rules.ColumnMappingRuleBuilder;
import au.com.infographi.sc.rules.evaluators.JuelExpression;
import com.greghaskins.spectrum.Spectrum;
import com.greghaskins.spectrum.Variable;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.greghaskins.spectrum.dsl.gherkin.Gherkin.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Spectrum.class)
public class ColumnMappingRuleSpec {{
    feature("Mapping Rule", () -> {
        scenario("1. One source to one dest", () -> {
            Variable<ColumnMappingRule> ruleVar = new Variable<>();
            Variable<Map<String, Object>> contextVar = new Variable<>();
            Variable<Map<String, Object>> outVar = new Variable<>();
            given("A column mapping rule, ", () -> {
                ruleVar.set(ColumnMappingRuleBuilder.aColumnMappingRule().withRuleIndex(0).withDestinationColumn("MemberRegistration.BenefitCategoryText")
                        .withFileType("sample").withSourceColumn("Benefit Category").withTestExpression(new JuelExpression("${!empty _[Benefit Category]}"))
                        .build());
            });
            and("in an execution context ", () -> {
                contextVar.set(DataMapBuilder.withHashMapInstance().withKeyAndValue("Benefit Category", "Value").build());
            });
            when("when executed ", () -> {
                outVar.set(ruleVar.get().apply(contextVar.get()));
            });
            then("output contins mapped column with value ", () -> {
                assertThat(outVar.get().containsKey(ruleVar.get().getDestinationColumn()));
                assertThat(outVar.get().get(ruleVar.get().getDestinationColumn())).isEqualTo("Value");
            });
        });

    });
}}
