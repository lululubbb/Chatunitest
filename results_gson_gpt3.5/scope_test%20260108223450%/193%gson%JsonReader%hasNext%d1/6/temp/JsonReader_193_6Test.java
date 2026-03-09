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

public class JsonReader_193_6Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_EOF = 17;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNotNoneAndNotEndTokens_returnsTrue() throws Exception {
        setPeeked(5); // PEEKED_TRUE (not end tokens)
        assertTrue(jsonReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsNotEndTokens_returnsTrue() throws Exception {
        setPeeked(PEEKED_NONE);
        // Spy on jsonReader to mock doPeek
        JsonReader spyReader = spy(jsonReader);
        doReturn(5).when(spyReader).doPeek(); // PEEKED_TRUE (not end tokens)
        assertTrue(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsEndObject_returnsFalse() throws Exception {
        setPeeked(PEEKED_NONE);
        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_END_OBJECT).when(spyReader).doPeek();
        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsEndArray_returnsFalse() throws Exception {
        setPeeked(PEEKED_NONE);
        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_END_ARRAY).when(spyReader).doPeek();
        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsEOF_returnsFalse() throws Exception {
        setPeeked(PEEKED_NONE);
        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_EOF).when(spyReader).doPeek();
        assertFalse(spyReader.hasNext());
    }

    // Helper method to set the private field 'peeked'
    private void setPeeked(int value) throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        peekedField.setInt(jsonReader, value);
    }
}