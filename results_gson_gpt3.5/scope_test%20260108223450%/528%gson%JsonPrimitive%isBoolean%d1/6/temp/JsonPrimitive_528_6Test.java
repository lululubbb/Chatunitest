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

public class JsonPrimitive_528_6Test {

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanFalse() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(false);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withString() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("true");
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNumber() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(1);
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withCharacter() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('a');
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNullValueUsingReflection() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, null);
    assertFalse(jsonPrimitive.isBoolean());
  }
}