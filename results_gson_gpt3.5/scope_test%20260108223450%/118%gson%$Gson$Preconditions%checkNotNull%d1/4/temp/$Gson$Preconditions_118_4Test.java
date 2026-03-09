package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class $Gson$Preconditions_118_4Test {

  @Test
    @Timeout(8000)
  public void testCheckNotNull_withNonNullObject_returnsSameObject() {
    String input = "test";
    String result = $Gson$Preconditions.checkNotNull(input);
    assertSame(input, result);
  }

  @Test
    @Timeout(8000)
  public void testCheckNotNull_withNullObject_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      $Gson$Preconditions.checkNotNull(null);
    });
  }
}