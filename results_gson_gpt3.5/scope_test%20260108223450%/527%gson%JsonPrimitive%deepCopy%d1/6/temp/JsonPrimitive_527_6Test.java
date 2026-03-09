package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class JsonPrimitive_527_6Test {

  @Test
    @Timeout(8000)
  void deepCopy_shouldReturnThisInstance() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    JsonPrimitive copy = jsonPrimitive.deepCopy();
    assertSame(jsonPrimitive, copy);
  }
}