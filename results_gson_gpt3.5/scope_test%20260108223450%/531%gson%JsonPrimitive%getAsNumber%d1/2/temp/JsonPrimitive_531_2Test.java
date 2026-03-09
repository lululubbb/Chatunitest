package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LazilyParsedNumber;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_531_2Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setup() {
    // no-op, instances created per test
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_WithNumberValue() throws Exception {
    // Use reflection to create instance with private final field 'value' set to a Number
    jsonPrimitive = (JsonPrimitive) createInstanceWithValue(123);

    Number result = jsonPrimitive.getAsNumber();

    assertNotNull(result);
    assertEquals(123, result.intValue());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_WithStringValue() throws Exception {
    String numberString = "456.789";
    jsonPrimitive = (JsonPrimitive) createInstanceWithValue(numberString);

    Number result = jsonPrimitive.getAsNumber();

    assertNotNull(result);
    assertTrue(result instanceof LazilyParsedNumber);
    assertEquals(numberString, result.toString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_WithUnsupportedValue_ThrowsException() throws Exception {
    Object unsupported = new Object();
    jsonPrimitive = (JsonPrimitive) createInstanceWithValue(unsupported);

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      jsonPrimitive.getAsNumber();
    });
    assertEquals("Primitive is neither a number nor a string", thrown.getMessage());
  }

  private Object createInstanceWithValue(Object value) throws Exception {
    // Use reflection to instantiate JsonPrimitive without calling public constructors
    // and set private final field 'value' to the provided value
    Class<JsonPrimitive> clazz = JsonPrimitive.class;
    // Find no-arg constructor or use unsafe allocation
    // JsonPrimitive has no no-arg constructor, so use Unsafe or reflection on constructor with Boolean
    // We'll use the Boolean constructor and then override value field

    JsonPrimitive instance = new JsonPrimitive(true); // dummy initialization

    // Set private final field 'value'
    Field valueField = clazz.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, value);

    return instance;
  }
}