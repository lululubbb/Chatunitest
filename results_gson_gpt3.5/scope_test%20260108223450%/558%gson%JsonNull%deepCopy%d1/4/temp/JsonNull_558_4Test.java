package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonNull_558_4Test {

  @Test
    @Timeout(8000)
  void deepCopy_ReturnsSingletonInstance() {
    JsonNull instance = JsonNull.INSTANCE;

    JsonNull copy = instance.deepCopy();

    assertSame(instance, copy, "deepCopy should return the singleton instance");
  }
}