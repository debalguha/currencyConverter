package com.example.fx.conversion;

import com.example.fx.FXEntry;
import com.example.fx.Pair;

import java.util.Optional;
import java.util.function.Function;

public class ConverterImpl implements Converter {
    private final Pair<String, String> fxPair;
    private final Optional<String> unresolvedFactorOrValue;
    private final Optional<Function<Double, Double>> converterFunction;

    public ConverterImpl(Pair<String, String> fxPair, Function<Double, Double> converterFunction) {
        this.fxPair = fxPair;
        this.converterFunction = Optional.of(converterFunction);
        this.unresolvedFactorOrValue = Optional.empty();
    }

    public ConverterImpl(FXEntry fxEntry){
        this.fxPair = Pair.fromFXEntry(fxEntry);
        this.converterFunction = Optional.empty();
        this.unresolvedFactorOrValue = Optional.of(fxEntry.getConversionEntry());
    }

    public ConverterImpl(Pair<String, String> fxPair) {
        this.fxPair = fxPair;
        this.converterFunction = Optional.empty();
        this.unresolvedFactorOrValue = Optional.empty();
    }

    @Override
    public Pair<String, String> getPair() {
        return fxPair;
    }

    @Override
    public Optional<Function<Double, Double>> getConverterFunction() {
        return this.converterFunction;
    }

    @Override
    public Double convert(Double from) {
        if(converterFunction.isPresent()) {
            return converterFunction.get().apply(from);
        }
        StringBuilder builder = new StringBuilder("Unable to built a converter function from reference data as [");
        builder.append("From Currency: ").append(fxPair._1()).append(", To Currency: ").append(fxPair._2()).append(", with Factor: ").append(unresolvedFactorOrValue.get());
        throw new ConverterResolutionException(new FXEntry(fxPair._1(), fxPair._2(), unresolvedFactorOrValue.get()), builder.toString());
    }

    @Override
    public Optional<String> getUnresolvedFactorOrValue() {
        return unresolvedFactorOrValue;
    }
}
