package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_201_5Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedTrue_returnsTrue() throws Exception {
        setPeekedField(getPeekedTrue());
        setStackSize(1);
        setPathIndices(0, 0);

        boolean result = jsonReader.nextBoolean();

        assertTrue(result);
        assertEquals(0, getPeekedField());
        assertEquals(1, getPathIndices(0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedFalse_returnsFalse() throws Exception {
        setPeekedField(getPeekedFalse());
        setStackSize(1);
        setPathIndices(0, 0);

        boolean result = jsonReader.nextBoolean();

        assertFalse(result);
        assertEquals(0, getPeekedField());
        assertEquals(1, getPathIndices(0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedNone_callsDoPeekAndReturnsTrue() throws Exception {
        setPeekedField(0);
        setStackSize(1);
        setPathIndices(0, 0);

        // Mock doPeek to return PEEKED_TRUE
        JsonReader spyReader = spy(jsonReader);
        doReturn(getPeekedTrue()).when(spyReader).doPeek();

        boolean result = spyReader.nextBoolean();

        assertTrue(result);
        assertEquals(0, getPeekedField(spyReader));
        assertEquals(1, getPathIndices(spyReader, 0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedNone_callsDoPeekAndReturnsFalse() throws Exception {
        setPeekedField(0);
        setStackSize(1);
        setPathIndices(0, 0);

        JsonReader spyReader = spy(jsonReader);
        doReturn(getPeekedFalse()).when(spyReader).doPeek();

        boolean result = spyReader.nextBoolean();

        assertFalse(result);
        assertEquals(0, getPeekedField(spyReader));
        assertEquals(1, getPathIndices(spyReader, 0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_invalidPeek_throwsIllegalStateException() throws Exception {
        setPeekedField(1); // PEEKED_BEGIN_OBJECT, invalid for boolean
        setStackSize(1);
        setPathIndices(0, 0);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            jsonReader.nextBoolean();
        });

        assertTrue(exception.getMessage().startsWith("Expected a boolean but was"));
    }

    // Helper methods for reflection and constants

    private int getPeekedTrue() throws Exception {
        return getStaticFinalInt("PEEKED_TRUE");
    }

    private int getPeekedFalse() throws Exception {
        return getStaticFinalInt("PEEKED_FALSE");
    }

    private int getStaticFinalInt(String fieldName) throws Exception {
        Field f = JsonReader.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.getInt(null);
    }

    private void setPeekedField(int value) throws Exception {
        setPeekedField(jsonReader, value);
    }

    private void setPeekedField(JsonReader reader, int value) throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        peekedField.setInt(reader, value);
    }

    private int getPeekedField() throws Exception {
        return getPeekedField(jsonReader);
    }

    private int getPeekedField(JsonReader reader) throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        return peekedField.getInt(reader);
    }

    private void setStackSize(int size) throws Exception {
        Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonReader, size);
    }

    private void setPathIndices(int index, int value) throws Exception {
        setPathIndices(jsonReader, index, value);
    }

    private void setPathIndices(JsonReader reader, int index, int value) throws Exception {
        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        pathIndices[index] = value;
    }

    private int getPathIndices(int index) throws Exception {
        return getPathIndices(jsonReader, index);
    }

    private int getPathIndices(JsonReader reader, int index) throws Exception {
        Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(reader);
        return pathIndices[index];
    }
}