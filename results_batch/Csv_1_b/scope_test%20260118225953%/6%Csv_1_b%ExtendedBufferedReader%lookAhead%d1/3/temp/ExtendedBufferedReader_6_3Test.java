package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExtendedBufferedReader_6_3Test {

    private ExtendedBufferedReader extendedBufferedReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() throws IOException {
        mockReader = mock(Reader.class);
        // When mark/reset are called, the underlying Reader should support them.
        doNothing().when(mockReader).mark(anyInt());
        doNothing().when(mockReader).reset();

        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsNextChar() throws IOException {
        // Arrange
        when(mockReader.read()).thenReturn((int) 'A');

        // Act
        int result = extendedBufferedReader.lookAhead();

        // Assert
        assertEquals('A', result);
    }

    @Test
    @Timeout(8000)
    void testLookAheadReturnsEndOfStream() throws IOException {
        // Arrange
        when(mockReader.read()).thenReturn(ExtendedBufferedReader.END_OF_STREAM);

        // Act
        int result = extendedBufferedReader.lookAhead();

        // Assert
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
    }

    @Test
    @Timeout(8000)
    void testLookAheadMultipleCalls() throws IOException {
        // Arrange
        when(mockReader.read()).thenReturn((int) 'B');

        // Act
        int firstCall = extendedBufferedReader.lookAhead();
        int secondCall = extendedBufferedReader.lookAhead();

        // Assert
        assertEquals('B', firstCall);
        assertEquals('B', secondCall);

        // Verify that read() was called twice on the underlying Reader (mark/reset do not cache)
        verify(mockReader, times(2)).read();
    }

    @Test
    @Timeout(8000)
    void testLookAheadResetCalled() throws IOException {
        // Spy on ExtendedBufferedReader to verify mark and reset calls on underlying Reader
        Reader spyReader = spy(mockReader);
        ExtendedBufferedReader spyExtendedBufferedReader = new ExtendedBufferedReader(spyReader);

        when(spyReader.read()).thenReturn((int) 'C');
        doNothing().when(spyReader).mark(anyInt());
        doNothing().when(spyReader).reset();

        int result = spyExtendedBufferedReader.lookAhead();

        assertEquals('C', result);
        verify(spyReader).mark(1);
        verify(spyReader).reset();
    }
}