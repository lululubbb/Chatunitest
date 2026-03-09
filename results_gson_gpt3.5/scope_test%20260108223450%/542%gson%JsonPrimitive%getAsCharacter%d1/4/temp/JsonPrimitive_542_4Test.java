package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Method;

public class JsonPrimitive_542_4Test {

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_nonEmptyString_returnsFirstChar() throws Exception {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive("abc"));

    // Use real getAsString method
    doReturn("abc").when(jsonPrimitive).getAsString();

    char result = jsonPrimitive.getAsCharacter();

    assertEquals('a', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyString_throwsUnsupportedOperationException() throws Exception {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive(""));

    doReturn("").when(jsonPrimitive).getAsString();

    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
      jsonPrimitive.getAsCharacter();
    });

    assertEquals("String value is empty", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_reflectionInvocation_nonEmptyString() throws Exception {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive("xyz"));

    doReturn("xyz").when(jsonPrimitive).getAsString();

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);
    char result = (char) method.invoke(jsonPrimitive);

    assertEquals('x', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_reflectionInvocation_emptyString_throws() throws Exception {
    JsonPrimitive jsonPrimitive = spy(new JsonPrimitive(""));

    doReturn("").when(jsonPrimitive).getAsString();

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);

    Exception exception = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
      method.invoke(jsonPrimitive);
    });

    // The cause should be UnsupportedOperationException
    assertTrue(exception.getCause() instanceof UnsupportedOperationException);
    assertEquals("String value is empty", exception.getCause().getMessage());
  }
}