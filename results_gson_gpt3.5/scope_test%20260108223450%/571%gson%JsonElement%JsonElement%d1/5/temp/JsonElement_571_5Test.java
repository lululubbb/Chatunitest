package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonElement_571_5Test {

  // Since JsonElement is abstract, create a minimal concrete subclass for testing
  private static class JsonElementConcrete extends JsonElement {
    private final String value;

    public JsonElementConcrete() {
      this.value = "test";
    }

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
      return true;
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
      // Return a new JsonPrimitive with the value
      return new JsonPrimitive(value);
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
      return value;
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
  }

  @Test
    @Timeout(8000)
  public void testConstructorDeprecated() {
    JsonElement element = new JsonElementConcrete();
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testToString_returnsJson() throws IOException {
    JsonElement element = new JsonElementConcrete();
    String result = element.toString();
    assertNotNull(result);
    assertEquals("\"test\"", result);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy_returnsSelf() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertSame(element, element.deepCopy());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonArray_false() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertFalse(element.isJsonArray());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonObject_false() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertFalse(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonPrimitive_true() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertTrue(element.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonNull_false() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonObject);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonArray);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_returnsSelf() {
    JsonElementConcrete element = new JsonElementConcrete();
    JsonPrimitive primitive = element.getAsJsonPrimitive();
    assertNotNull(primitive);
    assertEquals("test", primitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonNull_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonNull);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsNumber);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_returnsValue() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertEquals("test", element.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsFloat);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsLong);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsInt);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsByte);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsCharacter);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsBigDecimal);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_throws() {
    JsonElementConcrete element = new JsonElementConcrete();
    assertThrows(UnsupportedOperationException.class, element::getAsShort);
  }

  @Test
    @Timeout(8000)
  public void testToString_usesStreamsWrite() throws Exception {
    JsonElement element = new JsonElementConcrete();

    // Use reflection to call private static Streams.write(JsonElement, JsonWriter)
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    Method streamsWriteMethod = Streams.class.getDeclaredMethod("write", JsonElement.class, JsonWriter.class);
    streamsWriteMethod.setAccessible(true);

    streamsWriteMethod.invoke(null, element, jsonWriter);
    jsonWriter.flush();
    String output = stringWriter.toString();

    assertNotNull(output);
    assertEquals("\"test\"", output);
  }
}