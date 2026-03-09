package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_5_5Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader) {
            @Override
            public String readLine() throws IOException {
                return super.readLine();
            }
        };
    }

    @Test
    @Timeout(8000)
    void testReadLine_NonNullNonEmptyLine_Real() throws IOException, NoSuchFieldException, IllegalAccessException {
        java.io.StringReader stringReader = new java.io.StringReader("Hello\nWorld\n");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        String line1 = reader.readLine();
        assertEquals("Hello", line1);

        // Check lastChar field
        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(reader);
        assertEquals('o', lastChar);

        // Check lineCounter field
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(reader);
        assertEquals(1, lineCounter);

        String line2 = reader.readLine();
        assertEquals("World", line2);
        lastChar = (int) lastCharField.get(reader);
        assertEquals('d', lastChar);
        lineCounter = (int) lineCounterField.get(reader);
        assertEquals(2, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NullLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        java.io.StringReader stringReader = new java.io.StringReader("");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        String line = reader.readLine();
        assertNull(line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(reader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(reader);
        assertEquals(0, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NonNullEmptyLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        java.io.StringReader stringReader = new java.io.StringReader("\n");
        ExtendedBufferedReader reader = new ExtendedBufferedReader(stringReader);

        String line = reader.readLine();
        assertEquals("", line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(reader);
        // lastChar should remain UNDEFINED because line length is 0
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(reader);
        assertEquals(1, lineCounter);
    }
}