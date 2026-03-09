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

public class JsonReader_193_1Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_EOF = 17;

    @BeforeEach
    public void setup() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNotNoneAndNotEndTokens() throws IOException {
        setPeeked(5); // PEEKED_TRUE (some token not end or eof)
        assertTrue(jsonReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNone_doPeekReturnsNotEndTokens() throws Exception {
        setPeeked(PEEKED_NONE);

        Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
        doPeek.setAccessible(true);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_BEGIN_OBJECT).when(spyReader).doPeek();

        setPeeked(spyReader, PEEKED_NONE);

        assertTrue(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNone_doPeekReturnsEndObject() throws Exception {
        setPeeked(PEEKED_NONE);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_END_OBJECT).when(spyReader).doPeek();

        setPeeked(spyReader, PEEKED_NONE);

        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNone_doPeekReturnsEndArray() throws Exception {
        setPeeked(PEEKED_NONE);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_END_ARRAY).when(spyReader).doPeek();

        setPeeked(spyReader, PEEKED_NONE);

        assertFalse(spyReader.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testHasNext_peekedIsNone_doPeekReturnsEOF() throws Exception {
        setPeeked(PEEKED_NONE);

        JsonReader spyReader = spy(jsonReader);
        doReturn(PEEKED_EOF).when(spyReader).doPeek();

        setPeeked(spyReader, PEEKED_NONE);

        assertFalse(spyReader.hasNext());
    }

    private void setPeeked(int value) {
        try {
            Field peekedField = JsonReader.class.getDeclaredField("peeked");
            peekedField.setAccessible(true);
            peekedField.setInt(jsonReader, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPeeked(JsonReader readerInstance, int value) {
        try {
            Field peekedField = JsonReader.class.getDeclaredField("peeked");
            peekedField.setAccessible(true);
            peekedField.setInt(readerInstance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}