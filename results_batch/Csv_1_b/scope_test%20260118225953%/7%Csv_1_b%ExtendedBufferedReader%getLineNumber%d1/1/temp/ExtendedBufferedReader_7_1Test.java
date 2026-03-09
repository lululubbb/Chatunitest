package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_7_1Test {

    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() throws Exception {
        Reader reader = new StringReader("line1\nline2\nline3");
        extendedBufferedReader = new ExtendedBufferedReader(reader);
        // Use reflection to set lineCounter to 0 explicitly if needed
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        lineCounterField.setInt(extendedBufferedReader, 0);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_initialValue() throws Exception {
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        lineCounterField.setInt(extendedBufferedReader, 0);
        assertEquals(0, extendedBufferedReader.getLineNumber());
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_afterReadingLines() throws Exception {
        extendedBufferedReader.readLine();
        extendedBufferedReader.readLine();
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounterValue = lineCounterField.getInt(extendedBufferedReader);
        assertEquals(lineCounterValue, extendedBufferedReader.getLineNumber());
        assertEquals(2, lineCounterValue);
    }
}