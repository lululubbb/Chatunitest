package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_4_4Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader) {
            // Override read(char[], int, int) to call super.read to mock behavior
            @Override
            public int read(char[] buf, int offset, int length) throws IOException {
                return super.read(buf, offset, length);
            }
        };
    }

    @Test
    @Timeout(8000)
    void testRead_LengthZero_ReturnsZero() throws IOException {
        char[] buf = new char[10];
        int result = extendedBufferedReader.read(buf, 0, 0);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsMinusOne_SetsLastCharToEndOfStream() throws IOException {
        char[] buf = new char[10];
        // Spy on extendedBufferedReader to mock super.read
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        // Mock super.read to return -1
        doReturn(-1).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        int result = spyReader.read(buf, 0, 5);
        assertEquals(-1, result);

        // Use reflection to check private field lastChar
        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsPositive_NoNewlineOrCarriageReturn() throws IOException {
        char[] buf = new char[5];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';
        buf[3] = 'd';
        buf[4] = 'e';

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, b, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        int result = spyReader.read(new char[5], 0, 5);
        assertEquals(5, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        // No \n or \r, so lineCounter should be 0
        assertEquals(0, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('e', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsPositive_WithNewlineNotPrecededByCarriageReturn() throws IOException {
        char[] buf = new char[] {'a', '\n', 'b', 'c', 'd'};

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, b, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        // lastChar initially UNDEFINED (-2)
        setPrivateField(spyReader, "lastChar", ExtendedBufferedReader.UNDEFINED);

        int result = spyReader.read(new char[5], 0, 5);
        assertEquals(5, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        // '\n' at index 1, previous char at index 0 is 'a' != '\r', so lineCounter++
        assertEquals(1, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('d', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsPositive_WithNewlinePrecededByCarriageReturn() throws IOException {
        char[] buf = new char[] {'a', '\r', '\n', 'b', 'c'};

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, b, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        setPrivateField(spyReader, "lastChar", ExtendedBufferedReader.UNDEFINED);

        int result = spyReader.read(new char[5], 0, 5);
        assertEquals(5, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        // '\r' at index 1 -> lineCounter++
        // '\n' at index 2 preceded by buf[1] == '\r' -> no lineCounter++
        assertEquals(1, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('c', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsPositive_NewlineAtStart_UsesLastChar() throws IOException {
        char[] buf = new char[] {'\n', 'a', 'b'};

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, b, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        // Set lastChar to something other than '\r', e.g. 'x'
        setPrivateField(spyReader, "lastChar", 'x');

        int result = spyReader.read(new char[3], 0, 3);
        assertEquals(3, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        // '\n' at index 0, i == offset so check lastChar != '\r' (lastChar='x'), so lineCounter++
        assertEquals(1, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('b', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsPositive_NewlineAtStart_PrecededByCarriageReturnLastChar() throws IOException {
        char[] buf = new char[] {'\n', 'a', 'b'};

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, b, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        // Set lastChar to '\r'
        setPrivateField(spyReader, "lastChar", '\r');

        int result = spyReader.read(new char[3], 0, 3);
        assertEquals(3, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        // '\n' at index 0, i == offset, lastChar == '\r' so no lineCounter++
        assertEquals(0, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('b', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_ReadReturnsPositive_MultipleNewlinesAndCarriageReturns() throws IOException {
        char[] buf = new char[] {'\r', '\n', '\r', 'a', '\n'};

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            char[] b = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, b, off, len);
            return len;
        }).when(spyReader).read(any(char[].class), anyInt(), anyInt());

        setPrivateField(spyReader, "lastChar", ExtendedBufferedReader.UNDEFINED);

        int result = spyReader.read(new char[5], 0, 5);
        assertEquals(5, result);

        int lineCounter = (int) getPrivateField(spyReader, "lineCounter");
        // index 0: '\r' -> +1
        // index 1: '\n' preceded by '\r' -> no increment
        // index 2: '\r' -> +1 (total 2)
        // index 3: 'a' no increment
        // index 4: '\n' preceded by 'a' != '\r' -> +1 (total 3)
        assertEquals(3, lineCounter);

        int lastChar = (int) getPrivateField(spyReader, "lastChar");
        assertEquals('\n', lastChar);
    }

    private Object getPrivateField(Object instance, String fieldName) {
        try {
            java.lang.reflect.Field field = ExtendedBufferedReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPrivateField(Object instance, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = ExtendedBufferedReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}