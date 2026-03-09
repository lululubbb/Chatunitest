package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

public class JsonPrimitive_532_4Test {

  @Test
    @Timeout(8000)
  public void testIsString_withStringValue() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue("test string");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Integer() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue(123);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Boolean() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue(true);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Double() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue(123.45);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNullValue() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue(null);
    assertFalse(jsonPrimitive.isString());
  }

  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use a dummy constructor call (e.g. with Boolean) and then set the private final field via reflection
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, value);
    return jsonPrimitive;
  }
}