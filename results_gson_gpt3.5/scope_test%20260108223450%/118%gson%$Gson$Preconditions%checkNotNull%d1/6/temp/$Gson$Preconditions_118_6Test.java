package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class $Gson$Preconditions_118_6Test {

  @Test
    @Timeout(8000)
  void checkNotNull_withNonNullObject_returnsSameObject() {
    String testString = "test";
    String result = $Gson$Preconditions.checkNotNull(testString);
    assertSame(testString, result);
  }

  @Test
    @Timeout(8000)
  void checkNotNull_withNullObject_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> $Gson$Preconditions.checkNotNull(null));
  }
}