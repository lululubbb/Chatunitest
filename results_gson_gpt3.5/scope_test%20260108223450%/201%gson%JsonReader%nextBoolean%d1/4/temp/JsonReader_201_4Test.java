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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_201_4Test {

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
        setPeekedValue(getPeekedTrue());
        setStackSize(1);
        setPathIndices(0, 0);

        boolean result = jsonReader.nextBoolean();

        assertTrue(result);
        assertEquals(getPeekedNone(), getPeekedValue());
        assertEquals(1, getPathIndices(0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedFalse_returnsFalse() throws Exception {
        setPeekedValue(getPeekedFalse());
        setStackSize(1);
        setPathIndices(0, 0);

        boolean result = jsonReader.nextBoolean();

        assertFalse(result);
        assertEquals(getPeekedNone(), getPeekedValue());
        assertEquals(1, getPathIndices(0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedNone_callsDoPeekAndReturnsTrue() throws Exception {
        setPeekedValue(getPeekedNone());
        setStackSize(1);
        setPathIndices(0, 0);

        JsonReader spyReader = spy(jsonReader);
        doReturn(getPeekedTrue()).when(spyReader).doPeek();

        boolean result = spyReader.nextBoolean();

        assertTrue(result);
        assertEquals(getPeekedNone(), getPeekedField(spyReader));
        assertEquals(1, getPathIndices(spyReader, 0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_peekedNone_callsDoPeekAndReturnsFalse() throws Exception {
        setPeekedValue(getPeekedNone());
        setStackSize(1);
        setPathIndices(0, 0);

        JsonReader spyReader = spy(jsonReader);
        doReturn(getPeekedFalse()).when(spyReader).doPeek();

        boolean result = spyReader.nextBoolean();

        assertFalse(result);
        assertEquals(getPeekedNone(), getPeekedField(spyReader));
        assertEquals(1, getPathIndices(spyReader, 0));
    }

    @Test
    @Timeout(8000)
    public void testNextBoolean_invalidPeeked_throwsIllegalStateException() throws Exception {
        setStackSize(1);
        setPeekedValue(999); // Set invalid peeked to trigger error

        JsonReader spyReader = spy(jsonReader);
        // avoid calling doPeek() by setting peeked to invalid value directly

        // Set lenient to true to avoid AssertionError from peek()
        spyReader.setLenient(true);

        // Spy peek() to return a dummy token to avoid AssertionError
        doReturn(JsonToken.BOOLEAN).when(spyReader).peek();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextBoolean);
        assertTrue(thrown.getMessage().startsWith("Expected a boolean but was"));
    }

    // Helper methods to access private fields and constants

    private int getPeekedValue() throws Exception {
        return getPeekedField(jsonReader);
    }

    private int getPeekedField(JsonReader instance) throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        return peekedField.getInt(instance);
    }

    private void setPeekedValue(int value) {
        try {
            Field peekedField = JsonReader.class.getDeclaredField("peeked");
            peekedField.setAccessible(true);
            peekedField.setInt(jsonReader, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setStackSize(int size) {
        try {
            Field stackSizeField = JsonReader.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);
            stackSizeField.setInt(jsonReader, size);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPathIndices(int index, int value) {
        try {
            Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
            pathIndicesField.setAccessible(true);
            int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
            pathIndices[index] = value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getPathIndices(int index) {
        try {
            Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
            pathIndicesField.setAccessible(true);
            int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
            return pathIndices[index];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getPathIndices(JsonReader instance, int index) {
        try {
            Field pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
            pathIndicesField.setAccessible(true);
            int[] pathIndices = (int[]) pathIndicesField.get(instance);
            return pathIndices[index];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getPeekedNone() {
        return getStaticFinalInt("PEEKED_NONE");
    }

    private int getPeekedTrue() {
        return getStaticFinalInt("PEEKED_TRUE");
    }

    private int getPeekedFalse() {
        return getStaticFinalInt("PEEKED_FALSE");
    }

    private int getStaticFinalInt(String fieldName) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}