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
import java.io.Writer;
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
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_385_4Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // reflectively set stackSize to 1 to simulate open writer (not closed)
    try {
      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonWriter, 1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void name_nullName_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> jsonWriter.name(null));
    assertEquals("name == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void name_deferredNameNotNull_throwsIllegalStateException() throws Exception {
    // set deferredName to simulate already set name
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "existingName");

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonWriter.name("newName"));
    assertNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void name_stackSizeZero_throwsIllegalStateException() throws Exception {
    // set stackSize to 0 to simulate closed writer
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonWriter.name("name"));
    assertEquals("JsonWriter is closed.", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void name_validName_setsDeferredNameAndReturnsThis() throws Exception {
    String testName = "myName";
    JsonWriter returned = jsonWriter.name(testName);

    assertSame(jsonWriter, returned);

    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    String deferredNameValue = (String) deferredNameField.get(jsonWriter);
    assertEquals(testName, deferredNameValue);
  }
}