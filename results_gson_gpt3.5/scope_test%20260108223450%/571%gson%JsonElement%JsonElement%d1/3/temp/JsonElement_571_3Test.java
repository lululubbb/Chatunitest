package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonElement_571_3Test {

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
  public void testConstructorDeprecated() throws Exception {
    JsonElement element = new JsonElementImpl();
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    JsonElement element = new JsonElementImpl();
    JsonElement copy = element.deepCopy();
    assertSame(element, copy);
  }

  @Test
    @Timeout(8000)
  public void testIsMethodsReturnFalse() {
    JsonElement element = new JsonElementImpl();
    assertFalse(element.isJsonArray());
    assertFalse(element.isJsonObject());
    assertFalse(element.isJsonPrimitive());
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testGetAsMethodsThrowUnsupportedOperationException() {
    JsonElement element = new JsonElementImpl();

    assertThrows(UnsupportedOperationException.class, element::getAsJsonObject);
    assertThrows(UnsupportedOperationException.class, element::getAsJsonArray);
    assertThrows(UnsupportedOperationException.class, element::getAsJsonPrimitive);
    assertThrows(UnsupportedOperationException.class, element::getAsJsonNull);
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
  public void testToString() {
    JsonElement element = new JsonElementImpl();
    assertEquals("JsonElementImpl", element.toString());
  }

  @Test
    @Timeout(8000)
  public void testInvokePrivateMethodUsingReflection() throws Exception {
    // There are no private methods declared in JsonElement according to provided info.
    // But test invoking toString via reflection as example.
    JsonElement element = new JsonElementImpl();
    Method toStringMethod = JsonElement.class.getDeclaredMethod("toString");
    toStringMethod.setAccessible(true);
    String result = (String) toStringMethod.invoke(element);
    assertEquals("JsonElementImpl", result);
  }
}