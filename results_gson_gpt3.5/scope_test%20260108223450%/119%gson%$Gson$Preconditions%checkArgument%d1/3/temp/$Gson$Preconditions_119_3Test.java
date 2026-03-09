package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class $Gson$Preconditions_119_3Test {

  @Test
    @Timeout(8000)
  void checkArgument_withTrueCondition_doesNotThrow() {
    assertDoesNotThrow(() -> $Gson$Preconditions.checkArgument(true));
  }

  @Test
    @Timeout(8000)
  void checkArgument_withFalseCondition_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> $Gson$Preconditions.checkArgument(false));
  }
}