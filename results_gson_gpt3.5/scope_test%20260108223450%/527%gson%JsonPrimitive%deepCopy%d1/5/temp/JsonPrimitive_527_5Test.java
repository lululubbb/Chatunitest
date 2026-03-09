package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class JsonPrimitive_527_5Test {

  @Test
    @Timeout(8000)
  public void testDeepCopy_returnsThis() {
    JsonPrimitive primitive = new JsonPrimitive("test");
    JsonPrimitive copy = primitive.deepCopy();
    assertSame(primitive, copy);
  }
}