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

class ExtendedBufferedReader_2_2Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testRead_returnsCharNotNewline() throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        // Use reflection to access the private 'in' field of BufferedReader
        Reader inReader = getInReader(spyReader);
        Reader spyInReader = Mockito.spy(inReader);

        // Replace the 'in' field with the spy
        setInReader(spyReader, spyInReader);

        when(spyInReader.read()).thenReturn((int) 'A');

        int result = spyReader.read();

        assertEquals('A', result);
        assertEquals('A', getLastChar(spyReader));
        assertEquals(0, getLineCounter(spyReader));
    }

    @Test
    @Timeout(8000)
    void testRead_returnsNewlineIncrementsLineCounter() throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        Reader inReader = getInReader(spyReader);
        Reader spyInReader = Mockito.spy(inReader);
        setInReader(spyReader, spyInReader);

        when(spyInReader.read()).thenReturn((int) '\n');

        int result = spyReader.read();

        assertEquals('\n', result);
        assertEquals('\n', getLastChar(spyReader));
        assertEquals(1, getLineCounter(spyReader));
    }

    @Test
    @Timeout(8000)
    void testRead_returnsEndOfStream() throws IOException, NoSuchFieldException, IllegalAccessException {
        ExtendedBufferedReader spyReader = Mockito.spy(extendedBufferedReader);

        Reader inReader = getInReader(spyReader);
        Reader spyInReader = Mockito.spy(inReader);
        setInReader(spyReader, spyInReader);

        when(spyInReader.read()).thenReturn(ExtendedBufferedReader.END_OF_STREAM);

        int result = spyReader.read();

        assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, getLastChar(spyReader));
        assertEquals(0, getLineCounter(spyReader));
    }

    // Helper method to access private 'lastChar' field via reflection
    private int getLastChar(ExtendedBufferedReader reader) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lastChar");
            field.setAccessible(true);
            return field.getInt(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to access private 'lineCounter' field via reflection
    private int getLineCounter(ExtendedBufferedReader reader) {
        try {
            Field field = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
            field.setAccessible(true);
            return field.getInt(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to access private 'in' field of BufferedReader via reflection
    private Reader getInReader(ExtendedBufferedReader reader) {
        try {
            Field inField = BufferedReader.class.getDeclaredField("in");
            inField.setAccessible(true);
            return (Reader) inField.get(reader);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to set the private 'in' field of BufferedReader via reflection
    private void setInReader(ExtendedBufferedReader reader, Reader newIn) {
        try {
            Field inField = BufferedReader.class.getDeclaredField("in");
            inField.setAccessible(true);
            inField.set(reader, newIn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}