package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_533_4Test {

  private JsonPrimitive jsonPrimitive;

  private void setValue(Object instance, Object value) throws NoSuchFieldException, IllegalAccessException {
    Field field = JsonPrimitive.class.getDeclaredField("value");
    field.setAccessible(true);
    field.set(instance, value);
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsString() throws Exception {
    jsonPrimitive = new JsonPrimitive("testString");
    assertEquals("testString", jsonPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsNumber() throws Exception {
    jsonPrimitive = new JsonPrimitive(123);
    // spy to mock isNumber and getAsNumber
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(true).when(spyPrimitive).isNumber();
    Number number = 456;
    doReturn(number).when(spyPrimitive).getAsNumber();
    assertEquals("456", spyPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsBoolean() throws Exception {
    jsonPrimitive = new JsonPrimitive(true);
    // spy to mock isNumber and isBoolean
    JsonPrimitive spyPrimitive = spy(jsonPrimitive);
    doReturn(false).when(spyPrimitive).isNumber();
    doReturn(true).when(spyPrimitive).isBoolean();
    setValue(spyPrimitive, Boolean.TRUE);
    assertEquals("true", spyPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsString_whenValueIsUnexpectedType_throwsAssertionError() throws Exception {
    jsonPrimitive = new JsonPrimitive("dummy");
    // set private final value field to an unexpected type (e.g. Object)
    Object unexpected = new Object();
    setValue(jsonPrimitive, unexpected);

    AssertionError error = assertThrows(AssertionError.class, () -> {
      jsonPrimitive.getAsString();
    });
    assertTrue(error.getMessage().contains("Unexpected value type:"));
    assertTrue(error.getMessage().contains(unexpected.getClass().toString()));
  }
}