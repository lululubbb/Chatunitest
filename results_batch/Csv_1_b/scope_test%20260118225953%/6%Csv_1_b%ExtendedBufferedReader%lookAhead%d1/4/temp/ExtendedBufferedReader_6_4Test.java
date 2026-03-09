package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_6_4Test {

    ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        extendedBufferedReader = new ExtendedBufferedReader(new java.io.StringReader(""));
    }

    private void callRealMark(ExtendedBufferedReader spyReader, int readAheadLimit) throws Exception {
        Method markMethod = BufferedReader.class.getDeclaredMethod("mark", int.class);
        markMethod.setAccessible(true);
        markMethod.invoke(spyReader, readAheadLimit);
    }

    private void callRealReset(ExtendedBufferedReader spyReader) throws Exception {
        Method resetMethod = BufferedReader.class.getDeclaredMethod("reset");
        resetMethod.setAccessible(true);
        resetMethod.invoke(spyReader);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsCharSuccessfully() throws Exception {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            callRealMark(spyReader, invocation.getArgument(0));
            return null;
        }).when(spyReader).mark(anyInt());

        doAnswer(invocation -> {
            callRealReset(spyReader);
            return null;
        }).when(spyReader).reset();

        doReturn((int) 'A').when(spyReader).read();

        int result = spyReader.lookAhead();

        assertEquals('A', result);
        verify(spyReader).mark(anyInt());
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws Exception {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            callRealMark(spyReader, invocation.getArgument(0));
            return null;
        }).when(spyReader).mark(anyInt());

        doAnswer(invocation -> {
            callRealReset(spyReader);
            return null;
        }).when(spyReader).reset();

        doReturn(ExtendedBufferedReader.END_OF_STREAM).when(spyReader).read();

        int result = spyReader.lookAhead();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        verify(spyReader).mark(anyInt());
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOExceptionOnMark() throws IOException {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doThrow(new IOException("mark failed")).when(spyReader).mark(anyInt());

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("mark failed", thrown.getMessage());
        verify(spyReader).mark(anyInt());
        verify(spyReader, never()).read();
        verify(spyReader, never()).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOExceptionOnRead() throws Exception {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            callRealMark(spyReader, invocation.getArgument(0));
            return null;
        }).when(spyReader).mark(anyInt());

        doThrow(new IOException("read failed")).when(spyReader).read();

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("read failed", thrown.getMessage());
        verify(spyReader).mark(anyInt());
        verify(spyReader).read();
        verify(spyReader, never()).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOExceptionOnReset() throws Exception {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        doAnswer(invocation -> {
            callRealMark(spyReader, invocation.getArgument(0));
            return null;
        }).when(spyReader).mark(anyInt());

        doReturn((int) 'B').when(spyReader).read();

        doThrow(new IOException("reset failed")).when(spyReader).reset();

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("reset failed", thrown.getMessage());
        verify(spyReader).mark(anyInt());
        verify(spyReader).read();
        verify(spyReader).reset();
    }
}