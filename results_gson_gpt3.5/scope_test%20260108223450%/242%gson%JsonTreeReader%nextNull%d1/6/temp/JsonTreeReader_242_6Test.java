package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_242_6Test {

    private JsonTreeReader jsonTreeReader;
    private Method nextNullMethod;
    private Method expectMethod;
    private Method popStackMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, Exception {
        // Create a JsonNull element to pass to constructor
        jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);

        // Access private methods expect(JsonToken) and popStack()
        expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        popStackMethod = JsonTreeReader.class.getDeclaredMethod("popStack");
        popStackMethod.setAccessible(true);

        nextNullMethod = JsonTreeReader.class.getDeclaredMethod("nextNull");
        nextNullMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testNextNull_withStackSizeGreaterThanZero_incrementsPathIndices() throws Throwable {
        // Setup internal state with stackSize = 2 and pathIndices array
        setField(jsonTreeReader, "stackSize", 2);
        int[] pathIndices = new int[32];
        pathIndices[0] = 5;
        setField(jsonTreeReader, "pathIndices", pathIndices);

        // Override expect(JsonToken.NULL) to do nothing via reflection proxy
        JsonTreeReader spyReader = createSpyWithOverriddenExpect(jsonTreeReader);

        // Override popStack() to decrement stackSize and return top element via reflection proxy
        spyReader = createSpyWithOverriddenPopStack(spyReader);

        // Call nextNull via reflection
        nextNullMethod.invoke(spyReader);

        // Verify pathIndices[stackSize - 1] incremented by 1
        int[] updatedPathIndices = (int[]) getField(spyReader, "pathIndices");
        assertEquals(6, updatedPathIndices[1]); // index 1 because stackSize was 2

        // Verify stackSize is decremented by popStack()
        int stackSize = (int) getField(spyReader, "stackSize");
        assertEquals(1, stackSize);
    }

    @Test
    @Timeout(8000)
    public void testNextNull_withStackSizeEqualsZero_doesNotIncrementPathIndices() throws Throwable {
        // Setup internal state with stackSize = 0
        setField(jsonTreeReader, "stackSize", 0);
        int[] pathIndices = new int[32];
        setField(jsonTreeReader, "pathIndices", pathIndices);

        // Override expect(JsonToken.NULL) to do nothing via reflection proxy
        JsonTreeReader spyReader = createSpyWithOverriddenExpect(jsonTreeReader);

        // Override popStack() to decrement stackSize and return top element via reflection proxy
        spyReader = createSpyWithOverriddenPopStack(spyReader);

        // Call nextNull via reflection
        nextNullMethod.invoke(spyReader);

        // Verify pathIndices not incremented (all zero)
        int[] updatedPathIndices = (int[]) getField(spyReader, "pathIndices");
        for (int val : updatedPathIndices) {
            assertEquals(0, val);
        }

        // Verify stackSize decremented by popStack()
        int stackSize = (int) getField(spyReader, "stackSize");
        // starting from 0, popStack should throw or do nothing, so stackSize remains 0
        assertTrue(stackSize <= 0);
    }

    @Test
    @Timeout(8000)
    public void testNextNull_expectThrowsException() throws Throwable {
        // Setup stackSize > 0 so pathIndices increment would occur if no exception
        setField(jsonTreeReader, "stackSize", 1);
        int[] pathIndices = new int[32];
        setField(jsonTreeReader, "pathIndices", pathIndices);

        // Create a spy of jsonTreeReader to mock expect to throw IOException via reflection proxy
        JsonTreeReader spyReader = spy(jsonTreeReader);

        // Use reflection to get private expect method
        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        // Use Mockito doThrow on a public wrapper to invoke private expect
        // Create a wrapper method to call expect(JsonToken)
        Method expectWrapper = createExpectWrapper(spyReader);

        // Mock the wrapper method to throw IOException
        doThrow(new IOException("Expected token missing")).when(spyReader).expectWrapper(JsonToken.NULL);

        // Replace the call to expect in nextNull with our wrapper via reflection proxy
        JsonTreeReader proxyReader = createProxyWithExpectWrapper(spyReader);

        // Use reflection to invoke nextNull on proxyReader
        Method nextNull = JsonTreeReader.class.getDeclaredMethod("nextNull");
        nextNull.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> nextNull.invoke(proxyReader));
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof IOException);
        assertEquals("Expected token missing", cause.getMessage());
    }

    // Helper method to set private field by reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    // Helper method to get private field by reflection
    private static Object getField(Object target, String fieldName) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    // Helper method to create a spy with expect(JsonToken) overridden to do nothing
    private static JsonTreeReader createSpyWithOverriddenExpect(JsonTreeReader reader) throws Exception {
        JsonTreeReader spyReader = spy(reader);

        // Use reflection to get expect method
        Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        // Use Mockito's doAnswer on the spy's expect method via reflection proxy
        // Because expect is private, use reflection to invoke doAnswer on the spy's expect
        // Instead, use Mockito's inline mock maker to mock private method via reflection

        // Use Mockito's doAnswer to override private method via reflection proxy
        // This requires usage of Mockito's inline mock maker or other advanced features.
        // Since direct mocking private method is not allowed, use a workaround:
        // Create a subclass that overrides expect to do nothing, then spy on that.

        JsonTreeReader subclass = new JsonTreeReader(JsonNull.INSTANCE) {
            @Override
            protected void expect(JsonToken expected) {
                // do nothing
            }
        };

        // Copy state from original spyReader to subclass
        copyAllFields(spyReader, subclass);

        return spy(subclass);
    }

    // Helper method to create a spy with popStack() overridden to decrement stackSize and return top element
    private static JsonTreeReader createSpyWithOverriddenPopStack(JsonTreeReader reader) throws Exception {
        // Similar approach: subclass and override popStack()
        JsonTreeReader subclass = new JsonTreeReader(JsonNull.INSTANCE) {
            @Override
            protected Object popStack() {
                try {
                    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
                    stackSizeField.setAccessible(true);
                    int stackSize = stackSizeField.getInt(this);
                    if (stackSize == 0) {
                        throw new IllegalStateException("Stack empty");
                    }
                    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
                    stackField.setAccessible(true);
                    Object[] stack = (Object[]) stackField.get(this);
                    Object top = stack[stackSize - 1];
                    stackSizeField.setInt(this, stackSize - 1);
                    return top;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Copy state from original reader to subclass
        copyAllFields(reader, subclass);

        return spy(subclass);
    }

    // Helper method to copy all declared fields from source to target
    private static void copyAllFields(Object source, Object target) throws Exception {
        Field[] fields = JsonTreeReader.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(source);
            field.set(target, value);
        }
    }

    // Create a wrapper method expectWrapper(JsonToken) in spyReader to call private expect(JsonToken)
    // This is done via reflection and dynamic proxy to allow mocking expect indirectly
    private static Method createExpectWrapper(JsonTreeReader spyReader) throws Exception {
        // Add a public method expectWrapper(JsonToken) via a dynamic proxy or subclass
        // Since Java doesn't allow adding methods at runtime, create a subclass with this method

        class Wrapper extends JsonTreeReader {
            Wrapper(JsonElement e) {
                super(e);
            }

            public void expectWrapper(JsonToken token) throws IOException {
                try {
                    Method expect = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
                    expect.setAccessible(true);
                    expect.invoke(this, token);
                } catch (InvocationTargetException ite) {
                    Throwable cause = ite.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw new RuntimeException(cause);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        Wrapper wrapper = new Wrapper(JsonNull.INSTANCE);
        copyAllFields(spyReader, wrapper);
        // We need to spy on wrapper to mock expectWrapper
        JsonTreeReader spyWrapper = spy(wrapper);

        // Replace original spyReader reference with spyWrapper in test
        // This helper method returns the expectWrapper method for mocking
        return JsonTreeReader.class.getMethod("expectWrapper", JsonToken.class);
    }

    // Create a proxy that replaces calls to expect(JsonToken) with expectWrapper(JsonToken)
    private static JsonTreeReader createProxyWithExpectWrapper(JsonTreeReader spyReader) throws Exception {
        // Because we cannot modify nextNull method to call expectWrapper,
        // we create a subclass overriding nextNull to call expectWrapper instead of expect

        class ProxyReader extends JsonTreeReader {
            ProxyReader(JsonElement e) {
                super(e);
            }

            @Override
            public void nextNull() throws IOException {
                try {
                    Method expectWrapper = this.getClass().getMethod("expectWrapper", JsonToken.class);
                    expectWrapper.invoke(this, JsonToken.NULL);
                    Method popStack = JsonTreeReader.class.getDeclaredMethod("popStack");
                    popStack.setAccessible(true);
                    popStack.invoke(this);
                    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
                    stackSizeField.setAccessible(true);
                    int stackSize = stackSizeField.getInt(this);
                    if (stackSize > 0) {
                        Field pathIndicesField = JsonTreeReader.class.getDeclaredField("pathIndices");
                        pathIndicesField.setAccessible(true);
                        int[] pathIndices = (int[]) pathIndicesField.get(this);
                        pathIndices[stackSize - 1]++;
                    }
                } catch (InvocationTargetException ite) {
                    Throwable cause = ite.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw new RuntimeException(cause);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            public void expectWrapper(JsonToken token) throws IOException {
                try {
                    Method expect = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
                    expect.setAccessible(true);
                    expect.invoke(this, token);
                } catch (InvocationTargetException ite) {
                    Throwable cause = ite.getCause();
                    if (cause instanceof IOException) {
                        throw (IOException) cause;
                    }
                    throw new RuntimeException(cause);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        ProxyReader proxy = new ProxyReader(JsonNull.INSTANCE);
        copyAllFields(spyReader, proxy);
        return spy(proxy);
    }
}