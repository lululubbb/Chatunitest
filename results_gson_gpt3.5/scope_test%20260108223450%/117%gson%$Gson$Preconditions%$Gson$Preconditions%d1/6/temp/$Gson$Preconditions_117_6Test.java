package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class $Gson$Preconditions_117_6Test {

  @Test
    @Timeout(8000)
  void testPrivateConstructorThrowsException() throws Exception {
    Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void testCheckNotNullWithNonNull() {
    String nonNull = "test";
    String result = $Gson$Preconditions.checkNotNull(nonNull);
    assertSame(nonNull, result);
  }

  @Test
    @Timeout(8000)
  void testCheckNotNullWithNull() {
    assertThrows(NullPointerException.class, () -> {
      $Gson$Preconditions.checkNotNull(null);
    });
  }

  @Test
    @Timeout(8000)
  void testCheckArgumentTrue() {
    assertDoesNotThrow(() -> {
      $Gson$Preconditions.checkArgument(true);
    });
  }

  @Test
    @Timeout(8000)
  void testCheckArgumentFalse() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Preconditions.checkArgument(false);
    });
    assertNull(thrown.getMessage());
  }
}