package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class JsonPrimitive_525_2Test {

  @Test
    @Timeout(8000)
  void constructor_String_shouldSetValue() throws NoSuchFieldException, IllegalAccessException {
    String testString = "testValue";
    JsonPrimitive jsonPrimitive = new JsonPrimitive(testString);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals(testString, value);
  }

  @Test
    @Timeout(8000)
  void constructor_String_nullShouldThrowNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((String) null));
  }

  @Test
    @Timeout(8000)
  void deepCopy_shouldReturnEqualButDifferentInstance() throws NoSuchFieldException, IllegalAccessException {
    JsonPrimitive original = new JsonPrimitive("123");
    JsonPrimitive copy = original.deepCopy();

    assertNotSame(original, copy);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    assertEquals(valueField.get(original), valueField.get(copy));
  }

  @Test
    @Timeout(8000)
  void isBoolean_shouldReturnFalseForString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertFalse(jsonPrimitive.isBoolean());
  }

  @Test
    @Timeout(8000)
  void isNumber_shouldReturnFalseForString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertFalse(jsonPrimitive.isNumber());
  }

  @Test
    @Timeout(8000)
  void isString_shouldReturnTrueForString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  void getAsString_shouldReturnValue() {
    String testString = "stringValue";
    JsonPrimitive jsonPrimitive = new JsonPrimitive(testString);
    assertEquals(testString, jsonPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  void getAsBoolean_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(ClassCastException.class, () -> {
      jsonPrimitive.getAsBoolean();
    });
  }

  @Test
    @Timeout(8000)
  void getAsNumber_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, () -> {
      jsonPrimitive.getAsNumber();
    });
  }

  @Test
    @Timeout(8000)
  void getAsDouble_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsDouble);
  }

  @Test
    @Timeout(8000)
  void getAsBigDecimal_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsBigDecimal);
  }

  @Test
    @Timeout(8000)
  void getAsBigInteger_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsBigInteger);
  }

  @Test
    @Timeout(8000)
  void getAsFloat_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsFloat);
  }

  @Test
    @Timeout(8000)
  void getAsLong_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsLong);
  }

  @Test
    @Timeout(8000)
  void getAsShort_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsShort);
  }

  @Test
    @Timeout(8000)
  void getAsInt_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsInt);
  }

  @Test
    @Timeout(8000)
  void getAsByte_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(NumberFormatException.class, jsonPrimitive::getAsByte);
  }

  @Test
    @Timeout(8000)
  void getAsCharacter_shouldThrowException() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("string");
    assertThrows(ClassCastException.class, () -> {
      jsonPrimitive.getAsCharacter();
    });
  }

  @Test
    @Timeout(8000)
  void equalsAndHashCode_shouldWorkCorrectly() {
    JsonPrimitive jsonPrimitive1 = new JsonPrimitive("value");
    JsonPrimitive jsonPrimitive2 = new JsonPrimitive("value");
    JsonPrimitive jsonPrimitive3 = new JsonPrimitive("other");

    assertEquals(jsonPrimitive1, jsonPrimitive2);
    assertEquals(jsonPrimitive1.hashCode(), jsonPrimitive2.hashCode());

    assertNotEquals(jsonPrimitive1, jsonPrimitive3);
    // hashCode collision can happen, so do not assertNotEquals on hashCode here
    // but keep for consistency with original test:
    assertNotEquals(jsonPrimitive1.hashCode(), jsonPrimitive3.hashCode());

    assertNotEquals(jsonPrimitive1, null);
    assertNotEquals(jsonPrimitive1, "string");
  }

  @Test
    @Timeout(8000)
  void isIntegral_privateMethod_shouldReturnTrueForIntegral() throws Exception {
    JsonPrimitive integral = new JsonPrimitive(123); // use Integer to represent integral number
    var method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, integral);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isIntegral_privateMethod_shouldReturnFalseForNonIntegral() throws Exception {
    JsonPrimitive nonIntegral = new JsonPrimitive("123.45");
    var method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, nonIntegral);
    assertFalse(result);
  }
}