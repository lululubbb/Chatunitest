package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_6_2Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader) {
            @Override
            public void mark(int readAheadLimit) throws IOException {
                // do nothing
            }

            @Override
            public int read() throws IOException {
                return super.read();
            }

            @Override
            public void reset() throws IOException {
                // do nothing
            }
        };
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsNextChar() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        when(spyReader.read()).thenReturn((int) 'a');
        doNothing().when(spyReader).reset();

        int result = spyReader.lookAhead();

        assertEquals('a', result);

        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        when(spyReader.read()).thenReturn(ExtendedBufferedReader.END_OF_STREAM);
        doNothing().when(spyReader).reset();

        int result = spyReader.lookAhead();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);

        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOException() throws Exception {
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);

        doNothing().when(spyReader).mark(1);
        when(spyReader.read()).thenThrow(new IOException("read error"));

        IOException thrown = assertThrows(IOException.class, spyReader::lookAhead);
        assertEquals("read error", thrown.getMessage());

        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader, never()).reset();
    }
}