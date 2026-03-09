package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonReader_186_2Test {

    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_withNullReader_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new JsonReader(null);
        });
        assertEquals("in == null", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_withValidReader_initializesFields() throws Exception {
        JsonReader jsonReader = new JsonReader(mockReader);

        // Use reflection to check private fields
        var inField = JsonReader.class.getDeclaredField("in");
        inField.setAccessible(true);
        assertSame(mockReader, inField.get(jsonReader));

        var lenientField = JsonReader.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        assertFalse(lenientField.getBoolean(jsonReader));

        var bufferField = JsonReader.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        char[] buffer = (char[]) bufferField.get(jsonReader);
        assertNotNull(buffer);
        assertEquals(1024, buffer.length);

        var posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        assertEquals(0, posField.getInt(jsonReader));

        var limitField = JsonReader.class.getDeclaredField("limit");
        limitField.setAccessible(true);
        assertEquals(0, limitField.getInt(jsonReader));

        var lineNumberField = JsonReader.class.getDeclaredField("lineNumber");
        lineNumberField.setAccessible(true);
        assertEquals(0, lineNumberField.getInt(jsonReader));

        var lineStartField = JsonReader.class.getDeclaredField("lineStart");
        lineStartField.setAccessible(true);
        assertEquals(0, lineStartField.getInt(jsonReader));

        var peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        assertEquals(1, peekedField.getInt(jsonReader)); // PEEKED_BEGIN_OBJECT == 1

        var peekedLongField = JsonReader.class.getDeclaredField("peekedLong");
        peekedLongField.setAccessible(true);
        assertEquals(0L, peekedLongField.getLong(jsonReader));

        var peekedNumberLengthField = JsonReader.class.getDeclaredField("peekedNumberLength");
        peekedNumberLengthField.setAccessible(true);
        assertEquals(0, peekedNumberLengthField.getInt(jsonReader));

        var peekedStringField = JsonReader.class.getDeclaredField("peekedString");
        peekedStringField.setAccessible(true);
        assertNull(peekedStringField.get(jsonReader));

        var stackField = JsonReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = (int[]) stackField.get(jsonReader);
        assertNotNull(stack);
        assertEquals(32, stack.length);

        var stackSizeField = JsonReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        assertEquals(1, stackSizeField.getInt(jsonReader)); // stackSize initialized to 1

        var pathNamesField = JsonReader.class.getDeclaredField("pathNames");
        pathNamesField.setAccessible(true);
        String[] pathNames = (String[]) pathNamesField.get(jsonReader);
        assertNotNull(pathNames);
        assertEquals(32, pathNames.length);

        var pathIndicesField = JsonReader.class.getDeclaredField("pathIndices");
        pathIndicesField.setAccessible(true);
        int[] pathIndices = (int[]) pathIndicesField.get(jsonReader);
        assertNotNull(pathIndices);
        assertEquals(32, pathIndices.length);
    }
}