package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class JsonPrimitive_526_3Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithCharacter() throws Exception {
    // Create JsonPrimitive instance using Character constructor
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Character.class);
    constructor.setAccessible(true);

    Character c = 'a';
    JsonPrimitive jsonPrimitive = constructor.newInstance(c);

    // Access private field "value" via reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertNotNull(jsonPrimitive);
    assertEquals("a", value);
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithNullCharacterThrowsNPE() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Character.class);
    constructor.setAccessible(true);

    assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance((Character) null);
    });
  }
}