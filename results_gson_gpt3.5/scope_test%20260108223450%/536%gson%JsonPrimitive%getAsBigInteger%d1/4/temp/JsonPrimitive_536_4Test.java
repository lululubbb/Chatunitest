package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_536_4Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setup() throws Exception {
    // Use a public constructor to create a valid JsonPrimitive instance
    jsonPrimitive = new JsonPrimitive("dummy");
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_whenValueIsBigInteger() throws Exception {
    // Set private field 'value' to a BigInteger instance
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    BigInteger bigInteger = new BigInteger("12345678901234567890");
    valueField.set(jsonPrimitive, bigInteger);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertSame(bigInteger, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_whenValueIsNotBigInteger() throws Exception {
    // Set private field 'value' to a String representing a number
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    String numericString = "98765432109876543210";
    valueField.set(jsonPrimitive, numericString);

    BigInteger result = jsonPrimitive.getAsBigInteger();

    assertEquals(new BigInteger(numericString), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_whenValueIsNumberButNotBigInteger() throws Exception {
    // Set private field 'value' to a Number (e.g. Integer)
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Integer intValue = 42;
    valueField.set(jsonPrimitive, intValue);

    // We expect getAsString() to be called internally, so we mock it via spy
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn("42").when(spyPrimitive).getAsString();

    Field spyValueField = JsonPrimitive.class.getDeclaredField("value");
    spyValueField.setAccessible(true);
    spyValueField.set(spyPrimitive, intValue);

    BigInteger result = spyPrimitive.getAsBigInteger();

    assertEquals(BigInteger.valueOf(42), result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigInteger_whenValueIsLazilyParsedNumber() throws Exception {
    // LazilyParsedNumber is a Number subclass - test conversion through getAsString()
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object lazilyParsedNumber = new com.google.gson.internal.LazilyParsedNumber("1234567890");
    valueField.set(jsonPrimitive, lazilyParsedNumber);

    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn("1234567890").when(spyPrimitive).getAsString();

    Field spyValueField = JsonPrimitive.class.getDeclaredField("value");
    spyValueField.setAccessible(true);
    spyValueField.set(spyPrimitive, lazilyParsedNumber);

    BigInteger result = spyPrimitive.getAsBigInteger();

    assertEquals(new BigInteger("1234567890"), result);
  }
}