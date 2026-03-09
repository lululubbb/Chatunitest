package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class JsonElement_583_2Test {

  @Test
    @Timeout(8000)
  void getAsString_throwsUnsupportedOperationException() {
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return null;
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
        return null;
      }

      @Override
      public JsonArray getAsJsonArray() {
        return null;
      }

      @Override
      public JsonPrimitive getAsJsonPrimitive() {
        return null;
      }

      @Override
      public JsonNull getAsJsonNull() {
        return null;
      }

      @Override
      public boolean getAsBoolean() {
        return false;
      }

      @Override
      public Number getAsNumber() {
        return null;
      }

      @Override
      public double getAsDouble() {
        return 0;
      }

      @Override
      public float getAsFloat() {
        return 0;
      }

      @Override
      public long getAsLong() {
        return 0;
      }

      @Override
      public int getAsInt() {
        return 0;
      }

      @Override
      public byte getAsByte() {
        return 0;
      }

      @Override
      public char getAsCharacter() {
        return 0;
      }

      @Override
      public BigDecimal getAsBigDecimal() {
        return null;
      }

      @Override
      public BigInteger getAsBigInteger() {
        return null;
      }

      @Override
      public short getAsShort() {
        return 0;
      }
    };

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, element::getAsString);
    // Optionally verify exception message matches class simple name
    // assertEquals("JsonElement", thrown.getMessage());
  }
}