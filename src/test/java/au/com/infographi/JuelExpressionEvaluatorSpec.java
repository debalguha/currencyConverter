package au.com.infographi;

import au.com.infographi.sc.DataMapBuilder;
import au.com.infographi.sc.rules.JuelExpressionEvaluator;
import au.com.infographi.sc.rules.SimpleColumnMapping;
import au.com.infographi.sc.rules.evaluators.JuelExpression;
import com.greghaskins.spectrum.Spectrum;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.greghaskins.spectrum.dsl.specification.Specification.describe;
import static com.greghaskins.spectrum.dsl.specification.Specification.it;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Spectrum.class)
public class JuelExpressionEvaluatorSpec {{

    describe("The juel evaluator ", () -> {
        it("can test an expression if true or false ", () -> {
            Map<String, Object> dataMap = DataMapBuilder.withHashMapInstance().withKeyAndValue("EmployerABN", "").build();
            assertThat(JuelExpressionEvaluator.evaluate(new JuelExpression("${empty _[Employer.ABN]}"), dataMap, Boolean.class, SimpleColumnMapping.newSanitizer())).isTrue();
        });
        it("Can evaluate an expression's value in relation to the given context", () -> {
            Map<String, Object> dataMap = DataMapBuilder.withHashMapInstance()
                    .withKeyAndValue("EmployerABN", "123").withKeyAndValue("var1", "4.32").withKeyAndValue("var2", "2.32")
                    .withKeyAndValue("var3", "").build();
            assertThat(JuelExpressionEvaluator.evaluate(new JuelExpression("${_[var1] + _[var2]}"), dataMap, Double.class, SimpleColumnMapping.newSanitizer())).isBetween(6.64d, 6.641d);
        });
    });

}}
