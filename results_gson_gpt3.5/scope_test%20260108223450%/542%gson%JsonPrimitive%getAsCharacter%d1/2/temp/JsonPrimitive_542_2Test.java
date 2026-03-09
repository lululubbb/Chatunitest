package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;

public class JsonPrimitive_542_2Test {

  @Test
    @Timeout(8000)
  void testGetAsCharacter_withNonEmptyString() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive("abc"));

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);
    char result = (char) method.invoke(jsonPrimitive);

    assertEquals('a', result);
  }

  @Test
    @Timeout(8000)
  void testGetAsCharacter_withEmptyString_throwsException() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.spy(new JsonPrimitive(""));

    Method method = JsonPrimitive.class.getDeclaredMethod("getAsCharacter");
    method.setAccessible(true);

    Throwable thrown = assertThrows(Throwable.class, () -> {
      method.invoke(jsonPrimitive);
    });

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof UnsupportedOperationException);
    assertEquals("String value is empty", cause.getMessage());
  }
}