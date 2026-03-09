package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class JsonPrimitive_539_4Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no-op, instances created in tests
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNumber_returnsShortValue() {
    Number number = mock(Number.class);
    when(number.shortValue()).thenReturn((short)123);

    JsonPrimitive primitive = new JsonPrimitive(number);

    short result = primitive.getAsShort();

    assertEquals((short)123, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNotNumber_parsesString() {
    JsonPrimitive primitive = new JsonPrimitive("456");

    short result = primitive.getAsShort();

    assertEquals((short)456, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_whenNotNumber_invalidString_throwsNumberFormatException() {
    JsonPrimitive primitive = new JsonPrimitive("notANumber");

    assertThrows(NumberFormatException.class, primitive::getAsShort);
  }

  @Test
    @Timeout(8000)
  public void testGetAsShort_usingReflection_onPrivateValueField() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("789");

    // Access private field 'value' via reflection and change it to a Number
    var valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, (short)321);

    // Because getAsShort uses isNumber(), which depends on value, we mock isNumber() via spy
    JsonPrimitive spyPrimitive = Mockito.spy(primitive);
    doReturn(true).when(spyPrimitive).isNumber();

    // Actually, getAsNumber() returns Number, so we mock getAsNumber to return a Number with shortValue 321
    Number number = mock(Number.class);
    when(number.shortValue()).thenReturn((short)321);
    doReturn(number).when(spyPrimitive).getAsNumber();

    short result = spyPrimitive.getAsShort();

    assertEquals((short)321, result);
  }
}