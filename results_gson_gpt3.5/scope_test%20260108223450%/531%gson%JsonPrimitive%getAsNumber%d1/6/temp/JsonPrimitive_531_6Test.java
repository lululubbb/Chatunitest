package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.google.gson.internal.LazilyParsedNumber;

public class JsonPrimitive_531_6Test {

  private JsonPrimitive createJsonPrimitiveWithValue(Object val) {
    if (val instanceof Boolean) {
      return new JsonPrimitive((Boolean) val);
    } else if (val instanceof Number) {
      return new JsonPrimitive((Number) val);
    } else if (val instanceof String) {
      return new JsonPrimitive((String) val);
    } else if (val instanceof Character) {
      return new JsonPrimitive((Character) val);
    } else {
      // Use a dummy valid JsonPrimitive and replace the value field via reflection
      JsonPrimitive jp = new JsonPrimitive(true);
      try {
        java.lang.reflect.Field valueField = JsonPrimitive.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(jp, val);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return jp;
    }
  }

  @Test
    @Timeout(8000)
  public void getAsNumber_withNumberValue_returnsSameNumber() {
    Number number = 123.45;
    JsonPrimitive jp = createJsonPrimitiveWithValue(number);
    Number result = jp.getAsNumber();
    assertSame(number, result);
  }

  @Test
    @Timeout(8000)
  public void getAsNumber_withStringValue_returnsLazilyParsedNumber() {
    String numericString = "6789";
    JsonPrimitive jp = createJsonPrimitiveWithValue(numericString);
    Number result = jp.getAsNumber();
    assertNotNull(result);
    assertEquals(LazilyParsedNumber.class, result.getClass());
    assertEquals(numericString, result.toString());
  }

  @Test
    @Timeout(8000)
  public void getAsNumber_withNonNumberNonStringValue_throwsException() {
    Object nonNumberString = new Object();
    JsonPrimitive jp = createJsonPrimitiveWithValue(nonNumberString);
    Executable executable = jp::getAsNumber;
    UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class, executable);
    assertEquals("Primitive is neither a number nor a string", ex.getMessage());
  }
}