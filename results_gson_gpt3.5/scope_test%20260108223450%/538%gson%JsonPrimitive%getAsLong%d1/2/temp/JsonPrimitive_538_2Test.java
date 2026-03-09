package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_538_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // No common setup needed
  }

  private void setValueField(JsonPrimitive instance, Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, value);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenValueIsNumber() throws Exception {
    // Arrange
    jsonPrimitive = new JsonPrimitive(123L);
    // Act
    long result = jsonPrimitive.getAsLong();
    // Assert
    assertEquals(123L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenValueIsLazilyParsedNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive(new LazilyParsedNumber("456"));
    long result = jsonPrimitive.getAsLong();
    assertEquals(456L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenValueIsStringNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive("789");
    long result = jsonPrimitive.getAsLong();
    assertEquals(789L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenValueIsStringNumberNegative() throws Exception {
    jsonPrimitive = new JsonPrimitive("-9876543210");
    long result = jsonPrimitive.getAsLong();
    assertEquals(-9876543210L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_whenValueIsStringNumberWithWhitespace() throws Exception {
    jsonPrimitive = new JsonPrimitive("  42  ");
    // Trim the string before parsing by overriding getAsString via spy
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(jsonPrimitive.getAsString().trim()).when(spyPrimitive).getAsString();
    long result = spyPrimitive.getAsLong();
    assertEquals(42L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_withReflectionAndPrivateValueNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive("dummy");
    setValueField(jsonPrimitive, 12345);
    long result = jsonPrimitive.getAsLong();
    assertEquals(12345L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_withReflectionAndPrivateValueString() throws Exception {
    jsonPrimitive = new JsonPrimitive(0);
    setValueField(jsonPrimitive, "98765");
    long result = jsonPrimitive.getAsLong();
    assertEquals(98765L, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsLong_withReflectionAndPrivateValueLazilyParsedNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive(0);
    setValueField(jsonPrimitive, new LazilyParsedNumber("13579"));
    long result = jsonPrimitive.getAsLong();
    assertEquals(13579L, result);
  }
}