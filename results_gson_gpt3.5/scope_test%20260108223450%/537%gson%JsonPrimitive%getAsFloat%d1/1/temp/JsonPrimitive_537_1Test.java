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

public class JsonPrimitive_537_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No common setup needed, each test will create its own instance
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_NumberValue() throws Exception {
    // Create JsonPrimitive with a Number value
    jsonPrimitive = new JsonPrimitive(3.14f);

    // Use reflection to set private final field 'value' to a Number instance
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, 2.718f);

    // Spy to override isNumber() and getAsNumber()
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn(5.5f).when(spyPrimitive).getAsNumber();

    float result = spyPrimitive.getAsFloat();

    assertEquals(5.5f, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_StringValue() throws Exception {
    // Create JsonPrimitive with a String value
    jsonPrimitive = new JsonPrimitive("1.23");

    // Spy to override isNumber() and getAsString()
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("4.56").when(spyPrimitive).getAsString();

    float result = spyPrimitive.getAsFloat();

    assertEquals(4.56f, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_StringValue_InvalidFormat() throws Exception {
    jsonPrimitive = new JsonPrimitive("not_a_number");

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn("not_a_number").when(spyPrimitive).getAsString();

    assertThrows(NumberFormatException.class, () -> spyPrimitive.getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testGetAsFloat_NumberValue_LazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is package-private, so instantiate via constructor using reflection
    Class<?> lpnClass = Class.forName("com.google.gson.internal.LazilyParsedNumber");
    Object lpnInstance = lpnClass.getConstructor(String.class).newInstance("7.89");

    jsonPrimitive = new JsonPrimitive(0);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, lpnInstance);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    doReturn((Number) lpnInstance).when(spyPrimitive).getAsNumber();

    float result = spyPrimitive.getAsFloat();

    assertEquals(7.89f, result, 0.0001f);
  }
}