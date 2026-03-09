package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_4_6Test {

    ExtendedBufferedReader readerSpy;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        ExtendedBufferedReader realReader = new ExtendedBufferedReader(mockReader);
        // Spy on the realReader
        readerSpy = Mockito.spy(realReader);
    }

    @Test
    @Timeout(8000)
    void testRead_lengthZero_returnsZero() throws IOException {
        char[] buf = new char[10];
        int result = readerSpy.read(buf, 0, 0);
        assertEquals(0, result);
        // The spy's read method is called once with length=0, so verify no calls to underlying reader's read:
        verify(mockReader, never()).read(any(char[].class), anyInt(), intThat(len -> len > 0));
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withNewLineNotPrecededByCarriageReturn() throws IOException {
        // Prepare buffer with content: 'a', '\n' (not preceded by '\r'), 'b'
        char[] buf = new char[3];
        buf[0] = 'a';
        buf[1] = '\n';
        buf[2] = 'b';

        doAnswer(invocation -> {
            char[] array = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int length = invocation.getArgument(2);
            System.arraycopy(buf, 0, array, offset, 3);
            return 3;
        }).when(mockReader).read(any(char[].class), anyInt(), anyInt());

        setLastChar(readerSpy, ExtendedBufferedReader.UNDEFINED);
        setLineCounter(readerSpy, 0);

        char[] readBuf = new char[5];
        int result = readerSpy.read(readBuf, 0, 3);
        assertEquals(3, result);

        int lineCounter = getLineCounter(readerSpy);
        assertEquals(1, lineCounter);

        int lastChar = getLastChar(readerSpy);
        assertEquals('b', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withNewLinePrecededByCarriageReturn() throws IOException {
        // buf: '\r', '\n', 'c'
        char[] buf = new char[3];
        buf[0] = '\r';
        buf[1] = '\n';
        buf[2] = 'c';

        doAnswer(invocation -> {
            char[] array = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int length = invocation.getArgument(2);
            System.arraycopy(buf, 0, array, offset, 3);
            return 3;
        }).when(mockReader).read(any(char[].class), anyInt(), anyInt());

        setLastChar(readerSpy, ExtendedBufferedReader.UNDEFINED);
        setLineCounter(readerSpy, 0);

        char[] readBuf = new char[5];
        int result = readerSpy.read(readBuf, 0, 3);
        assertEquals(3, result);

        int lineCounter = getLineCounter(readerSpy);
        assertEquals(1, lineCounter);

        int lastChar = getLastChar(readerSpy);
        assertEquals('c', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withOnlyCarriageReturn() throws IOException {
        char[] buf = new char[1];
        buf[0] = '\r';

        doAnswer(invocation -> {
            char[] array = invocation.getArgument(0);
            int offset = invocation.getArgument(1);
            int length = invocation.getArgument(2);
            System.arraycopy(buf, 0, array, offset, 1);
            return 1;
        }).when(mockReader).read(any(char[].class), anyInt(), anyInt());

        setLineCounter(readerSpy, 0);
        setLastChar(readerSpy, ExtendedBufferedReader.UNDEFINED);

        char[] readBuf = new char[5];
        int result = readerSpy.read(readBuf, 0, 1);
        assertEquals(1, result);

        int lineCounter = getLineCounter(readerSpy);
        assertEquals(1, lineCounter);

        int lastChar = getLastChar(readerSpy);
        assertEquals('\r', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenNegativeOne_setsLastCharToEndOfStream() throws IOException {
        doReturn(-1).when(mockReader).read(any(char[].class), anyInt(), anyInt());

        setLastChar(readerSpy, ExtendedBufferedReader.UNDEFINED);

        char[] readBuf = new char[5];
        int result = readerSpy.read(readBuf, 0, 5);
        assertEquals(-1, result);

        int lastChar = getLastChar(readerSpy);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    private int getLineCounter(ExtendedBufferedReader r) {
        try {
            Field f = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
            f.setAccessible(true);
            return f.getInt(r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getLastChar(ExtendedBufferedReader r) {
        try {
            Field f = ExtendedBufferedReader.class.getDeclaredField("lastChar");
            f.setAccessible(true);
            return f.getInt(r);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setLastChar(ExtendedBufferedReader r, int value) {
        try {
            Field f = ExtendedBufferedReader.class.getDeclaredField("lastChar");
            f.setAccessible(true);
            f.setInt(r, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setLineCounter(ExtendedBufferedReader r, int value) {
        try {
            Field f = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
            f.setAccessible(true);
            f.setInt(r, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}