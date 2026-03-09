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

public class JsonReader_193_5Test {

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
    public void testHasNext_peekedNotNoneAndNotEndTokens() throws Exception {
        // Set peeked to a value other than PEEKED_NONE, PEEKED_END_OBJECT, PEEKED_END_ARRAY, PEEKED_EOF
        setPeeked(5); // e.g. PEEKED_TRUE
        assertTrue(jsonReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsNonEnd() throws Exception {
        setPeeked(PEEKED_NONE);

        // Use reflection to mock doPeek() to return PEEKED_BEGIN_OBJECT (1)
        Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
        doPeekMethod.setAccessible(true);

        JsonReader spyReader = spy(jsonReader);
        doReturn(1).when(spyReader).doPeek();

        assertTrue(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsEndObject() throws Exception {
        setPeeked(PEEKED_NONE);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_END_OBJECT).when(spyReader).doPeek();

        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsEndArray() throws Exception {
        setPeeked(PEEKED_NONE);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_END_ARRAY).when(spyReader).doPeek();

        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNone_doPeekReturnsEOF() throws Exception {
        setPeeked(PEEKED_NONE);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_EOF).when(spyReader).doPeek();

        assertFalse(spyReader.hasNext());
    }

    private void setPeeked(int value) throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        peekedField.setInt(jsonReader, value);
    }
}