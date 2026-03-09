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

public class JsonPrimitive_542_6Test {

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_nonEmptyString() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("hello");
    Method getAsCharacterMethod = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    getAsCharacterMethod.setAccessible(true);

    char result = (char) getAsCharacterMethod.invoke(primitive);
    assertEquals('h', result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_emptyString_throwsException() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("");
    Method getAsCharacterMethod = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    getAsCharacterMethod.setAccessible(true);

    Executable executable = () -> {
      try {
        getAsCharacterMethod.invoke(primitive);
      } catch (Exception e) {
        // unwrap the cause from reflection invocation target exception
        Throwable cause = e.getCause();
        if (cause != null) {
          throw cause;
        } else {
          throw e;
        }
      }
    };

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, executable);
    assertEquals("String value is empty", thrown.getMessage());
  }
}