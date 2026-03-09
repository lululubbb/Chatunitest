package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.Test;

class JsonElement_592_3Test {

  static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public short getAsShort() {
      throw new UnsupportedOperationException(getClass().getSimpleName());
    }
  }

  @Test
    @Timeout(8000)
  void getAsShort_throwsOnBaseClass() {
    JsonElement element = new JsonElementSubclass();
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, element::getAsShort);
    // The exception message should be the simple class name of the subclass
    assertEquals("JsonElementSubclass", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  void getAsShort_fromJsonPrimitive() {
    JsonPrimitive primitive = new JsonPrimitive((short) 123);
    assertEquals((short) 123, primitive.getAsShort());

    JsonPrimitive primitiveFromString = new JsonPrimitive("456");
    assertEquals((short) 456, primitiveFromString.getAsShort());

    JsonPrimitive primitiveFromNumber = new JsonPrimitive(789);
    assertEquals((short) 789, primitiveFromNumber.getAsShort());
  }

  @Test
    @Timeout(8000)
  void getAsShort_fromJsonNull_throws() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertThrows(UnsupportedOperationException.class, jsonNull::getAsShort);
  }

  @Test
    @Timeout(8000)
  void getAsShort_fromJsonObject_throws() {
    JsonObject jsonObject = new JsonObject();
    assertThrows(UnsupportedOperationException.class, jsonObject::getAsShort);
  }

  @Test
    @Timeout(8000)
  void getAsShort_fromJsonArray_throws() {
    JsonArray jsonArray = new JsonArray();
    // Add one element to avoid IllegalStateException for empty array
    JsonElementSubclass element = new JsonElementSubclass();
    jsonArray.add(element);
    // Call get(0).getAsShort() to trigger UnsupportedOperationException from JsonElementSubclass
    assertThrows(UnsupportedOperationException.class, () -> jsonArray.get(0).getAsShort());
  }
}