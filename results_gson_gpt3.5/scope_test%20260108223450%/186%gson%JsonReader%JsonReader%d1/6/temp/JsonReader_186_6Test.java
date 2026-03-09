package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_186_6Test {

    private Reader mockReader;

    @BeforeEach
    void setup() {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void constructor_nullReader_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new JsonReader(null);
        });
        assertEquals("in == null", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void constructor_initializesFieldsCorrectly() throws Exception {
        JsonReader jsonReader = new JsonReader(mockReader);

        // Validate the 'in' field is set correctly
        Field inField = JsonReader.class.getDeclaredField("in");
        inField.setAccessible(true);
        Reader inValue = (Reader) inField.get(jsonReader);
        assertSame(mockReader, inValue);

        // Validate initial peeked value is PEEKED_NONE (0)
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        int peekedValue = (int) peekedField.get(jsonReader);
        assertEquals(0, peekedValue);

        // Validate buffer length
        Field bufferField = JsonReader.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        char[] buffer = (char[]) bufferField.get(jsonReader);
        assertEquals(1024, buffer.length);

        // Validate pos and limit are zero
        Field posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        int posValue = (int) posField.get(jsonReader);
        assertEquals(0, posValue);

        Field limitField = JsonReader.class.getDeclaredField("limit");
        limitField.setAccessible(true);
        int limitValue = (int) limitField.get(jsonReader);
        assertEquals(0, limitValue);

        // Validate lenient default false
        Field lenientField = JsonReader.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        boolean lenientValue = (boolean) lenientField.get(jsonReader);
        assertFalse(lenientValue);
    }

    @Test
    @Timeout(8000)
    void constructor_readerReadThrowsIOException_fieldInitialization() throws Exception {
        Reader throwingReader = mock(Reader.class);
        when(throwingReader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new IOException("read error"));
        JsonReader jsonReader = new JsonReader(throwingReader);

        // The constructor itself does not read, so no exception expected here
        // But we can test that calling fillBuffer (private) will throw IOException

        // Use reflection to invoke private fillBuffer method to confirm IOException propagates
        Method fillBufferMethod = JsonReader.class.getDeclaredMethod("fillBuffer", int.class);
        fillBufferMethod.setAccessible(true);

        Throwable thrown = assertThrows(Throwable.class, () -> {
            fillBufferMethod.invoke(jsonReader, 1);
        });

        // The thrown is InvocationTargetException, unwrap cause to check IOException
        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IOException);
        assertTrue(cause.getMessage().contains("read error"));
    }
}