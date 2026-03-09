package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JsonPrimitive_527_1Test {

  @Test
    @Timeout(8000)
  void deepCopy_returnsThis() {
    JsonPrimitive primitive = new JsonPrimitive("test");
    JsonPrimitive copy = primitive.deepCopy();
    assertSame(primitive, copy);
  }
}