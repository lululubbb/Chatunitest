package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_558_2Test {

  @Test
    @Timeout(8000)
  void deepCopy_shouldReturnInstance() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonNull copy = jsonNull.deepCopy();
    assertSame(JsonNull.INSTANCE, copy);
  }
}