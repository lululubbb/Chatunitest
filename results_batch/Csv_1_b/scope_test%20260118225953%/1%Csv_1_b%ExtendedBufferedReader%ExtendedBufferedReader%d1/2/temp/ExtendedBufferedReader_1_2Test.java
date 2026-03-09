package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_1_2Test {

    ExtendedBufferedReader reader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        reader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsChar() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a');
        int result = reader.read();
        assertEquals('a', result);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsEndOfStream() throws IOException {
        when(mockReader.read()).thenReturn(-1);
        int result = reader.read();
        assertEquals(-1, result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainReturnsChar() throws Exception {
        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);

        when(mockReader.read()).thenReturn((int) 'b');
        int result = (int) readAgainMethod.invoke(reader);
        assertEquals('b', result);
    }

    @Test
    @Timeout(8000)
    void testReadAgainReturnsEndOfStream() throws Exception {
        Method readAgainMethod = ExtendedBufferedReader.class.getDeclaredMethod("readAgain");
        readAgainMethod.setAccessible(true);

        when(mockReader.read()).thenReturn(-1);
        int result = (int) readAgainMethod.invoke(reader);
        assertEquals(-1, result);
    }

    @Test
    @Timeout(8000)
    void testReadCharArray() throws IOException {
        char[] buf = new char[5];
        when(mockReader.read(any(char[].class), eq(0), eq(5))).thenAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            b[off] = 'x';
            b[off + 1] = 'y';
            b[off + 2] = 'z';
            return 3;
        });

        int read = reader.read(buf, 0, 5);
        assertEquals(3, read);
        assertArrayEquals(new char[] {'x', 'y', 'z', '\u0000', '\u0000'}, buf);
    }

    @Test
    @Timeout(8000)
    void testReadLineReturnsLine() throws IOException {
        when(mockReader.read()).thenReturn((int) 'h', (int) 'e', (int) 'l', (int) 'l', (int) 'o', (int) '\n', -1);
        String line = reader.readLine();
        assertEquals("hello", line);
    }

    @Test
    @Timeout(8000)
    void testReadLineReturnsNullAtEOF() throws IOException {
        when(mockReader.read()).thenReturn(-1);
        String line = reader.readLine();
        assertNull(line);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsChar() throws Exception {
        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        when(mockReader.read()).thenReturn((int) 'k');
        int result = (int) lookAheadMethod.invoke(reader);
        assertEquals('k', result);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws Exception {
        Method lookAheadMethod = ExtendedBufferedReader.class.getDeclaredMethod("lookAhead");
        lookAheadMethod.setAccessible(true);

        when(mockReader.read()).thenReturn(-1);
        int result = (int) lookAheadMethod.invoke(reader);
        assertEquals(-1, result);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumberInitial() throws Exception {
        Method getLineNumberMethod = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        getLineNumberMethod.setAccessible(true);

        int lineNumber = (int) getLineNumberMethod.invoke(reader);
        assertEquals(0, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumberAfterReadingLine() throws IOException, Exception {
        when(mockReader.read()).thenReturn((int) 'a', (int) '\n', -1);
        reader.readLine();

        Method getLineNumberMethod = ExtendedBufferedReader.class.getDeclaredMethod("getLineNumber");
        getLineNumberMethod.setAccessible(true);

        int lineNumber = (int) getLineNumberMethod.invoke(reader);
        assertEquals(1, lineNumber);
    }
}