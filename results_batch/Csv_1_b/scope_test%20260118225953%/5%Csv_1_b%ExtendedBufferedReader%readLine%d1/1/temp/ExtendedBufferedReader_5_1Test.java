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

class ExtendedBufferedReader_5_1Test {

    private ExtendedBufferedReader extendedBufferedReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NonNullNonEmptyLine() throws Exception {
        Reader readerWithHello = new Reader() {
            private boolean returned = false;

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                if (returned) {
                    return -1;
                }
                String s = "hello\n";
                int length = Math.min(len, s.length());
                s.getChars(0, length, cbuf, off);
                returned = true;
                return length;
            }

            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader reader3 = new ExtendedBufferedReader(readerWithHello);

        String line = reader3.readLine();

        assertEquals("hello", line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(reader3);
        assertEquals('o', lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(reader3);
        assertEquals(1, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NonNullEmptyLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader readerWithEmptyLine = new Reader() {
            private boolean returned = false;

            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                if (returned) {
                    return -1;
                }
                String s = "\n";
                int length = Math.min(len, s.length());
                s.getChars(0, length, cbuf, off);
                returned = true;
                return length;
            }

            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader reader = new ExtendedBufferedReader(readerWithEmptyLine);

        String line = reader.readLine();

        assertEquals("", line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(reader);
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(reader);
        assertEquals(1, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NullLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader emptyReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return -1;
            }

            @Override
            public void close() throws IOException {}
        };
        ExtendedBufferedReader reader = new ExtendedBufferedReader(emptyReader);

        String line = reader.readLine();

        assertNull(line);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = lastCharField.getInt(reader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = lineCounterField.getInt(reader);
        assertEquals(0, lineCounter);
    }
}