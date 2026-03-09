package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class $Gson$Preconditions_118_3Test {

  @Test
    @Timeout(8000)
  void checkNotNull_withNonNullObject_returnsSameObject() {
    String obj = "test";
    String result = $Gson$Preconditions.checkNotNull(obj);
    assertSame(obj, result);
  }

  @Test
    @Timeout(8000)
  void checkNotNull_withNullObject_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> $Gson$Preconditions.checkNotNull(null));
  }
}