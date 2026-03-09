package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class ExtendedBufferedReader_2_5Test {

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
        when(mockReader.read()).thenReturn((int) 'a');

        int result = extendedBufferedReader.read();

        assertEquals('a', result);
        assertEquals('a', getLastChar(extendedBufferedReader));
        assertEquals(0, getLineCounter(extendedBufferedReader));
    }

    @Test
    @Timeout(8000)
    void testReadReturnsNewlineIncrementsLineCounter() throws IOException {
        when(mockReader.read()).thenReturn((int) '\n');

        int result = extendedBufferedReader.read();

        assertEquals('\n', result);
        assertEquals('\n', getLastChar(extendedBufferedReader));
        assertEquals(1, getLineCounter(extendedBufferedReader));
    }

    @Test
    @Timeout(8000)
    void testReadReturnsEndOfStream() throws IOException {
        when(mockReader.read()).thenReturn(ExtendedBufferedReader.END_OF_STREAM);

        int result = extendedBufferedReader.read();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, getLastChar(extendedBufferedReader));
        assertEquals(0, getLineCounter(extendedBufferedReader));
    }

    private int getLastChar(ExtendedBufferedReader reader) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lastChar");
            field.setAccessible(true);
            return field.getInt(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getLineCounter(ExtendedBufferedReader reader) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
            field.setAccessible(true);
            return field.getInt(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}