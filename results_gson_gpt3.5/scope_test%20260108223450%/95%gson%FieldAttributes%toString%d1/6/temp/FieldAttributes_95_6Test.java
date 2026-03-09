package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class FieldAttributes_95_6Test {

    private FieldAttributes fieldAttributes;
    private Field field;

    static class DummyClass {
        private int dummyField;
    }

    @BeforeEach
    void setUp() throws NoSuchFieldException {
        field = DummyClass.class.getDeclaredField("dummyField");
        fieldAttributes = new FieldAttributes(field);
    }

    @Test
    @Timeout(8000)
    void testToString_returnsFieldToString() {
        String expected = field.toString();
        String actual = fieldAttributes.toString();
        assertEquals(expected, actual);
    }
}