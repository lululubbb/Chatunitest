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

public class JsonWriter_385_1Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // Initialize stackSize to 1 to avoid "JsonWriter is closed." exception
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
  public void name_nullName_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> {
      jsonWriter.name(null);
    });
    assertEquals("name == null", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_deferredNameNotNull_throwsIllegalStateException() throws Exception {
    // Set deferredName to a non-null value to simulate illegal state
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "existingName");

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonWriter.name("newName");
    });
    assertTrue(exception.getMessage() == null || exception.getMessage().isEmpty());
  }

  @Test
    @Timeout(8000)
  public void name_stackSizeZero_throwsIllegalStateException() throws Exception {
    // Set stackSize to 0 to simulate closed writer
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonWriter.name("name");
    });
    assertEquals("JsonWriter is closed.", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_validName_setsDeferredNameAndReturnsThis() throws Exception {
    String testName = "testName";

    JsonWriter returned = jsonWriter.name(testName);

    // Verify returned object is the same instance
    assertSame(jsonWriter, returned);

    // Verify deferredName field is set to testName
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    String deferredNameValue = (String) deferredNameField.get(jsonWriter);
    assertEquals(testName, deferredNameValue);
  }
}