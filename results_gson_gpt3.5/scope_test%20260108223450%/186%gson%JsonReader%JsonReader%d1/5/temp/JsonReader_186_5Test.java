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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JsonReader_186_5Test {

    @Test
    @Timeout(8000)
    @DisplayName("Constructor should initialize JsonReader with non-null Reader")
    void constructor_WithValidReader_ShouldInitialize() throws Exception {
        Reader mockReader = mock(Reader.class);
        JsonReader jsonReader = new JsonReader(mockReader);

        // Use reflection to verify private fields
        var inField = JsonReader.class.getDeclaredField("in");
        inField.setAccessible(true);
        Object inValue = inField.get(jsonReader);
        assertSame(mockReader, inValue);

        var posField = JsonReader.class.getDeclaredField("pos");
        posField.setAccessible(true);
        assertEquals(0, posField.getInt(jsonReader));

        var limitField = JsonReader.class.getDeclaredField("limit");
        limitField.setAccessible(true);
        assertEquals(0, limitField.getInt(jsonReader));

        var peekedField = JsonReader.class.getDeclaredField("peeked");
        peekedField.setAccessible(true);
        assertEquals(0, peekedField.getInt(jsonReader)); // PEEKED_NONE = 0
    }

    @Test
    @Timeout(8000)
    @DisplayName("Constructor with null Reader should throw NullPointerException")
    void constructor_WithNullReader_ShouldThrow() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new JsonReader(null));
        assertEquals("in == null", npe.getMessage());
    }
}