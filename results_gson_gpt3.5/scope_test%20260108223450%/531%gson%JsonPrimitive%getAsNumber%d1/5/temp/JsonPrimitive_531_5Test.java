package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LazilyParsedNumber;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_531_5Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setUp() {
    // no-op
  }

  @Test
    @Timeout(8000)
  void getAsNumber_whenValueIsNumber_returnsNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive(42);

    Number result = jsonPrimitive.getAsNumber();

    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  void getAsNumber_whenValueIsString_returnsLazilyParsedNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive("123.45");

    Number result = jsonPrimitive.getAsNumber();

    assertTrue(result instanceof LazilyParsedNumber);
    assertEquals("123.45", result.toString());
  }

  @Test
    @Timeout(8000)
  void getAsNumber_whenValueIsNeitherNumberNorString_throwsException() throws Exception {
    jsonPrimitive = new JsonPrimitive(true);
    // forcibly set private field 'value' to an unsupported type to test exception branch
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, new Object());

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      jsonPrimitive.getAsNumber();
    });
    assertEquals("Primitive is neither a number nor a string", thrown.getMessage());
  }
}