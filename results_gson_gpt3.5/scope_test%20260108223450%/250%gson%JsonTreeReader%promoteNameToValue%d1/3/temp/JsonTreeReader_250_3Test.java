package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

class JsonTreeReader_250_3Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  void setUp() throws Exception {
    // Create a dummy JsonElement to satisfy constructor (can be null since not used in test)
    jsonTreeReader = new JsonTreeReader(null);

    // Setup stack and stackSize using reflection
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);

    // Prepare a mocked Iterator<Map.Entry<?, ?>> to be placed on top of the stack
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<?, ?>> mockIterator = mock(Iterator.class);

    // Prepare a Map.Entry mock with key and value
    @SuppressWarnings("unchecked")
    Map.Entry<String, JsonPrimitive> mockEntry = mock(Map.Entry.class);
    when(mockEntry.getKey()).thenReturn("mockKey");
    when(mockEntry.getValue()).thenReturn(new JsonPrimitive("mockValue"));

    // Setup iterator.next() to return the mockEntry
    when(mockIterator.next()).thenReturn(mockEntry);

    // Place the mockIterator on top of the stack (index 0)
    stack[0] = mockIterator;
    stackSizeField.setInt(jsonTreeReader, 1);
  }

  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(target, value);
  }

  private Object getPrivateField(Object target, String fieldName) throws Exception {
    Field field = JsonTreeReader.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private void invokePrivateMethod(Object target, String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
    Method method = JsonTreeReader.class.getDeclaredMethod(methodName, paramTypes);
    method.setAccessible(true);
    method.invoke(target, args);
  }

  /**
   * Helper to invoke private void expect(JsonToken) method via reflection
   */
  private void expect(JsonTreeReader instance, JsonToken token) throws Exception {
    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);
    expectMethod.invoke(instance, token);
  }

  @Test
    @Timeout(8000)
  void testPromoteNameToValue() throws Exception {
    // Use reflection to access private method promoteNameToValue
    Method promoteNameToValue = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValue.setAccessible(true);

    // Spy the instance to allow partial mocking
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Stub the private expect method by reflection proxying
    // We cannot mock private methods directly with Mockito, so we create a spy subclass with overridden expect
    // Instead, we use a workaround: Use a dynamic proxy or reflection to replace expect method with no-op.
    // But here we can replace expect by reflection with a no-op method using a dynamic proxy is complex.
    // Instead, we use a helper to invoke promoteNameToValue on the spy, but intercept expect calls by reflection.

    // So we hack expect method to be accessible and override it via reflection.
    // But since expect is private and final, we cannot mock it with Mockito.
    // Instead, we create a subclass of JsonTreeReader with overridden expect for the test.

    // Create subclass with overridden expect to do nothing
    class JsonTreeReaderForTest extends JsonTreeReader {
      JsonTreeReaderForTest() {
        super(null);
        try {
          // Copy stack and stackSize from original jsonTreeReader
          Field stackField = JsonTreeReader.class.getDeclaredField("stack");
          stackField.setAccessible(true);
          stackField.set(this, stackField.get(jsonTreeReader));
          Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
          stackSizeField.setAccessible(true);
          stackSizeField.setInt(this, (int) stackSizeField.get(jsonTreeReader));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      @Override
      public void expect(JsonToken expected) {
        // do nothing
      }
    }

    JsonTreeReaderForTest testReader = new JsonTreeReaderForTest();

    // Invoke promoteNameToValue on testReader
    promoteNameToValue.invoke(testReader);

    // After promoteNameToValue, stackSize should increase by 2 (push called twice)
    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSizeAfter = stackSizeField.getInt(testReader);
    assertEquals(3, stackSizeAfter);

    // The top of the stack should be JsonPrimitive with key "mockKey"
    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(testReader);
    Object top = stack[stackSizeAfter - 1];
    assertTrue(top instanceof JsonPrimitive);
    assertEquals("mockKey", ((JsonPrimitive) top).getAsString());

    // The second top should be the value JsonPrimitive "mockValue"
    Object secondTop = stack[stackSizeAfter - 2];
    assertTrue(secondTop instanceof JsonPrimitive);
    assertEquals("mockValue", ((JsonPrimitive) secondTop).getAsString());
  }

  @Test
    @Timeout(8000)
  void testPromoteNameToValue_expectThrows() throws Exception {
    // Create subclass with overridden expect to throw IOException
    class JsonTreeReaderForTest extends JsonTreeReader {
      JsonTreeReaderForTest() {
        super(null);
        try {
          // Copy stack and stackSize from original jsonTreeReader
          Field stackField = JsonTreeReader.class.getDeclaredField("stack");
          stackField.setAccessible(true);
          stackField.set(this, stackField.get(jsonTreeReader));
          Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
          stackSizeField.setAccessible(true);
          stackSizeField.setInt(this, (int) stackSizeField.get(jsonTreeReader));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      @Override
      public void expect(JsonToken expected) throws IOException {
        throw new IOException("expect failure");
      }
    }

    JsonTreeReaderForTest testReader = new JsonTreeReaderForTest();

    Method promoteNameToValue = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValue.setAccessible(true);

    IOException thrown = assertThrows(IOException.class, () -> promoteNameToValue.invoke(testReader));
    assertTrue(thrown.getCause().getMessage().contains("expect failure"));
  }

  @Test
    @Timeout(8000)
  void testPromoteNameToValue_iteratorNextThrows() throws Exception {
    // Prepare stack with iterator that throws on next()
    @SuppressWarnings("unchecked")
    Iterator<Map.Entry<?, ?>> throwingIterator = mock(Iterator.class);
    when(throwingIterator.next()).thenThrow(new RuntimeException("next failure"));

    Field stackField = JsonTreeReader.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Object[] stack = (Object[]) stackField.get(jsonTreeReader);
    stack[0] = throwingIterator;

    Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonTreeReader, 1);

    // Create subclass with overridden expect to do nothing
    class JsonTreeReaderForTest extends JsonTreeReader {
      JsonTreeReaderForTest() {
        super(null);
        try {
          Field stackField = JsonTreeReader.class.getDeclaredField("stack");
          stackField.setAccessible(true);
          stackField.set(this, stack);
          Field stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
          stackSizeField.setAccessible(true);
          stackSizeField.setInt(this, 1);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
      @Override
      public void expect(JsonToken expected) {
        // do nothing
      }
    }

    JsonTreeReaderForTest testReader = new JsonTreeReaderForTest();

    Method promoteNameToValue = JsonTreeReader.class.getDeclaredMethod("promoteNameToValue");
    promoteNameToValue.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> promoteNameToValue.invoke(testReader));
    assertTrue(thrown.getCause().getMessage().contains("next failure"));
  }
}