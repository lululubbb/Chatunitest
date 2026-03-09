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

public class JsonReader_194_1Test {

    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() {
        Reader mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testPeek_whenPeekedIsNotNone_shouldReturnCorrespondingJsonToken() throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);

        // Test each peeked value except PEEKED_NONE and default (invalid)
        // PEEKED_BEGIN_OBJECT = 1 -> BEGIN_OBJECT
        peekedField.setInt(jsonReader, 1);
        assertEquals(JsonToken.BEGIN_OBJECT, jsonReader.peek());

        // PEEKED_END_OBJECT = 2 -> END_OBJECT
        peekedField.setInt(jsonReader, 2);
        assertEquals(JsonToken.END_OBJECT, jsonReader.peek());

        // PEEKED_BEGIN_ARRAY = 3 -> BEGIN_ARRAY
        peekedField.setInt(jsonReader, 3);
        assertEquals(JsonToken.BEGIN_ARRAY, jsonReader.peek());

        // PEEKED_END_ARRAY = 4 -> END_ARRAY
        peekedField.setInt(jsonReader, 4);
        assertEquals(JsonToken.END_ARRAY, jsonReader.peek());

        // PEEKED_SINGLE_QUOTED_NAME = 12, PEEKED_DOUBLE_QUOTED_NAME = 13, PEEKED_UNQUOTED_NAME = 14 -> NAME
        peekedField.setInt(jsonReader, 12);
        assertEquals(JsonToken.NAME, jsonReader.peek());
        peekedField.setInt(jsonReader, 13);
        assertEquals(JsonToken.NAME, jsonReader.peek());
        peekedField.setInt(jsonReader, 14);
        assertEquals(JsonToken.NAME, jsonReader.peek());

        // PEEKED_TRUE = 5, PEEKED_FALSE = 6 -> BOOLEAN
        peekedField.setInt(jsonReader, 5);
        assertEquals(JsonToken.BOOLEAN, jsonReader.peek());
        peekedField.setInt(jsonReader, 6);
        assertEquals(JsonToken.BOOLEAN, jsonReader.peek());

        // PEEKED_NULL = 7 -> NULL
        peekedField.setInt(jsonReader, 7);
        assertEquals(JsonToken.NULL, jsonReader.peek());

        // PEEKED_SINGLE_QUOTED = 8, PEEKED_DOUBLE_QUOTED = 9, PEEKED_UNQUOTED = 10, PEEKED_BUFFERED = 11 -> STRING
        peekedField.setInt(jsonReader, 8);
        assertEquals(JsonToken.STRING, jsonReader.peek());
        peekedField.setInt(jsonReader, 9);
        assertEquals(JsonToken.STRING, jsonReader.peek());
        peekedField.setInt(jsonReader, 10);
        assertEquals(JsonToken.STRING, jsonReader.peek());
        peekedField.setInt(jsonReader, 11);
        assertEquals(JsonToken.STRING, jsonReader.peek());

        // PEEKED_LONG = 15, PEEKED_NUMBER = 16 -> NUMBER
        peekedField.setInt(jsonReader, 15);
        assertEquals(JsonToken.NUMBER, jsonReader.peek());
        peekedField.setInt(jsonReader, 16);
        assertEquals(JsonToken.NUMBER, jsonReader.peek());

        // PEEKED_EOF = 17 -> END_DOCUMENT
        peekedField.setInt(jsonReader, 17);
        assertEquals(JsonToken.END_DOCUMENT, jsonReader.peek());
    }

    @Test
    @Timeout(8000)
    public void testPeek_whenPeekedIsNone_callsDoPeekAndReturnsCorrectToken() throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);

        // Set peeked to PEEKED_NONE (0) to trigger doPeek call
        peekedField.setInt(jsonReader, 0);

        // Spy on jsonReader to mock doPeek method
        JsonReader spyReader = spy(jsonReader);

        // Use reflection to get doPeek method
        Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
        doPeekMethod.setAccessible(true);

        // Mock doPeek to return each peeked value and verify peek returns correct JsonToken
        for (int peekedValue = 1; peekedValue <= 17; peekedValue++) {
            if (peekedValue == 0) continue; // skip PEEKED_NONE

            doReturn(peekedValue).when(spyReader).doPeek();
            // Set peeked to 0 to force doPeek call
            peekedField.setInt(spyReader, 0);

            JsonToken expectedToken;
            switch (peekedValue) {
                case 1:
                    expectedToken = JsonToken.BEGIN_OBJECT;
                    break;
                case 2:
                    expectedToken = JsonToken.END_OBJECT;
                    break;
                case 3:
                    expectedToken = JsonToken.BEGIN_ARRAY;
                    break;
                case 4:
                    expectedToken = JsonToken.END_ARRAY;
                    break;
                case 5:
                case 6:
                    expectedToken = JsonToken.BOOLEAN;
                    break;
                case 7:
                    expectedToken = JsonToken.NULL;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    expectedToken = JsonToken.STRING;
                    break;
                case 12:
                case 13:
                case 14:
                    expectedToken = JsonToken.NAME;
                    break;
                case 15:
                case 16:
                    expectedToken = JsonToken.NUMBER;
                    break;
                case 17:
                    expectedToken = JsonToken.END_DOCUMENT;
                    break;
                default:
                    expectedToken = null; // will cause AssertionError
            }

            if (expectedToken != null) {
                assertEquals(expectedToken, spyReader.peek());
            }
        }
    }

    @Test
    @Timeout(8000)
    public void testPeek_whenPeekedHasInvalidValue_shouldThrowAssertionError() throws Exception {
        Field peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);

        // Set peeked to an invalid value outside defined constants
        peekedField.setInt(jsonReader, 999);

        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            jsonReader.peek();
        });
        assertNotNull(thrown);
    }
}