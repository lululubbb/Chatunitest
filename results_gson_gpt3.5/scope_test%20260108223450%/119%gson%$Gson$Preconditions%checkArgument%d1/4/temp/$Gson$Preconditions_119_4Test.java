package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class $Gson$Preconditions_119_4Test {

  @Test
    @Timeout(8000)
  void checkArgument_conditionTrue_noException() {
    // Should not throw any exception
    $Gson$Preconditions.checkArgument(true);
  }

  @Test
    @Timeout(8000)
  void checkArgument_conditionFalse_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> $Gson$Preconditions.checkArgument(false));
  }
}