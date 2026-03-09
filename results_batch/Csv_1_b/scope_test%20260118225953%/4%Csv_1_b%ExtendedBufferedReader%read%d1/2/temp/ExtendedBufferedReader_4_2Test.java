package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_4_2Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader) {
            @Override
            public int read(char[] buf, int offset, int length) throws IOException {
                return super.read(buf, offset, length);
            }
        };
    }

    @Test
    @Timeout(8000)
    void testRead_lengthZero_returnsZero() throws IOException {
        char[] buf = new char[10];
        int result = extendedBufferedReader.read(buf, 0, 0);
        assertEquals(0, result);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_noNewlineOrCarriage_return() throws Exception {
        char[] buf = new char[5];
        buf[0] = 'a';
        buf[1] = 'b';
        buf[2] = 'c';
        buf[3] = 'd';
        buf[4] = 'e';

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);
        doCallRealMethod().when(spyReader).read(any(char[].class), anyInt(), anyInt());
        doAnswer(invocation -> {
            char[] arr = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, arr, off, len);
            return len;
        }).when((BufferedReader) spyReader).read(any(char[].class), anyInt(), anyInt());

        char[] actualBuf = new char[5];
        int len = spyReader.read(actualBuf, 0, 5);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(spyReader);
        assertEquals('e', lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(spyReader);
        assertEquals(0, lineCounter);

        assertEquals(5, len);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withCarriageReturnInBuf_incrementsLineCounter() throws Exception {
        char[] buf = new char[5];
        buf[0] = 'a';
        buf[1] = '\r';
        buf[2] = 'b';
        buf[3] = 'c';
        buf[4] = 'd';

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);
        doCallRealMethod().when(spyReader).read(any(char[].class), anyInt(), anyInt());
        doAnswer(invocation -> {
            char[] arr = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, arr, off, len);
            return len;
        }).when((BufferedReader) spyReader).read(any(char[].class), anyInt(), anyInt());

        char[] actualBuf = new char[5];
        int len = spyReader.read(actualBuf, 0, 5);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(spyReader);
        assertEquals(1, lineCounter);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(spyReader);
        assertEquals('d', lastChar);

        assertEquals(5, len);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withNewlineWithoutPrecedingCarriageReturn_incrementsLineCounter() throws Exception {
        char[] buf = new char[5];
        buf[0] = 'a';
        buf[1] = '\n';
        buf[2] = 'b';
        buf[3] = 'c';
        buf[4] = 'd';

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);
        doCallRealMethod().when(spyReader).read(any(char[].class), anyInt(), anyInt());
        doAnswer(invocation -> {
            char[] arr = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, arr, off, len);
            return len;
        }).when((BufferedReader) spyReader).read(any(char[].class), anyInt(), anyInt());

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.set(spyReader, ExtendedBufferedReader.UNDEFINED);

        char[] actualBuf = new char[5];
        int len = spyReader.read(actualBuf, 0, 5);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(spyReader);
        assertEquals(1, lineCounter);

        int lastChar = (int) lastCharField.get(spyReader);
        assertEquals('d', lastChar);

        assertEquals(5, len);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withNewlineWithPrecedingCarriageReturn_doesNotIncrementLineCounter() throws Exception {
        char[] buf = new char[5];
        buf[0] = 'a';
        buf[1] = '\r';
        buf[2] = '\n';
        buf[3] = 'c';
        buf[4] = 'd';

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);
        doCallRealMethod().when(spyReader).read(any(char[].class), anyInt(), anyInt());
        doAnswer(invocation -> {
            char[] arr = invocation.getArgument(0);
            int off = invocation.getArgument(1);
            int len = invocation.getArgument(2);
            System.arraycopy(buf, 0, arr, off, len);
            return len;
        }).when((BufferedReader) spyReader).read(any(char[].class), anyInt(), anyInt());

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        lineCounterField.set(spyReader, 1);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.set(spyReader, '\r');

        char[] actualBuf = new char[5];
        int len = spyReader.read(actualBuf, 0, 5);

        int lineCounter = (int) lineCounterField.get(spyReader);
        assertEquals(1, lineCounter);

        int lastChar = (int) lastCharField.get(spyReader);
        assertEquals('d', lastChar);

        assertEquals(5, len);
    }

    @Test
    @Timeout(8000)
    void testRead_lenNegativeOne_setsLastCharToEndOfStream() throws Exception {
        char[] buf = new char[5];

        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);
        doReturn(-1).when((BufferedReader) spyReader).read(any(char[].class), anyInt(), anyInt());
        doCallRealMethod().when(spyReader).read(any(char[].class), anyInt(), anyInt());

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        lastCharField.set(spyReader, 0);

        int len = spyReader.read(buf, 0, 5);

        int lastChar = (int) lastCharField.get(spyReader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        assertEquals(-1, len);
    }
}