package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LazilyParsedNumber;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_540_6Test {

  JsonPrimitive jsonPrimitive;

  @BeforeEach
  void setup() {
    // no common setup needed
  }

  @Test
    @Timeout(8000)
  void getAsInt_whenIsNumber_returnsIntValue() {
    // Arrange
    Number number = 42;
    jsonPrimitive = new JsonPrimitive(number);

    // Act
    int result = jsonPrimitive.getAsInt();

    // Assert
    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_whenIsNumber_withLazilyParsedNumber_returnsIntValue() {
    // Arrange
    Number number = new LazilyParsedNumber("123");
    jsonPrimitive = new JsonPrimitive(number);

    // Act
    int result = jsonPrimitive.getAsInt();

    // Assert
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_whenNotNumber_parsesString() {
    // Arrange
    String numberString = "456";
    jsonPrimitive = new JsonPrimitive(numberString);

    // Act
    int result = jsonPrimitive.getAsInt();

    // Assert
    assertEquals(456, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_whenNotNumber_invalidString_throwsNumberFormatException() {
    // Arrange
    String invalidNumberString = "notANumber";
    jsonPrimitive = new JsonPrimitive(invalidNumberString);

    // Act & Assert
    assertThrows(NumberFormatException.class, () -> jsonPrimitive.getAsInt());
  }

  @Test
    @Timeout(8000)
  void getAsInt_withCharacterValue_parsesCharacterToInt() {
    // Arrange
    Character c = '7';
    jsonPrimitive = new JsonPrimitive(c);

    // Act
    int result = jsonPrimitive.getAsInt();

    // Assert
    assertEquals(7, result);
  }

  @Test
    @Timeout(8000)
  void getAsInt_privateIsNumberMethod_reflectionConsistent() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive(10);

    Method isNumberMethod = JsonPrimitive.class.getDeclaredMethod("isNumber");
    isNumberMethod.setAccessible(true);

    // Act
    boolean isNumber = (boolean) isNumberMethod.invoke(jsonPrimitive);
    int asInt = jsonPrimitive.getAsInt();

    // Assert
    assertTrue(isNumber);
    assertEquals(10, asInt);
  }
}