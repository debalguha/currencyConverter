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
        it("can sanitize ']' ", () ->  assertThat(SimpleColumnMapping.sanitizer().apply("ABC.gef]")).isEqualTo("ABC.gef"));
        it("can sanitize '[' ", () ->  assertThat(SimpleColumnMapping.sanitizer().apply("[ABC.gef")).isEqualTo("ABC.gef"));
        it("can replace 'DOT' ", () ->  assertThat(SimpleColumnMapping.replaceDotFunction().apply("[ABC.gef")).isEqualTo("[ABCgef"));
        it("can sanitize variable enclosed in [] ", () ->  assertThat(SimpleColumnMapping.sanitizer().apply("_[ABC.gef]")).isEqualTo("ABCgef"));
    });

}}
