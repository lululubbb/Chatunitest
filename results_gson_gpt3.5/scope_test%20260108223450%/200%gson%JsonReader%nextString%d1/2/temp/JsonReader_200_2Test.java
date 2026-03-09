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

public class JsonReader_200_2Test {

    private JsonReader reader;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
        reader = new JsonReader(mockReader);
        // Initialize stackSize to 1 to avoid ArrayIndexOutOfBounds in nextString()
        setField(reader, "stackSize", 1);
        // Initialize pathIndices array element 0 to 0
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(reader, "pathIndices", pathIndices);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_NONE_callsDoPeekAndReturnsUnquotedValue() throws Exception {
        setField(reader, "peeked", 0); // PEEKED_NONE
        // Mock doPeek() to return PEEKED_UNQUOTED (10)
        Method doPeek = JsonReader.class.getDeclaredMethod("doPeek");
        doPeek.setAccessible(true);
        JsonReader spyReader = spy(reader);
        doReturn(10).when(spyReader).doPeek();

        // Mock nextUnquotedValue() to return "unquoted"
        Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
        nextUnquotedValue.setAccessible(true);
        doReturn("unquoted").when(spyReader).nextUnquotedValue();

        setField(spyReader, "peeked", 0);
        setField(spyReader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(spyReader, "pathIndices", pathIndices);

        String result = spyReader.nextString();

        assertEquals("unquoted", result);
        assertEquals(0, getField(spyReader, "peeked"));
        assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_UNQUOTED_returnsNextUnquotedValue() throws Exception {
        setField(reader, "peeked", 10); // PEEKED_UNQUOTED
        Method nextUnquotedValue = JsonReader.class.getDeclaredMethod("nextUnquotedValue");
        nextUnquotedValue.setAccessible(true);
        JsonReader spyReader = spy(reader);
        doReturn("unquotedValue").when(spyReader).nextUnquotedValue();

        setField(spyReader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(spyReader, "pathIndices", pathIndices);

        String result = spyReader.nextString();

        assertEquals("unquotedValue", result);
        assertEquals(0, getField(spyReader, "peeked"));
        assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_SINGLE_QUOTED_returnsNextQuotedValue() throws Exception {
        setField(reader, "peeked", 8); // PEEKED_SINGLE_QUOTED
        JsonReader spyReader = spy(reader);
        Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
        nextQuotedValue.setAccessible(true);
        doReturn("singleQuotedValue").when(spyReader).nextQuotedValue('\'');

        setField(spyReader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(spyReader, "pathIndices", pathIndices);

        String result = spyReader.nextString();

        assertEquals("singleQuotedValue", result);
        assertEquals(0, getField(spyReader, "peeked"));
        assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_DOUBLE_QUOTED_returnsNextQuotedValue() throws Exception {
        setField(reader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
        JsonReader spyReader = spy(reader);
        Method nextQuotedValue = JsonReader.class.getDeclaredMethod("nextQuotedValue", char.class);
        nextQuotedValue.setAccessible(true);
        doReturn("doubleQuotedValue").when(spyReader).nextQuotedValue('"');

        setField(spyReader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(spyReader, "pathIndices", pathIndices);

        String result = spyReader.nextString();

        assertEquals("doubleQuotedValue", result);
        assertEquals(0, getField(spyReader, "peeked"));
        assertEquals(1, ((int[]) getField(spyReader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_BUFFERED_returnsPeekedStringAndClears() throws Exception {
        setField(reader, "peeked", 11); // PEEKED_BUFFERED
        setField(reader, "peekedString", "bufferedValue");
        setField(reader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(reader, "pathIndices", pathIndices);

        String result = reader.nextString();

        assertEquals("bufferedValue", result);
        assertNull(getField(reader, "peekedString"));
        assertEquals(0, getField(reader, "peeked"));
        assertEquals(1, ((int[]) getField(reader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_LONG_returnsLongAsString() throws Exception {
        setField(reader, "peeked", 15); // PEEKED_LONG
        setField(reader, "peekedLong", 123456789L);
        setField(reader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(reader, "pathIndices", pathIndices);

        String result = reader.nextString();

        assertEquals("123456789", result);
        assertEquals(0, getField(reader, "peeked"));
        assertEquals(1, ((int[]) getField(reader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_PEEKED_NUMBER_returnsStringFromBufferAndAdvancesPos() throws Exception {
        setField(reader, "peeked", 16); // PEEKED_NUMBER
        setField(reader, "peekedNumberLength", 3);
        setField(reader, "pos", 2);
        setField(reader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(reader, "pathIndices", pathIndices);
        char[] buffer = new char[1024];
        buffer[2] = '4';
        buffer[3] = '5';
        buffer[4] = '6';
        setField(reader, "buffer", buffer);

        String result = reader.nextString();

        assertEquals("456", result);
        assertEquals(5, getField(reader, "pos")); // pos advanced by peekedNumberLength (3)
        assertEquals(0, getField(reader, "peeked"));
        assertEquals(1, ((int[]) getField(reader, "pathIndices"))[0]);
    }

    @Test
    @Timeout(8000)
    public void testNextString_ThrowsIllegalStateExceptionForUnexpectedPeeked() throws Exception {
        setField(reader, "peeked", 5); // PEEKED_TRUE, not a string
        setField(reader, "stackSize", 1);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(reader, "pathIndices", pathIndices);

        Method peek = JsonReader.class.getDeclaredMethod("peek");
        peek.setAccessible(true);
        JsonReader spyReader = spy(reader);
        doReturn(JsonToken.TRUE).when(spyReader).peek();

        Method locationString = JsonReader.class.getDeclaredMethod("locationString");
        locationString.setAccessible(true);
        doReturn(" at line 1 column 1").when(spyReader).locationString();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, spyReader::nextString);
        assertTrue(thrown.getMessage().contains("Expected a string but was TRUE at line 1 column 1"));
    }

    // Helper methods to set/get private fields via reflection
    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T getField(Object target, String fieldName) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}