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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import java.lang.reflect.Method;

class JsonElement_576_2Test {

  @Test
    @Timeout(8000)
  void testIsJsonNull_withJsonNullInstance() {
    JsonElement jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_withNonJsonNullInstance() {
    // Create a mock of abstract class JsonElement using Mockito
    JsonElement mockElement = Mockito.mock(JsonElement.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    // Ensure mock is not instance of JsonNull
    assertFalse(mockElement.isJsonNull());
  }

  @Test
    @Timeout(8000)
  void testIsJsonNull_viaReflection() throws Exception {
    JsonNull jsonNullInstance = JsonNull.INSTANCE;
    Method isJsonNullMethod = JsonElement.class.getDeclaredMethod("isJsonNull");
    isJsonNullMethod.setAccessible(true);
    Object result = isJsonNullMethod.invoke(jsonNullInstance);
    assertTrue((Boolean) result);

    JsonElement mockElement = Mockito.mock(JsonElement.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
    result = isJsonNullMethod.invoke(mockElement);
    assertFalse((Boolean) result);
  }
}