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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonReader_192_5Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenPeekedIsPeekedEndObject() throws Exception {
        setField(jsonReader, "peeked", 2); // PEEKED_END_OBJECT = 2
        setField(jsonReader, "stackSize", 2);
        String[] pathNames = new String[32];
        pathNames[1] = "key";
        setField(jsonReader, "pathNames", pathNames);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(jsonReader, "pathIndices", pathIndices);

        jsonReader.endObject();

        assertEquals(1, (int) getField(jsonReader, "stackSize"));
        assertNull(pathNames[1]);
        assertEquals(1, pathIndices[0]);
        assertEquals(0, (int) getField(jsonReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenPeekedIsPeekedNoneAndDoPeekReturnsPeekedEndObject() throws Exception {
        setField(jsonReader, "peeked", 0); // PEEKED_NONE = 0
        setField(jsonReader, "stackSize", 2);
        String[] pathNames = new String[32];
        pathNames[1] = "key";
        setField(jsonReader, "pathNames", pathNames);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(jsonReader, "pathIndices", pathIndices);

        // We cannot directly mock private method with Mockito 3 easily, so use spy and override doPeek via subclass:
        JsonReader testReader = new JsonReader(mockReader) {
            {
                setField(this, "peeked", 0);
                setField(this, "stackSize", 2);
                setField(this, "pathNames", pathNames);
                setField(this, "pathIndices", pathIndices);
            }
            @Override
            int doPeek() {
                return 2; // PEEKED_END_OBJECT
            }
        };

        testReader.endObject();

        assertEquals(1, (int) getField(testReader, "stackSize"));
        assertNull(pathNames[1]);
        assertEquals(1, pathIndices[0]);
        assertEquals(0, (int) getField(testReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    public void testEndObject_whenPeekedIsNotEndObject_throwsIllegalStateException() throws Exception {
        setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT = 1

        // Mock peek() to return a token string
        JsonReader spyReader = Mockito.spy(jsonReader);
        doReturn(JsonToken.BEGIN_OBJECT).when(spyReader).peek();

        Exception exception = assertThrows(IllegalStateException.class, spyReader::endObject);
        String message = exception.getMessage();
        assertTrue(message.contains("Expected END_OBJECT but was"));
        assertTrue(message.contains(JsonToken.BEGIN_OBJECT.toString()));
    }

    // Helper methods to access private fields via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private <T> T getField(Object target, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }
}