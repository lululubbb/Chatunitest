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

class ExtendedBufferedReader_2_3Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsCharNotNewline() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read() throws IOException {
                int current = 'a';
                if (current == '\n') {
                    incrementLineCounter(this);
                }
                setLastChar(this, current);
                return current;
            }
        };

        int result = reader.read();

        assertEquals('a', result);
        int lineNumber = getField(reader, "lineCounter");
        assertEquals(0, lineNumber);
        int lastChar = getField(reader, "lastChar");
        assertEquals('a', lastChar);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsNewlineIncrementsLineCounter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read() throws IOException {
                int current = '\n';
                if (current == '\n') {
                    incrementLineCounter(this);
                }
                setLastChar(this, current);
                return current;
            }
        };

        int result = reader.read();

        assertEquals('\n', result);
        int lineNumber = getField(reader, "lineCounter");
        assertEquals(1, lineNumber);
        int lastChar = getField(reader, "lastChar");
        assertEquals('\n', lastChar);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read() throws IOException {
                int current = ExtendedBufferedReader.END_OF_STREAM;
                if (current == '\n') {
                    incrementLineCounter(this);
                }
                setLastChar(this, current);
                return current;
            }
        };

        int result = reader.read();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        int lineNumber = getField(reader, "lineCounter");
        assertEquals(0, lineNumber);
        int lastChar = getField(reader, "lastChar");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object obj, String fieldName) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void incrementLineCounter(ExtendedBufferedReader reader) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
            field.setAccessible(true);
            int current = field.getInt(reader);
            field.setInt(reader, current + 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setLastChar(ExtendedBufferedReader reader, int value) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lastChar");
            field.setAccessible(true);
            field.setInt(reader, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}