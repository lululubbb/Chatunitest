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

class ExtendedBufferedReader_5_2Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NullLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);
        // Use doReturn to stub the super.readLine(), but since readLine() is final in BufferedReader,
        // we stub the spy's readLine() itself.
        doReturn(null).when(spyReader).readLine();

        String result = spyReader.readLine();

        assertNull(result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(spyReader);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(spyReader);
        assertEquals(0, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_EmptyLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader mockReaderForEmptyLine = mock(Reader.class);

        doAnswer(invocation -> {
            char[] buf = invocation.getArgument(0);
            buf[0] = '\n';
            return 1;
        }).doAnswer(invocation -> -1)
          .when(mockReaderForEmptyLine).read(any(char[].class), anyInt(), anyInt());

        ExtendedBufferedReader readerUnderTest = new ExtendedBufferedReader(mockReaderForEmptyLine);

        String result = readerUnderTest.readLine();

        assertEquals("", result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(readerUnderTest);
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(readerUnderTest);
        assertEquals(1, lineCounter);
    }

    @Test
    @Timeout(8000)
    void testReadLine_NonEmptyLine() throws IOException, NoSuchFieldException, IllegalAccessException {
        String testLine = "Hello, World!";

        Reader reader = new java.io.StringReader(testLine + "\n");
        ExtendedBufferedReader readerUnderTest = new ExtendedBufferedReader(reader);

        String result = readerUnderTest.readLine();

        assertEquals(testLine, result);

        Field lastCharField = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        lastCharField.setAccessible(true);
        int lastChar = (int) lastCharField.get(readerUnderTest);
        assertEquals(testLine.charAt(testLine.length() - 1), lastChar);

        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        int lineCounter = (int) lineCounterField.get(readerUnderTest);
        assertEquals(1, lineCounter);
    }
}