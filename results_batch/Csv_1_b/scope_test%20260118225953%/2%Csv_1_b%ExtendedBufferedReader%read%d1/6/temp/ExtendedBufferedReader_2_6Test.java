package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_2_6Test {

    ExtendedBufferedReader extendedBufferedReader;
    Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        extendedBufferedReader = new ExtendedBufferedReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void testReadReturnsCharAndUpdatesLastCharNoNewline() throws IOException {
        when(mockReader.read()).thenReturn((int) 'a', -1);

        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader);

        int result = reader.read();
        assertEquals('a', result);
        assertEquals('a', getPrivateField(reader, "lastChar"));
        assertEquals(0, getPrivateField(reader, "lineCounter"));
    }

    @Test
    @Timeout(8000)
    void testReadReturnsNewlineAndIncrementsLineCounter() throws IOException {
        when(mockReader.read()).thenReturn((int) '\n', -1);

        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader);

        int result = reader.read();
        assertEquals('\n', result);
        assertEquals('\n', getPrivateField(reader, "lastChar"));
        assertEquals(1, getPrivateField(reader, "lineCounter"));
    }

    @Test
    @Timeout(8000)
    void testReadReturnsEOF() throws IOException {
        when(mockReader.read()).thenReturn(-1);

        ExtendedBufferedReader reader = new ExtendedBufferedReader(mockReader);

        int result = reader.read();
        assertEquals(-1, result);
        assertEquals(-1, getPrivateField(reader, "lastChar")); // lastChar should be set to -1 after EOF
        assertEquals(0, getPrivateField(reader, "lineCounter"));
    }

    // Utility method to read private fields via reflection
    private int getPrivateField(ExtendedBufferedReader reader, String fieldName) {
        try {
            java.lang.reflect.Field field = ExtendedBufferedReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(reader);
            if (value instanceof Integer) {
                return (Integer) value;
            } else {
                throw new RuntimeException("Field " + fieldName + " is not an int");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}