package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.Method;

public class JsonPrimitive_542_1Test {

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_nonEmptyString() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("abc");
    Method getAsCharacter = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    getAsCharacter.setAccessible(true);
    char result = (char) getAsCharacter.invoke(jsonPrimitive);
    assertEquals('a', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyString_throws() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("");
    Method getAsCharacter = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    getAsCharacter.setAccessible(true);
    Executable invocation = () -> {
      getAsCharacter.invoke(jsonPrimitive);
    };
    Exception exception = assertThrows(Exception.class, invocation);
    // InvocationTargetException wraps the UnsupportedOperationException
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof UnsupportedOperationException);
    assertEquals("String value is empty", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_singleCharacterString() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("Z");
    Method getAsCharacter = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    getAsCharacter.setAccessible(true);
    char result = (char) getAsCharacter.invoke(jsonPrimitive);
    assertEquals('Z', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_numericString() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("123");
    Method getAsCharacter = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    getAsCharacter.setAccessible(true);
    char result = (char) getAsCharacter.invoke(jsonPrimitive);
    assertEquals('1', result);
  }

}