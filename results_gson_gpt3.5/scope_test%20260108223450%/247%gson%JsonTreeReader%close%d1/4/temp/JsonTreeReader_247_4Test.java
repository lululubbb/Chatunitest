package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.internal.bind.JsonTreeReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

public class JsonTreeReader_247_4Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a minimal JsonElement mock since constructor requires it
        JsonElement mockElement = mock(JsonElement.class);
        jsonTreeReader = new JsonTreeReader(mockElement);
    }

    @Test
    @Timeout(8000)
    public void testClose_setsStackToSentinelClosedAndStackSizeOne() throws Exception {
        // Use reflection to set stack and stackSize to known values before close
        Field stackField = JsonTreeReader.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        Object[] initialStack = new Object[32];
        initialStack[0] = new Object();
        stackField.set(jsonTreeReader, initialStack);

        Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonTreeReader, 10);

        jsonTreeReader.close();

        Object[] stackAfterClose = (Object[]) stackField.get(jsonTreeReader);
        int stackSizeAfterClose = stackSizeField.getInt(jsonTreeReader);

        // Verify stackSize is 1
        assertEquals(1, stackSizeAfterClose);

        // Verify stack[0] is SENTINEL_CLOSED
        Field sentinelClosedField = JsonTreeReader.class.getDeclaredField("SENTINEL_CLOSED");
        sentinelClosedField.setAccessible(true);
        Object sentinelClosed = sentinelClosedField.get(null);

        assertSame(sentinelClosed, stackAfterClose[0]);
    }
}