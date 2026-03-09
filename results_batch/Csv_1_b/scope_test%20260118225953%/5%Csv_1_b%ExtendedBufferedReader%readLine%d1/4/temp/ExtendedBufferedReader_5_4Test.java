package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_5_4Test {

    @Test
    @Timeout(8000)
    void testReadLine_withNonEmptyLine() throws Exception {
        java.io.StringReader stringReader = new java.io.StringReader("Hello World\n");
        ExtendedBufferedReader realReader = new ExtendedBufferedReader(stringReader);

        String result = realReader.readLine();

        assertEquals("Hello World", result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(realReader);
        assertEquals('d', lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(realReader);
        assertEquals(1, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_withEmptyLine() throws Exception {
        java.io.StringReader stringReader = new java.io.StringReader("\n");
        ExtendedBufferedReader realReader = new ExtendedBufferedReader(stringReader);

        String result = realReader.readLine();

        assertEquals("", result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(realReader);
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(realReader);
        assertEquals(1, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_withNullLine() throws Exception {
        java.io.StringReader stringReader = new java.io.StringReader("");
        ExtendedBufferedReader realReader = new ExtendedBufferedReader(stringReader);

        String line = realReader.readLine();

        assertNull(line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(realReader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(realReader);
        assertEquals(0, lineCounter);
    }
}