package au.com.infographi;

import au.com.infographi.sc.rules.SimpleColumnMapping;
import com.greghaskins.spectrum.Spectrum;
import org.junit.runner.RunWith;

import static com.greghaskins.spectrum.dsl.specification.Specification.describe;
import static com.greghaskins.spectrum.dsl.specification.Specification.it;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Spectrum.class)
public class SanitizerSpec {{

    /*describe("The sanitizer function ", () -> {
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
    });*/

    describe("The New sanitizer function ", () -> {
        it("does not sanitize only ']' ", () ->  assertThat(SimpleColumnMapping.newSanitizer().apply("ABC.gef]")).isEqualTo("ABC.gef]"));
        it("does not sanitize only '[' ", () ->  assertThat(SimpleColumnMapping.newSanitizer().apply("_[ABC.gef")).isEqualTo("_[ABC.gef"));
        it("does not sanitize only _[' ", () ->  assertThat(SimpleColumnMapping.newSanitizer().apply("ABC_gef")).isEqualTo("ABC_gef"));
        it("can remove 'DOT' from variable enclosed in [] ", () ->  assertThat(SimpleColumnMapping.newSanitizer().apply("_[ABC.gef]")).isEqualTo("ABCgef"));
        it("can remove 'SPACE' from variable enclosed in [] ", () ->  assertThat(SimpleColumnMapping.newSanitizer().apply("_[ABC gef]")).isEqualTo("ABCgef"));

        it("can sanitize expression ", () ->
                assertThat(SimpleColumnMapping.newSanitizer().apply("${!empty _[Benefit Category]}")).isEqualTo("${!empty BenefitCategory}"));
        it("can sanitize expression preserving '[' and ']' in other places ", () ->
                assertThat(SimpleColumnMapping.newSanitizer().apply("${!empty _[Benefit Category][0]}")).isEqualTo("${!empty BenefitCategory[0]}"));
        it("can sanitize expression with multiple variables", () ->
                assertThat(SimpleColumnMapping.newSanitizer().apply("${_[Member.FamilyName].concat(',').concat(_[Member.GivenName]).concat(_[Member.OtherGivenName]) * _[Member.BenefitCategoryText]}"))
                        .isEqualTo("${MemberFamilyName.concat(',').concat(MemberGivenName).concat(MemberOtherGivenName) * MemberBenefitCategoryText}"));
        it("can sanitize expression with multiple variables", () ->
                assertThat(SimpleColumnMapping.newSanitizer().apply("${(fn:upper ( _[Payee.USI]) == '60905115063001' or fn:upper ( _[Payee.USI]) == '60905115063002') and ( empty _[MemberRegistration.EmploymentStatusCode])}"))
                        .isEqualTo("${(fn:upper ( PayeeUSI) == '60905115063001' or fn:upper ( PayeeUSI) == '60905115063002') and ( empty MemberRegistrationEmploymentStatusCode)}"));
    });

}}
