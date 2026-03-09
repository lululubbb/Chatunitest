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

class JsonElement_571_2Test {

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
      return "testString";
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
      // The real toString() calls Streams.write(this, new JsonWriter(writer))
      // We will mock Streams.write to verify toString behavior
      return super.toString();
    }
  }

  @Test
    @Timeout(8000)
  void testToString_invokesStreamsWrite_andReturnsString() throws Exception {
    JsonElementImpl element = spy(new JsonElementImpl());

    // Use reflection to get the toString method from JsonElement (should be public)
    Method toStringMethod = JsonElement.class.getDeclaredMethod("toString");

    // Mock static method Streams.write(JsonElement, JsonWriter)
    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(eq(element), any(JsonWriter.class)))
          .thenAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(1);
            writer.value("mockedValue");
            return null;
          });

      // Invoke toString() method via reflection
      String result = (String) toStringMethod.invoke(element);

      assertNotNull(result);
      assertTrue(result.contains("mockedValue"));
      streamsMockedStatic.verify(() -> Streams.write(eq(element), any(JsonWriter.class)));
    }
  }

  @Test
    @Timeout(8000)
  void testDeprecatedConstructor() {
    // Just instantiate to cover deprecated constructor
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  void testIsJsonMethods_returnFalse() {
    JsonElementImpl element = new JsonElementImpl();
    assertFalse(element.isJsonArray());
    assertFalse(element.isJsonObject());
    assertFalse(element.isJsonPrimitive());
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testGetAsString_returnsExpected() {
    JsonElementImpl element = new JsonElementImpl();
    assertEquals("testString", element.getAsString());
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonObject_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonObject);
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonArray_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonArray);
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonPrimitive_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonPrimitive);
  }

  @Test
    @Timeout(8000)
  void testGetAsJsonNull_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonNull);
  }

  @Test
    @Timeout(8000)
  void testGetAsBoolean_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
  }

  @Test
    @Timeout(8000)
  void testGetAsNumber_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsNumber);
  }

  @Test
    @Timeout(8000)
  void testGetAsDouble_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsDouble);
  }

  @Test
    @Timeout(8000)
  void testGetAsFloat_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsFloat);
  }

  @Test
    @Timeout(8000)
  void testGetAsLong_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsLong);
  }

  @Test
    @Timeout(8000)
  void testGetAsInt_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsInt);
  }

  @Test
    @Timeout(8000)
  void testGetAsByte_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsByte);
  }

  @Test
    @Timeout(8000)
  void testGetAsCharacter_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsCharacter);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigDecimal_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBigDecimal);
  }

  @Test
    @Timeout(8000)
  void testGetAsBigInteger_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
  }

  @Test
    @Timeout(8000)
  void testGetAsShort_throws() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsShort);
  }

  @Test
    @Timeout(8000)
  void testDeepCopy_returnsSelf() {
    JsonElementImpl element = new JsonElementImpl();
    assertSame(element, element.deepCopy());
  }
}