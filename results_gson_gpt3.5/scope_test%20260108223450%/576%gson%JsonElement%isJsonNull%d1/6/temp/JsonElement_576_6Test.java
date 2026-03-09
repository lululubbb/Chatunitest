package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;

class JsonElement_576_6Test {

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsJsonNull() throws Exception {
    Constructor<JsonNull> constructor = JsonNull.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    JsonElement jsonNull = constructor.newInstance();
    assertTrue(jsonNull.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_whenInstanceIsNotJsonNull() {
    // Create a mock of JsonElement that is not JsonNull
    JsonElement mockElement = Mockito.mock(JsonElement.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    // Force the instance to be a mock but not JsonNull
    assertFalse(mockElement.isJsonNull());
  }
}