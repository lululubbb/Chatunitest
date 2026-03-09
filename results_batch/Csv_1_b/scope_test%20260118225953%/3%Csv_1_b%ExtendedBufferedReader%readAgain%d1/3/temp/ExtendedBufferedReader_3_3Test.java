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

class ExtendedBufferedReader_3_3Test {

    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        Reader mockReader = Mockito.mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadAgain_returnsLastChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Using reflection to set private field lastChar
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);

        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);

        // Test case 1: lastChar = UNDEFINED (-2)
        lastCharField.setInt(extendedBufferedReader, ExtendedBufferedReader.UNDEFINED);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);

        // Test case 2: lastChar = END_OF_STREAM (-1)
        lastCharField.setInt(extendedBufferedReader, ExtendedBufferedReader.END_OF_STREAM);
        result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);

        // Test case 3: lastChar = arbitrary positive value (e.g., 65)
        lastCharField.setInt(extendedBufferedReader, 65);
        result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals(65, result);
    }
}