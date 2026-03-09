package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_6_5Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsCharSuccessfully() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        doReturn(65).when(spyReader).read();
        doNothing().when(spyReader).reset();

        int result = spyReader.lookAhead();

        assertEquals(65, result);
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        doReturn(-1).when(spyReader).read();
        doNothing().when(spyReader).reset();

        int result = spyReader.lookAhead();

        assertEquals(-1, result);
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOExceptionFromMark() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doThrow(new IOException("mark failed")).when(spyReader).mark(1);

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("mark failed", thrown.getMessage());
        verify(spyReader).mark(1);
        verify(spyReader, never()).read();
        verify(spyReader, never()).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOExceptionFromRead() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        doThrow(new IOException("read failed")).when(spyReader).read();

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("read failed", thrown.getMessage());
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader, never()).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOExceptionFromReset() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        doReturn(42).when(spyReader).read();
        doThrow(new IOException("reset failed")).when(spyReader).reset();

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("reset failed", thrown.getMessage());
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }
}