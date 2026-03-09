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

class ExtendedBufferedReader_7_5Test {

    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        Reader reader = new StringReader("line1\nline2\nline3");
        extendedBufferedReader = new ExtendedBufferedReader(reader);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_initialValue() throws Exception {
        // Use reflection to set the private field lineCounter to 0 before testing
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        lineCounterField.setInt(extendedBufferedReader, 0);

        int lineNumber = extendedBufferedReader.getLineNumber();
        assertEquals(0, lineNumber);
    }
}