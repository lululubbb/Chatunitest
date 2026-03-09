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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.stream.JsonWriter;

public class JsonWriter_384_3Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testReplaceTop_replacesTopOfStack() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Prepare stack and stackSize via reflection
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = 5;
        stack[1] = 10;
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 2);

        // Access private method replaceTop
        Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
        replaceTop.setAccessible(true);

        // Replace top with a new value
        replaceTop.invoke(jsonWriter, 42);

        // Verify stack top is replaced
        int[] modifiedStack = (int[]) stackField.get(jsonWriter);
        int stackSize = stackSizeField.getInt(jsonWriter);
        assertEquals(2, stackSize);
        assertEquals(42, modifiedStack[stackSize - 1]);
        assertEquals(5, modifiedStack[0]); // unchanged below top
    }

    @Test
    @Timeout(8000)
    public void testReplaceTop_withStackSizeOne() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Setup stack with one element
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stack[0] = 7;
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 1);

        Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
        replaceTop.setAccessible(true);

        replaceTop.invoke(jsonWriter, 99);

        int[] modifiedStack = (int[]) stackField.get(jsonWriter);
        int stackSize = stackSizeField.getInt(jsonWriter);
        assertEquals(1, stackSize);
        assertEquals(99, modifiedStack[0]);
    }

    @Test
    @Timeout(8000)
    public void testReplaceTop_withStackSizeZero_noChange() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Setup stackSize 0 (empty stack)
        Field stackField = JsonWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        int[] stack = new int[32];
        stackField.set(jsonWriter, stack);

        Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
        stackSizeField.setAccessible(true);
        stackSizeField.setInt(jsonWriter, 0);

        Method replaceTop = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
        replaceTop.setAccessible(true);

        // Since stackSize is 0, the method will try to assign stack[-1] = topOfStack, which would throw ArrayIndexOutOfBoundsException
        assertThrows(InvocationTargetException.class, () -> {
            replaceTop.invoke(jsonWriter, 123);
        });
    }
}