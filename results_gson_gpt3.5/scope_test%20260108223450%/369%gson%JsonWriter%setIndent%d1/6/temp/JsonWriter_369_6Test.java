package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.stream.JsonScope.DANGLING_NAME;
import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.EMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.EMPTY_OBJECT;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_DOCUMENT;
import static com.google.gson.stream.JsonScope.NONEMPTY_OBJECT;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_369_6Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void testSetIndent_emptyString_resetsIndentAndSeparator() {
    jsonWriter.setIndent("  "); // set indent to non-empty first
    jsonWriter.setIndent("");
    // Using reflection to get private fields
    String indent = getPrivateField(jsonWriter, "indent");
    String separator = getPrivateField(jsonWriter, "separator");

    assertNull(indent, "indent should be null when empty string is passed");
    assertEquals(":", separator, "separator should be ':' when indent is empty");
  }

  @Test
    @Timeout(8000)
  void testSetIndent_nonEmptyString_setsIndentAndSeparator() {
    String indentStr = "  ";
    jsonWriter.setIndent(indentStr);
    String indent = getPrivateField(jsonWriter, "indent");
    String separator = getPrivateField(jsonWriter, "separator");

    assertEquals(indentStr, indent, "indent should be set to the given non-empty string");
    assertEquals(": ", separator, "separator should be ': ' when indent is non-empty");
  }

  @SuppressWarnings("unchecked")
  private <T> T getPrivateField(Object instance, String fieldName) {
    try {
      var field = instance.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(instance);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}