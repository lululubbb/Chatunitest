package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_528_5Test {

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanTrue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_withBooleanFalse() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(false);
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
}