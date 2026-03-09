package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_6_6Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsNextChar() throws IOException {
        // Arrange
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);
        // Use doAnswer to avoid calling real mark/reset/read on spy, which may not be mockable directly
        doAnswer(invocation -> null).when(spyReader).mark(1);
        doReturn((int) 'a').when(spyReader).read();
        doAnswer(invocation -> null).when(spyReader).reset();

        // Act
        int result = spyReader.lookAhead();

        // Assert
        assertEquals('a', result);
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws IOException {
        // Arrange
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);
        doAnswer(invocation -> null).when(spyReader).mark(1);
        doReturn(ExtendedBufferedReader.END_OF_STREAM).when(spyReader).read();
        doAnswer(invocation -> null).when(spyReader).reset();

        // Act
        int result = spyReader.lookAhead();

        // Assert
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        verify(spyReader).mark(1);
        verify(spyReader).read();
        verify(spyReader).reset();
    }

    @Test
    @Timeout(8000)
    void testLookAheadThrowsIOException() throws IOException {
        // Arrange
        ExtendedBufferedReader spyReader = spy(extendedBufferedReader);
        doThrow(new IOException("mark failed")).when(spyReader).mark(1);

        // Act & Assert
        IOException thrown = null;
        try {
            spyReader.lookAhead();
        } catch (IOException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals("mark failed", thrown.getMessage());
    }
}