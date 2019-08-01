package au.com.infographi;

import au.com.infographi.sc.rules.SimpleColumnMapping;
import com.greghaskins.spectrum.Spectrum;
import org.junit.runner.RunWith;

import static com.greghaskins.spectrum.dsl.specification.Specification.describe;
import static com.greghaskins.spectrum.dsl.specification.Specification.it;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Spectrum.class)
public class SanitizerSpec {{

    describe("The sanitizer function ", () -> {
        it("can sanitize ']' but won't sanitize 'DOT' ", () ->  assertThat(SimpleColumnMapping.sanitizer().apply("ABC.gef]")).isEqualTo("ABC.gef"));
        it("can sanitize '_[' but won't sanitize 'DOT' ", () ->  assertThat(SimpleColumnMapping.sanitizer().apply("_[ABC.gef")).isEqualTo("ABC.gef"));
        it("can replace 'DOT' ", () ->  assertThat(SimpleColumnMapping.replaceDotFunction().apply("[ABC.gef")).isEqualTo("[ABCgef"));
        it("can replace 'SPACE' ", () ->  assertThat(SimpleColumnMapping.replaceSpaceFunction().apply("ABC      gef")).isEqualTo("ABCgef"));
        it("can sanitize variable enclosed in [] ", () ->  assertThat(SimpleColumnMapping.sanitizer().apply("_[ABC.gef]")).isEqualTo("ABCgef"));
        it("can sanitize expression ", () ->
                assertThat(SimpleColumnMapping.sanitizer().apply("${!empty _[Benefit Category]}")).isEqualTo("${!empty BenefitCategory}"));
        it("can sanitize expression with multiple variables", () ->
                assertThat(SimpleColumnMapping.sanitizer().apply("${_[Member.FamilyName].concat(',').concat(_[Member.GivenName]).concat(_[Member.OtherGivenName]) * _[Member.BenefitCategoryText]}"))
                        .isEqualTo("${MemberFamilyName.concat(',').concat(MemberGivenName).concat(MemberOtherGivenName) * MemberBenefitCategoryText}"));
    });

}}
