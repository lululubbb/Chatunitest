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

public class JsonReader_193_3Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedNotNoneAndNotEndTokens() throws Exception {
        setPeekedValue(1); // PEEKED_BEGIN_OBJECT
        assertTrue(jsonReader.hasNext());
        setPeekedValue(3); // PEEKED_BEGIN_ARRAY
        assertTrue(jsonReader.hasNext());
        setPeekedValue(5); // PEEKED_TRUE
        assertTrue(jsonReader.hasNext());
        setPeekedValue(6); // PEEKED_FALSE
        assertTrue(jsonReader.hasNext());
        setPeekedValue(7); // PEEKED_NULL
        assertTrue(jsonReader.hasNext());
        setPeekedValue(8); // PEEKED_SINGLE_QUOTED
        assertTrue(jsonReader.hasNext());
        setPeekedValue(9); // PEEKED_DOUBLE_QUOTED
        assertTrue(jsonReader.hasNext());
        setPeekedValue(10); // PEEKED_UNQUOTED
        assertTrue(jsonReader.hasNext());
        setPeekedValue(11); // PEEKED_BUFFERED
        assertTrue(jsonReader.hasNext());
        setPeekedValue(12); // PEEKED_SINGLE_QUOTED_NAME
        assertTrue(jsonReader.hasNext());
        setPeekedValue(13); // PEEKED_DOUBLE_QUOTED_NAME
        assertTrue(jsonReader.hasNext());
        setPeekedValue(14); // PEEKED_UNQUOTED_NAME
        assertTrue(jsonReader.hasNext());
        setPeekedValue(15); // PEEKED_LONG
        assertTrue(jsonReader.hasNext());
        setPeekedValue(16); // PEEKED_NUMBER
        assertTrue(jsonReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNone_callsDoPeekAndReturnsTrue() throws Exception {
        setPeekedValue(0); // PEEKED_NONE

        // Spy on jsonReader to mock doPeek method
        JsonReader spyReader = spy(jsonReader);
        doReturn(1).when(spyReader).doPeek();

        assertTrue(spyReader.hasNext());
        verify(spyReader).doPeek();
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNone_callsDoPeekAndReturnsFalseForEndTokens() throws Exception {
        setPeekedValue(0); // PEEKED_NONE

        JsonReader spyReader = spy(jsonReader);

        // PEEKED_END_OBJECT = 2
        doReturn(2).when(spyReader).doPeek();
        assertFalse(spyReader.hasNext());

        // PEEKED_END_ARRAY = 4
        doReturn(4).when(spyReader).doPeek();
        assertFalse(spyReader.hasNext());

        // PEEKED_EOF = 17
        doReturn(17).when(spyReader).doPeek();
        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsEndTokens_returnsFalse() throws Exception {
        setPeekedValue(2); // PEEKED_END_OBJECT
        assertFalse(jsonReader.hasNext());
        setPeekedValue(4); // PEEKED_END_ARRAY
        assertFalse(jsonReader.hasNext());
        setPeekedValue(17); // PEEKED_EOF
        assertFalse(jsonReader.hasNext());
    }

    private void setPeekedValue(int value) throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        peekedField.setInt(jsonReader, value);
    }
}