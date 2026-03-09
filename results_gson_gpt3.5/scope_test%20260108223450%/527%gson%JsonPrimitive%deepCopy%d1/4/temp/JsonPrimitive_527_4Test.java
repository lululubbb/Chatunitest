package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_527_4Test {

  @Test
    @Timeout(8000)
  public void testDeepCopyReturnsSameInstance() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    JsonPrimitive copy = jsonPrimitive.deepCopy();
    assertSame(jsonPrimitive, copy, "deepCopy should return the same instance");
  }
}