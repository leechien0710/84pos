package com.example.facebookinteration.convert;

public interface Converter<S, T> {
    T convert(S source);
}
