package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_383_3Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void peek_stackSizeZero_throwsIllegalStateException() throws Exception {
        // Use reflection to set stackSize to 0 explicitly (should be 0 by default)
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                peekMethod.invoke(jsonWriter);
            } catch (InvocationTargetException e) {
                // Rethrow the cause to be caught by assertThrows
                throw e.getCause();
            }
        });
        assertEquals("JsonWriter is closed.", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void peek_stackSizePositive_returnsTopOfStack() throws Exception {
        // Set stackSize to 3
        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 3);

        // Create stack array and set values
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stackArray = new int[32];
        stackArray[0] = 10;
        stackArray[1] = 20;
        stackArray[2] = 30; // top of stack for stackSize=3
        stackField.set(jsonWriter, stackArray);

        Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        int result = (int) peekMethod.invoke(jsonWriter);

        assertEquals(30, result);
    }
}