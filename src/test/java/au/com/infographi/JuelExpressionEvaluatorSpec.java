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
            Map<String, Object> dataMap = DataMapBuilder.withHashMapInstance().withKeyAndValue("Employer.ABN", "").build();
            assertThat(JuelExpressionEvaluator.evaluate(new JuelExpression("${empty _[Employer.ABN]}"), dataMap, Boolean.class, SimpleColumnMapping.sanitizer())).isTrue();
        });
    });

    /*@Test
    public void test_Expression() {
        Map<String, Object> dataMap = DataMapBuilder.withHashMapInstance().withKeyAndValue("Employer.ABN", "").build();
        assertEquals("", JuelExpressionEvaluator.evaluate(new JuelExpression(""), dataMap, String.class));
        assertEquals(Boolean.TRUE, JuelExpressionEvaluator.evaluate(new JuelExpression("${empty _[Employer.ABN]}"), dataMap, Boolean.class));
    }

    @Test
    public void test_ExpressionReturningValue() {
        Map<String, Object> dataMap = DataMapBuilder.withHashMapInstance().withKeyAndValue("Member.GivenName", "Debal")
                .withKeyAndValue("Member.FamilyName", "Guha").build();
        assertEquals("Guha,Debal", JuelExpressionEvaluator.evaluate(new JuelExpression("${_[Member.FamilyName].concat(',').concat(_[Member.GivenName])}"), dataMap, String.class));
    }*/
}}
