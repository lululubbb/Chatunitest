package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class JsonPrimitive_526_4Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithCharacter() throws NoSuchFieldException, IllegalAccessException {
    Character input = 'a';
    JsonPrimitive jsonPrimitive = new JsonPrimitive(input);

    // Access private field 'value' via reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertNotNull(value);
    assertTrue(value instanceof String);
    assertEquals("a", value);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithNullCharacterThrowsNPE() {
    assertThrows(NullPointerException.class, () -> {
      new JsonPrimitive((Character) null);
    });
  }
}