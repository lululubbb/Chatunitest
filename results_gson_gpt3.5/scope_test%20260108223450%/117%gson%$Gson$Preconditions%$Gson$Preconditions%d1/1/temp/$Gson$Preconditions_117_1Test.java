package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class $Gson$Preconditions_117_1Test {

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<$Gson$Preconditions> constructor =
        $Gson$Preconditions.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }

  @Test
    @Timeout(8000)
  void testCheckNotNull_withNonNull() {
    String value = "test";
    String result = $Gson$Preconditions.checkNotNull(value);
    assertSame(value, result);
  }

  @Test
    @Timeout(8000)
  void testCheckNotNull_withNull() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      $Gson$Preconditions.checkNotNull(null);
    });
    // check that the message is null, since the method likely throws NPE without message
    assertNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void testCheckArgument_trueCondition() {
    assertDoesNotThrow(() -> $Gson$Preconditions.checkArgument(true));
  }

  @Test
    @Timeout(8000)
  void testCheckArgument_falseCondition() {
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Preconditions.checkArgument(false);
    });
    // check that the message is null, since the method likely throws IAE without message
    assertNull(thrown.getMessage());
  }
}