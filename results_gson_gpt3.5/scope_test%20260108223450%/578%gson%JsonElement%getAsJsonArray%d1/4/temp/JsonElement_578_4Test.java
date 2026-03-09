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

import org.junit.jupiter.api.Test;

public class JsonElement_578_4Test {

  static class JsonElementStub extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public boolean isJsonArray() {
      return false;
    }

    @Override
    public boolean isJsonObject() {
      return false;
    }

    @Override
    public boolean isJsonPrimitive() {
      return false;
    }

    @Override
    public boolean isJsonNull() {
      return false;
    }

    @Override
    public JsonObject getAsJsonObject() {
      throw new IllegalStateException("Not a JSON Object");
    }

    @Override
    public JsonPrimitive getAsJsonPrimitive() {
      throw new IllegalStateException("Not a JSON Primitive");
    }

    @Override
    public JsonNull getAsJsonNull() {
      throw new IllegalStateException("Not a JSON Null");
    }

    @Override
    public boolean getAsBoolean() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Number getAsNumber() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getAsString() {
      throw new UnsupportedOperationException();
    }

    @Override
    public double getAsDouble() {
      throw new UnsupportedOperationException();
    }

    @Override
    public float getAsFloat() {
      throw new UnsupportedOperationException();
    }

    @Override
    public long getAsLong() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int getAsInt() {
      throw new UnsupportedOperationException();
    }

    @Override
    public byte getAsByte() {
      throw new UnsupportedOperationException();
    }

    @Override
    public char getAsCharacter() {
      throw new UnsupportedOperationException();
    }

    @Override
    public java.math.BigDecimal getAsBigDecimal() {
      throw new UnsupportedOperationException();
    }

    @Override
    public java.math.BigInteger getAsBigInteger() {
      throw new UnsupportedOperationException();
    }

    @Override
    public short getAsShort() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
      return "JsonElementStub";
    }
  }

  static class JsonArrayStub extends JsonArray {
    @Override
    public JsonElement deepCopy() {
      return this;
    }

    @Override
    public String toString() {
      return "JsonArrayStub";
    }
  }

  @Test
    @Timeout(8000)
  public void getAsJsonArray_whenIsJsonArray_returnsThisAsJsonArray() {
    JsonElement jsonArray = new JsonArrayStub();
    JsonArray result = jsonArray.getAsJsonArray();
    assertEquals("JsonArrayStub", result.toString());
  }

  @Test
    @Timeout(8000)
  public void getAsJsonArray_whenNotJsonArray_throwsIllegalStateException() {
    JsonElementStub jsonElement = new JsonElementStub();
    IllegalStateException exception = assertThrows(IllegalStateException.class, jsonElement::getAsJsonArray);
    assertEquals("Not a JSON Array: " + jsonElement.toString(), exception.getMessage());
  }
}