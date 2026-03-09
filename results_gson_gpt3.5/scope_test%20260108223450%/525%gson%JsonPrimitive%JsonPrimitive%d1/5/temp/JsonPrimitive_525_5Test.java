package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class JsonPrimitive_525_5Test {

  @Test
    @Timeout(8000)
  void testConstructor_StringStoresValue() throws Exception {
    String input = "testString";
    JsonPrimitive primitive = new JsonPrimitive(input);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertEquals(input, value);
  }

  @Test
    @Timeout(8000)
  void testConstructor_StringNull_ThrowsNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((String) null));
  }
}