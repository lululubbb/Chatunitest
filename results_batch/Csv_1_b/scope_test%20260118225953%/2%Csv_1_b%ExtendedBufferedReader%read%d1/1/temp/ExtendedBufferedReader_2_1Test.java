package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_2_1Test {

    private ExtendedBufferedReader extendedBufferedReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsCharAndUpdatesLastCharWithoutNewline() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read() throws IOException {
                int current = 'a';
                try {
                    if (current == '\n') {
                        incrementLineCounter(this);
                    }
                    setLastChar(this, current);
                } catch (Exception e) {
                    throw new IOException(e);
                }
                return current;
            }
        };

        int result = reader.read();
        assertEquals('a', result);
        int lineNumber = getLineNumber(reader);
        assertEquals(0, lineNumber);
        int lastCharValue = getLastChar(reader);
        assertEquals('a', lastCharValue);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsNewlineCharAndIncrementsLineCounter() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read() throws IOException {
                int current = '\n';
                try {
                    if (current == '\n') {
                        incrementLineCounter(this);
                    }
                    setLastChar(this, current);
                } catch (Exception e) {
                    throw new IOException(e);
                }
                return current;
            }
        };

        int result = reader.read();
        assertEquals('\n', result);
        int lineNumber = getLineNumber(reader);
        assertEquals(1, lineNumber);
        int lastCharValue = getLastChar(reader);
        assertEquals('\n', lastCharValue);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsEndOfStream() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read() throws IOException {
                int current = ExtendedBufferedReader.END_OF_STREAM;
                try {
                    if (current == '\n') {
                        incrementLineCounter(this);
                    }
                    setLastChar(this, current);
                } catch (Exception e) {
                    throw new IOException(e);
                }
                return current;
            }
        };

        int result = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        int lineNumber = getLineNumber(reader);
        assertEquals(0, lineNumber);
        int lastCharValue = getLastChar(reader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastCharValue);
    }

    private int getLineNumber(ExtendedBufferedReader reader) throws Exception {
        Method method = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        method.setAccessible(true);
        return (int) method.invoke(reader);
    }

    private int getLastChar(ExtendedBufferedReader reader) throws Exception {
        Field field = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        field.setAccessible(true);
        return (int) field.get(reader);
    }

    private void incrementLineCounter(ExtendedBufferedReader reader) throws Exception {
        Field field = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        field.setAccessible(true);
        int current = (int) field.get(reader);
        field.set(reader, current + 1);
    }

    private void setLastChar(ExtendedBufferedReader reader, int value) throws Exception {
        Field field = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        field.setAccessible(true);
        field.set(reader, value);
    }
}