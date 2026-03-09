package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class $Gson$Preconditions_117_2Test {

  @Test
    @Timeout(8000)
  void testConstructorThrowsUnsupportedOperationException() throws Exception {
    Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
    assertNotNull(thrown);
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void testCheckNotNull_withNonNull() {
    String input = "test";
    String result = $Gson$Preconditions.checkNotNull(input);
    assertSame(input, result);
  }

  @Test
    @Timeout(8000)
  void testCheckNotNull_withNull() {
    assertThrows(NullPointerException.class, () -> {
      $Gson$Preconditions.checkNotNull(null);
    });
  }

  @Test
    @Timeout(8000)
  void testCheckArgument_trueCondition() {
    assertDoesNotThrow(() -> {
      $Gson$Preconditions.checkArgument(true);
    });
  }

  @Test
    @Timeout(8000)
  void testCheckArgument_falseCondition() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Preconditions.checkArgument(false);
    });
    assertNotNull(thrown);
  }
}