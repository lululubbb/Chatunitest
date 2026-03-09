package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class JsonPrimitive_542_3Test {

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_nonEmptyString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("abc"));

    // Use real method getAsString() to return "abc"
    doReturn("abc").when(jsonPrimitive).getAsString();

    char result = jsonPrimitive.getAsCharacter();

    assertEquals('a', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyString_throws() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(""));

    doReturn("").when(jsonPrimitive).getAsString();

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      jsonPrimitive.getAsCharacter();
    });

    assertEquals("String value is empty", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_usingReflection_nonEmptyString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("hello"));
    doReturn("hello").when(jsonPrimitive).getAsString();

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);

    char result = (char) method.invoke(jsonPrimitive);

    assertEquals('h', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_usingReflection_emptyString_throws() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(""));
    doReturn("").when(jsonPrimitive).getAsString();

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);

    Exception exception = assertThrows(Exception.class, () -> {
      method.invoke(jsonPrimitive);
    });

    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof UnsupportedOperationException);
    assertEquals("String value is empty", cause.getMessage());
  }
}