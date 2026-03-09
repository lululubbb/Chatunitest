package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonElement_571_6Test {

  // Since JsonElement is abstract, create a minimal concrete subclass for testing
  static class JsonElementImpl extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public boolean isJsonArray() {
      return false;
    }

    @Override
    public boolean isJsonObject() {
      return false;
    }

    @Override
    public boolean isJsonPrimitive() {
      return false;
    }

    @Override
    public boolean isJsonNull() {
      return false;
    }

    @Override
    public JsonObject getAsJsonObject() {
      throw new UnsupportedOperationException();
    }

    @Override
    public JsonArray getAsJsonArray() {
      throw new UnsupportedOperationException();
    }

    @Override
    public JsonPrimitive getAsJsonPrimitive() {
      throw new UnsupportedOperationException();
    }

    @Override
    public JsonNull getAsJsonNull() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAsBoolean() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Number getAsNumber() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getAsString() {
      throw new UnsupportedOperationException();
    }

    @Override
    public double getAsDouble() {
      throw new UnsupportedOperationException();
    }

    @Override
    public float getAsFloat() {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getAsLong() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int getAsInt() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte getAsByte() {
      throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public char getAsCharacter() {
      throw new UnsupportedOperationException();
    }

    @Override
    public BigDecimal getAsBigDecimal() {
      throw new UnsupportedOperationException();
    }

    @Override
    public BigInteger getAsBigInteger() {
      throw new UnsupportedOperationException();
    }

    @Override
    public short getAsShort() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
      return "JsonElementImpl";
    }
  }

  @Test
    @Timeout(8000)
  public void testConstructorDeprecated() {
    JsonElement element = new JsonElementImpl();
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    JsonElementImpl element = new JsonElementImpl();
    JsonElement copy = element.deepCopy();
    assertSame(element, copy);
  }

  @Test
    @Timeout(8000)
  public void testIsJsonTypeMethods() {
    JsonElementImpl element = new JsonElementImpl();
    assertFalse(element.isJsonArray());
    assertFalse(element.isJsonObject());
    assertFalse(element.isJsonPrimitive());
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonMethodsUnsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonObject);
    assertThrows(UnsupportedOperationException.class, element::getAsJsonArray);
    assertThrows(UnsupportedOperationException.class, element::getAsJsonPrimitive);
    assertThrows(UnsupportedOperationException.class, element::getAsJsonNull);
  }

  @Test
    @Timeout(8000)
  public void testGetAsPrimitiveMethodsUnsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
    assertThrows(UnsupportedOperationException.class, element::getAsNumber);
    assertThrows(UnsupportedOperationException.class, element::getAsString);
    assertThrows(UnsupportedOperationException.class, element::getAsDouble);
    assertThrows(UnsupportedOperationException.class, element::getAsFloat);
    assertThrows(UnsupportedOperationException.class, element::getAsLong);
    assertThrows(UnsupportedOperationException.class, element::getAsInt);
    assertThrows(UnsupportedOperationException.class, element::getAsByte);
    assertThrows(UnsupportedOperationException.class, element::getAsCharacter);
    assertThrows(UnsupportedOperationException.class, element::getAsBigDecimal);
    assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
    assertThrows(UnsupportedOperationException.class, element::getAsShort);
  }

  @Test
    @Timeout(8000)
  public void testToStringOverride() {
    JsonElementImpl element = new JsonElementImpl();
    assertEquals("JsonElementImpl", element.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_UsesStreamsAndJsonWriter() throws Exception {
    // We need to test the toString method of some JsonElement subclass that uses Streams.write()
    // but JsonElement.toString() is abstract here, so we mock a subclass and use reflection to invoke toString

    JsonElement element = mock(JsonElement.class, CALLS_REAL_METHODS);

    // Mock Streams.write to write a specific string to the writer
    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(element), any(JsonWriter.class))).thenAnswer(invocation -> {
        JsonWriter writer = invocation.getArgument(1);
        writer.jsonValue("\"mocked\"");
        return null;
      });

      // Use reflection to invoke toString (which is public, but we want to ensure coverage)
      Method toStringMethod = JsonElement.class.getDeclaredMethod("toString");
      toStringMethod.setAccessible(true);
      String result = (String) toStringMethod.invoke(element);

      assertEquals("\"mocked\"", result);

      streamsMockedStatic.verify(() -> Streams.write(eq(element), any(JsonWriter.class)));
    }
  }

  @Test
    @Timeout(8000)
  public void testToString_IOExceptionHandled() throws Exception {
    // First, test that the default toString throws IOException wrapped in InvocationTargetException
    JsonElement element = mock(JsonElement.class, CALLS_REAL_METHODS);

    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(element), any(JsonWriter.class))).thenThrow(new IOException("test exception"));

      Method toStringMethod = JsonElement.class.getDeclaredMethod("toString");
      toStringMethod.setAccessible(true);

      try {
        toStringMethod.invoke(element);
        fail("Expected IOException to be thrown");
      } catch (java.lang.reflect.InvocationTargetException e) {
        Throwable cause = e.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IOException);
        assertEquals("test exception", cause.getMessage());
      }
    }

    // Then, test a subclass that overrides toString and handles IOException properly
    class JsonElementWithToStringHandling extends JsonElement {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public boolean isJsonArray() { return false; }

      @Override
      public boolean isJsonObject() { return false; }

      @Override
      public boolean isJsonPrimitive() { return false; }

      @Override
      public boolean isJsonNull() { return false; }

      @Override
      public JsonObject getAsJsonObject() { throw new UnsupportedOperationException(); }

      @Override
      public JsonArray getAsJsonArray() { throw new UnsupportedOperationException(); }

      @Override
      public JsonPrimitive getAsJsonPrimitive() { throw new UnsupportedOperationException(); }

      @Override
      public JsonNull getAsJsonNull() { throw new UnsupportedOperationException(); }

      @Override
      public boolean getAsBoolean() { throw new UnsupportedOperationException(); }

      @Override
      public Number getAsNumber() { throw new UnsupportedOperationException(); }

      @Override
      public String getAsString() { throw new UnsupportedOperationException(); }

      @Override
      public double getAsDouble() { throw new UnsupportedOperationException(); }

      @Override
      public float getAsFloat() { throw new UnsupportedOperationException(); }

      @Override
      public long getAsLong() { throw new UnsupportedOperationException(); }

      @Override
      public int getAsInt() { throw new UnsupportedOperationException(); }

      @Override
      public byte getAsByte() { throw new UnsupportedOperationException(); }

      @Override
      @Deprecated
      public char getAsCharacter() { throw new UnsupportedOperationException(); }

      @Override
      public BigDecimal getAsBigDecimal() { throw new UnsupportedOperationException(); }

      @Override
      public BigInteger getAsBigInteger() { throw new UnsupportedOperationException(); }

      @Override
      public short getAsShort() { throw new UnsupportedOperationException(); }

      @Override
      public String toString() {
        try {
          StringWriter stringWriter = new StringWriter();
          JsonWriter jsonWriter = new JsonWriter(stringWriter);
          Streams.write(this, jsonWriter);
          jsonWriter.close();
          return stringWriter.toString();
        } catch (IOException e) {
          return getClass().getSimpleName() + "{" + e + "}";
        }
      }
    }

    JsonElementWithToStringHandling elementWithHandling = spy(new JsonElementWithToStringHandling());

    try (MockedStatic<Streams> streamsMockedStatic2 = mockStatic(Streams.class)) {
      streamsMockedStatic2.when(() -> Streams.write(eq(elementWithHandling), any(JsonWriter.class))).thenThrow(new IOException("test exception"));

      String handledResult = elementWithHandling.toString();

      assertNotNull(handledResult);
      assertTrue(handledResult.contains("JsonElementWithToStringHandling"));
      assertTrue(handledResult.contains("test exception"));

      streamsMockedStatic2.verify(() -> Streams.write(eq(elementWithHandling), any(JsonWriter.class)));
    }
  }
}