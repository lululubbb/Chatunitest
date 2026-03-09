package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonElement_571_4Test {

  // Since JsonElement is abstract, create a minimal concrete subclass for testing
  static class JsonElementImpl extends com.google.gson.JsonElement {
    @Override
    public com.google.gson.JsonElement deepCopy() {
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
    public com.google.gson.JsonObject getAsJsonObject() {
      throw new UnsupportedOperationException();
    }
    @Override
    public com.google.gson.JsonArray getAsJsonArray() {
      throw new UnsupportedOperationException();
    }
    @Override
    public com.google.gson.JsonPrimitive getAsJsonPrimitive() {
      throw new UnsupportedOperationException();
    }
    @Override
    public com.google.gson.JsonNull getAsJsonNull() {
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
      return "JsonElementImpl toString";
    }
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Deprecated() throws Exception {
    Constructor<JsonElementImpl> constructor = JsonElementImpl.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonElementImpl element = constructor.newInstance();
    assertNotNull(element);
  }

  @Test
    @Timeout(8000)
  public void testIsJsonArray() {
    JsonElementImpl element = new JsonElementImpl();
    assertFalse(element.isJsonArray());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonObject() {
    JsonElementImpl element = new JsonElementImpl();
    assertFalse(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonPrimitive() {
    JsonElementImpl element = new JsonElementImpl();
    assertFalse(element.isJsonPrimitive());
  }

  @Test
    @Timeout(8000)
  public void testIsJsonNull() {
    JsonElementImpl element = new JsonElementImpl();
    assertFalse(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    JsonElementImpl element = new JsonElementImpl();
    assertSame(element, element.deepCopy());
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonObject_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonObject);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonArray_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonArray);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonPrimitive_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonPrimitive);
  }

  @Test
    @Timeout(8000)
  public void testGetAsJsonNull_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsJsonNull);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBoolean);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsNumber);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsString);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsFloat);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsLong);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsInt);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsByte);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsCharacter);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBigDecimal);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsBigInteger);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_Unsupported() {
    JsonElementImpl element = new JsonElementImpl();
    assertThrows(UnsupportedOperationException.class, element::getAsShort);
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    JsonElementImpl element = new JsonElementImpl();
    assertEquals("JsonElementImpl toString", element.toString());
  }
}