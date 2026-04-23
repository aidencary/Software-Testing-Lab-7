package com.baarsch_bytes.Converters;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class LongConstantConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) {
            if ("Long.MAX_VALUE".equals(source)) {
                return Long.MAX_VALUE;
            }
            if ("Long.MIN_VALUE".equals(source)) {
                return Long.MIN_VALUE;
            }
            return Long.parseLong(source.toString());
        }
}


