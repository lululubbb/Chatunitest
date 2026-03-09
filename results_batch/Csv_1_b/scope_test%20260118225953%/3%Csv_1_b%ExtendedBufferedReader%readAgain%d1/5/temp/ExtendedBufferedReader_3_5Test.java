package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_3_5Test {

    ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) {
                return -1;
            }
            @Override
            public void close() {
            }
        };
        extendedBufferedReader = new ExtendedBufferedReader(reader);
    }

    @Test
    @Timeout(8000)
    void testReadAgainInitialValue() throws Exception {
        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainAfterSettingLastChar() throws Exception {
        // Using reflection to set private field lastChar
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.setInt(extendedBufferedReader, 'A');

        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals('A', result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainAfterSettingLastCharToEndOfStream() throws Exception {
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.setInt(extendedBufferedReader, ExtendedBufferedReader.END_OF_STREAM);

        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);
        int result = (int) readAgainMethod.invoke(extendedBufferedReader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }
}