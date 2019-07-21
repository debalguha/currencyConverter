package com.example.fx.conversion;

import com.example.fx.Pair;

import java.util.Optional;
import java.util.function.Function;

public interface Converter {
    Pair<String, String> getPair();
    Optional<Function<Double, Double>> getConverterFunction();
    Double convert(Double from);

    Optional<String> getUnresolvedFactorOrValue();
}
