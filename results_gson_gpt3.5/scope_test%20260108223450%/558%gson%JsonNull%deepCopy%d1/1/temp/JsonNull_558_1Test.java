package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_558_1Test {

  @Test
    @Timeout(8000)
  void deepCopy_returnsInstance() {
    JsonNull jsonNull = new JsonNull();
    JsonNull copy = jsonNull.deepCopy();
    assertSame(JsonNull.INSTANCE, copy);
  }
}