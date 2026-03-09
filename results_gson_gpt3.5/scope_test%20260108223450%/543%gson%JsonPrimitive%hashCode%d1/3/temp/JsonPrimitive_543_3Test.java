package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_543_3Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // Default initialization with a String value
    jsonPrimitive = new JsonPrimitive("test");
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueNull() throws Exception {
    // Set private field 'value' to null via reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, null);

    int hash = jsonPrimitive.hashCode();
    assertEquals(31, hash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_integralNumber() throws Exception {
    // Create a JsonPrimitive with integral Number value (e.g. Integer)
    JsonPrimitive integralPrimitive = new JsonPrimitive(123);

    // Verify isIntegral returns true via reflection
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    assertTrue((Boolean) isIntegralMethod.invoke(null, integralPrimitive));

    // Calculate expected hash manually
    long val = integralPrimitive.getAsNumber().longValue();
    int expectedHash = (int) (val ^ (val >>> 32));

    int actualHash = integralPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_numberNonIntegral() throws Exception {
    // Create a JsonPrimitive with non-integral Number (Double)
    JsonPrimitive doublePrimitive = new JsonPrimitive(123.456d);

    // Verify isIntegral returns false via reflection
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    assertFalse((Boolean) isIntegralMethod.invoke(null, doublePrimitive));

    // Calculate expected hash manually
    long val = Double.doubleToLongBits(doublePrimitive.getAsNumber().doubleValue());
    int expectedHash = (int) (val ^ (val >>> 32));

    int actualHash = doublePrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueNotNumber() throws Exception {
    // Create a JsonPrimitive with a String value
    JsonPrimitive stringPrimitive = new JsonPrimitive("stringValue");

    // Confirm value is not a Number via reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object val = valueField.get(stringPrimitive);
    assertFalse(val instanceof Number);

    // Expected hash is value.hashCode()
    int expectedHash = val.hashCode();

    int actualHash = stringPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsLazilyParsedNumber_integral() throws Exception {
    // LazilyParsedNumber with integral value
    LazilyParsedNumber lpn = new LazilyParsedNumber("789");
    JsonPrimitive lpnPrimitive = new JsonPrimitive(lpn);

    // Confirm isIntegral returns false (fix: LazilyParsedNumber is not considered integral by isIntegral)
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    assertFalse((Boolean) isIntegralMethod.invoke(null, lpnPrimitive));

    long val = Double.doubleToLongBits(lpnPrimitive.getAsNumber().doubleValue());
    int expectedHash = (int) (val ^ (val >>> 32));

    int actualHash = lpnPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsLazilyParsedNumber_nonIntegral() throws Exception {
    // LazilyParsedNumber with non-integral value
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    JsonPrimitive lpnPrimitive = new JsonPrimitive(lpn);

    // Confirm isIntegral returns false
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);
    assertFalse((Boolean) isIntegralMethod.invoke(null, lpnPrimitive));

    long val = Double.doubleToLongBits(lpnPrimitive.getAsNumber().doubleValue());
    int expectedHash = (int) (val ^ (val >>> 32));

    int actualHash = lpnPrimitive.hashCode();
    assertEquals(expectedHash, actualHash);
  }
}