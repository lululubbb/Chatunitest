package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;

public class JsonPrimitive_537_4Test {

  private JsonPrimitive jsonPrimitive;

  private JsonPrimitive createJsonPrimitive(Object value) throws Exception {
    Constructor<JsonPrimitive> constructor;
    if (value instanceof Boolean) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
      return constructor.newInstance((Boolean) value);
    } else if (value instanceof Number) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
      return constructor.newInstance((Number) value);
    } else if (value instanceof String) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(String.class);
      return constructor.newInstance((String) value);
    } else if (value instanceof Character) {
      constructor = JsonPrimitive.class.getDeclaredConstructor(Character.class);
      return constructor.newInstance((Character) value);
    }
    throw new IllegalArgumentException("Unsupported type: " + value.getClass());
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenValueIsNumber() throws Exception {
    jsonPrimitive = createJsonPrimitive(3.14d);

    float result = jsonPrimitive.getAsFloat();

    assertEquals(3.14f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenValueIsLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is internal, create instance via constructor
    LazilyParsedNumber lazilyParsedNumber = new LazilyParsedNumber("2.718");

    jsonPrimitive = createJsonPrimitive(lazilyParsedNumber);

    float result = jsonPrimitive.getAsFloat();

    assertEquals(2.718f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenValueIsStringNumber() throws Exception {
    jsonPrimitive = createJsonPrimitive("1.234");

    float result = jsonPrimitive.getAsFloat();

    assertEquals(1.234f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenValueIsStringNonNumber_throwsNumberFormatException() throws Exception {
    jsonPrimitive = createJsonPrimitive("notANumber");

    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsFloat();
    });
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenValueIsIntegerNumber() throws Exception {
    jsonPrimitive = createJsonPrimitive(42);

    float result = jsonPrimitive.getAsFloat();

    assertEquals(42f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_whenValueIsFloatNumber() throws Exception {
    jsonPrimitive = createJsonPrimitive(5.5f);

    float result = jsonPrimitive.getAsFloat();

    assertEquals(5.5f, result, 0.0001f);
  }
}