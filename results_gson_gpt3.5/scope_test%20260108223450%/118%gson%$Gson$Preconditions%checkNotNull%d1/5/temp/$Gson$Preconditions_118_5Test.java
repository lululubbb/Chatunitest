package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class $Gson$Preconditions_118_5Test {

  @Test
    @Timeout(8000)
  void checkNotNull_withNonNullObject_returnsSameObject() {
    String str = "test";
    String result = $Gson$Preconditions.checkNotNull(str);
    assertSame(str, result);
  }

  @Test
    @Timeout(8000)
  void checkNotNull_withNullObject_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> $Gson$Preconditions.checkNotNull(null));
  }
}