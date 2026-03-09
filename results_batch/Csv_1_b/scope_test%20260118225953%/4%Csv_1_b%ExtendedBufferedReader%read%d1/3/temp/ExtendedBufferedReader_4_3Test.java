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
import org.mockito.Mockito;

class ExtendedBufferedReader_4_3Test {

    ExtendedBufferedReader readerSpy;

    @BeforeEach
    void setUp() throws Exception {
        Reader mockReader = mock(Reader.class);
        ExtendedBufferedReader realReader = new ExtendedBufferedReader(mockReader);
        readerSpy = Mockito.spy(realReader);
    }

    @Test
    @Timeout(8000)
    void testRead_lengthZero_returnsZero() throws IOException {
        char[] buf = new char[10];
        int result = readerSpy.read(buf, 0, 0);
        assertEquals(0, result);
        // Since read with length=0 returns immediately, super.read should never be called
        // We verify that the spy's read(char[],int,int) method is called exactly once (this call)
        verify(readerSpy, times(1)).read(buf, 0, 0);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_noNewlineOrCarriageReturn() throws Exception {
        // Instead of trying to mock super.read, create a subclass to override read behavior
        class TestExtendedBufferedReader extends ExtendedBufferedReader {
            TestExtendedBufferedReader(Reader r) {
                super(r);
            }

            @Override
            public int read(char[] buf, int offset, int length) throws IOException {
                // simulate super.read returns 3 and fills buf with 'a','b','c'
                if (length == 0) {
                    return 0;
                }
                buf[offset] = 'a';
                buf[offset + 1] = 'b';
                buf[offset + 2] = 'c';
                return 3;
            }
        }

        TestExtendedBufferedReader testReader = new TestExtendedBufferedReader(mock(Reader.class));
        ExtendedBufferedReader spyReader = spy(testReader);

        char[] buf = new char[5];
        int result = spyReader.read(buf, 0, 3);
        assertEquals(3, result);
        assertArrayEquals(new char[] {'a', 'b', 'c', '\0', '\0'}, buf);

        // lineCounter should be 0 because no \n or \r
        int lineCounter = getPrivateIntField(spyReader, "lineCounter");
        assertEquals(0, lineCounter);

        // lastChar should be 'c'
        int lastChar = getPrivateIntField(spyReader, "lastChar");
        assertEquals('c', lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenPositive_withLineBreaks() throws IOException, NoSuchFieldException, IllegalAccessException {
        // We'll create a real ExtendedBufferedReader over a Reader that returns known chars
        String input = "a\r\nb\n\r";
        ExtendedBufferedReader realReader = new ExtendedBufferedReader(new java.io.StringReader(input));
        char[] buf = new char[10];

        // read first 3 chars: 'a', '\r', '\n'
        int readLen1 = realReader.read(buf, 0, 3);
        assertEquals(3, readLen1);
        // lineCounter after first read should be 1 (for '\r')
        int lineCounter = getPrivateIntField(realReader, "lineCounter");
        assertEquals(1, lineCounter);

        // read next 3 chars: 'b', '\n', '\r'
        int readLen2 = realReader.read(buf, 0, 3);
        assertEquals(3, readLen2);
        // lineCounter after second read should be 3
        lineCounter = getPrivateIntField(realReader, "lineCounter");
        assertEquals(3, lineCounter);

        // read until end
        int readLen3 = realReader.read(buf, 0, 10);
        // Should be -1 (end of stream)
        assertEquals(-1, readLen3);
        int lastChar = getPrivateIntField(realReader, "lastChar");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test
    @Timeout(8000)
    void testRead_lenNegativeOne_setsLastCharToEndOfStream() throws Exception {
        // Create a spy that calls real read method on empty stream
        ExtendedBufferedReader spyReader = spy(new ExtendedBufferedReader(new java.io.StringReader("")));
        // We do not mock read to -1 because that would cause infinite recursion or interfere with tested method

        char[] buf = new char[5];
        int len = spyReader.read(buf, 0, 5);
        assertEquals(-1, len);
        int lastChar = getPrivateIntField(spyReader, "lastChar");
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    private int getPrivateIntField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Field f = null;
        // Search in class hierarchy for the field
        while (clazz != null) {
            try {
                f = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (f == null) {
            throw new NoSuchFieldException(fieldName);
        }
        f.setAccessible(true);
        return f.getInt(obj);
    }
}