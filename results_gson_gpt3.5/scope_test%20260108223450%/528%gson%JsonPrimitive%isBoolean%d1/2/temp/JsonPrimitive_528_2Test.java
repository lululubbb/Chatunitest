package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

public class JsonPrimitive_528_2Test {

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanTrue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanFalse() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.FALSE);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNumber() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("true");
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withCharacter() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('a');
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withNullValue() throws Exception {
    // Create a normal instance with a non-null value
    JsonPrimitive normalInstance = new JsonPrimitive("dummy");
    // Set the private final 'value' field to null using reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(normalInstance, null);

    assertFalse(normalInstance.isBoolean());
  }
}