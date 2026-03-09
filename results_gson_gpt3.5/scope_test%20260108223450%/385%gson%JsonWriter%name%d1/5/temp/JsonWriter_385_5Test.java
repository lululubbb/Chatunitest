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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_385_5Test {

  private JsonWriter writer;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    writer = new JsonWriter(stringWriter);
    // Initialize stackSize to 1 to avoid "JsonWriter is closed." exception
    // Using reflection because stackSize is private
    try {
      var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(writer, 1);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  public void testName_withValidName_returnsThisAndSetsDeferredName() throws IOException {
    JsonWriter returned = writer.name("testName");
    assertSame(writer, returned);

    try {
      var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
      deferredNameField.setAccessible(true);
      Object deferredNameValue = deferredNameField.get(writer);
      assertEquals("testName", deferredNameValue);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testName_withNullName_throwsNullPointerException() {
    NullPointerException exception = assertThrows(NullPointerException.class, () -> writer.name(null));
    assertEquals("name == null", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testName_whenDeferredNameAlreadySet_throwsIllegalStateException() throws Exception {
    // Set deferredName to non-null using reflection
    var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(writer, "alreadySet");

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("newName"));
    assertNull(exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testName_whenStackSizeZero_throwsIllegalStateException() throws Exception {
    // Set deferredName to null
    var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(writer, null);
    // Set stackSize to 0
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(writer, 0);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("name"));
    assertEquals("JsonWriter is closed.", exception.getMessage());
  }

}