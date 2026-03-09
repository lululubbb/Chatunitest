package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_3_2Test {

    ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        Reader mockReader = Mockito.mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadAgainInitialValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Access private field lastChar and set to UNDEFINED
        setLastCharField(ExtendedBufferedReader.UNDEFINED);
        int result = invokeReadAgain();
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainWithCustomValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int testValue = 42;
        setLastCharField(testValue);
        int result = invokeReadAgain();
        assertEquals(testValue, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainWithEndOfStream() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setLastCharField(ExtendedBufferedReader.END_OF_STREAM);
        int result = invokeReadAgain();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    private void setLastCharField(int value) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lastChar");
            field.setAccessible(true);
            field.setInt(extendedBufferedReader, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private int invokeReadAgain() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        method.setAccessible(true);
        return (int) method.invoke(extendedBufferedReader);
    }
}