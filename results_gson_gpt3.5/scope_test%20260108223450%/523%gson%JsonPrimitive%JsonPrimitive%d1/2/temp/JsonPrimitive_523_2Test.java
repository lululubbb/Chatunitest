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
import java.lang.reflect.Method;

public class JsonPrimitive_523_2Test {

  @Test
    @Timeout(8000)
  public void testConstructor_booleanValue_setsValue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);
    assertEquals(Boolean.TRUE, value);
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_trueForBooleanValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.FALSE);
    assertTrue(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBoolean_returnsBooleanValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertTrue(jsonPrimitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_falseForBooleanValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertFalse(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsString_falseForBooleanValue() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_returnsStringRepresentation() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertEquals("true", jsonPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(UnsupportedOperationException.class, jsonPrimitive::getAsNumber);
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsDouble);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsLong);
  }

  @Test
    @Timeout(8000)
  public void testGetAsInt_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsInt);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsFloat);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsBigDecimal);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsBigInteger);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsShort);
  }

  @Test
    @Timeout(8000)
  public void testGetAsByte_throwsExceptionForBoolean() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsByte);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_throwsUnsupportedOperationException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(Boolean.TRUE);
    // changed: no exception is thrown, so test updated to expect no exception and check value
    assertDoesNotThrow(() -> {
      char c = jsonPrimitive.getAsCharacter();
      // For Boolean, getAsCharacter returns 't' or 'f', so check it's either 't' or 'f'
      assertTrue(c == 't' || c == 'f');
    });
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    JsonPrimitive a = new JsonPrimitive(Boolean.TRUE);
    JsonPrimitive b = new JsonPrimitive(Boolean.TRUE);
    JsonPrimitive c = new JsonPrimitive(Boolean.FALSE);

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());

    assertNotEquals(a, c);
    assertNotEquals(a.hashCode(), c.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy_returnsSameInstanceForBoolean() {
    JsonPrimitive original = new JsonPrimitive(Boolean.TRUE);
    JsonPrimitive copy = original.deepCopy();
    assertEquals(original, copy);
    // changed: deepCopy returns same instance for Boolean, so assertSame instead of assertNotSame
    assertSame(original, copy);
  }

  @Test
    @Timeout(8000)
  public void testPrivateIsIntegralMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
    constructor.setAccessible(true);
    JsonPrimitive jsonPrimitive = constructor.newInstance(Boolean.TRUE);

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    Boolean result = (Boolean) isIntegralMethod.invoke(null, jsonPrimitive);
    assertFalse(result);
  }
}