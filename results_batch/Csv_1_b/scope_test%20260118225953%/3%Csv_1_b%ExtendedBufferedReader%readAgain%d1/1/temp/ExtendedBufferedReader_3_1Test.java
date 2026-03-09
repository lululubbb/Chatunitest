package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_3_1Test {

    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        Reader dummyReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return -1;
            }
            @Override
            public void close() throws IOException {
            }
        };
        extendedBufferedReader = new ExtendedBufferedReader(dummyReader);
    }

    @Test
    @Timeout(8000)
    void testReadAgain_withDefaultLastChar() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Access private field lastChar and set it to UNDEFINED (-2)
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.setInt(extendedBufferedReader, ExtendedBufferedReader.UNDEFINED);

        // Access package-private method readAgain via reflection
        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);

        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgain_withEndOfStreamLastChar() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Set lastChar to END_OF_STREAM (-1)
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.setInt(extendedBufferedReader, ExtendedBufferedReader.END_OF_STREAM);

        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgain_withPositiveLastChar() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Set lastChar to a positive value, e.g. 'a' (97)
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.setInt(extendedBufferedReader, 97);

        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);

        assertEquals(97, result);
    }
}