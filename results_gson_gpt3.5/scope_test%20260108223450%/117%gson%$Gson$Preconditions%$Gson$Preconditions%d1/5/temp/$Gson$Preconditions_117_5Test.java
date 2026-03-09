package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class $Gson$Preconditions_117_5Test {

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);
    assertTrue(thrown.getCause() instanceof UnsupportedOperationException);
  }
}