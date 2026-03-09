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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_369_4Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void testSetIndent_emptyString_setsIndentNullAndSeparatorColon() throws Exception {
    jsonWriter.setIndent("");
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);

    assertNull(indentField.get(jsonWriter));
    assertEquals(":", separatorField.get(jsonWriter));
  }

  @Test
    @Timeout(8000)
  void testSetIndent_nonEmptyString_setsIndentAndSeparatorColonSpace() throws Exception {
    String indentValue = "  ";
    jsonWriter.setIndent(indentValue);
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);

    assertEquals(indentValue, indentField.get(jsonWriter));
    assertEquals(": ", separatorField.get(jsonWriter));
  }

  @Test
    @Timeout(8000)
  void testSetIndent_singleCharIndent_setsIndentAndSeparatorColonSpace() throws Exception {
    String indentValue = "\t";
    jsonWriter.setIndent(indentValue);
    Field indentField = JsonWriter.class.getDeclaredField("indent");
    indentField.setAccessible(true);
    Field separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);

    assertEquals(indentValue, indentField.get(jsonWriter));
    assertEquals(": ", separatorField.get(jsonWriter));
  }
}