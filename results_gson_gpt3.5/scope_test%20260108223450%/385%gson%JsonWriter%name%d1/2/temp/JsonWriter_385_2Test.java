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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class JsonWriter_385_2Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() throws Exception {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);

    // Set stackSize to 1 to avoid "JsonWriter is closed." exception in tests
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 1);
  }

  @Test
    @Timeout(8000)
  public void name_nullName_throwsNullPointerException() {
    NullPointerException npe = assertThrows(NullPointerException.class, () -> jsonWriter.name(null));
    assertEquals("name == null", npe.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_deferredNameNotNull_throwsIllegalStateException() throws Exception {
    // Use reflection to set deferredName to non-null
    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    deferredNameField.set(jsonWriter, "alreadyDeferred");

    IllegalStateException ise = assertThrows(IllegalStateException.class, () -> jsonWriter.name("newName"));
    assertEquals("alreadyDeferred", deferredNameField.get(jsonWriter));
  }

  @Test
    @Timeout(8000)
  public void name_stackSizeZero_throwsIllegalStateException() throws Exception {
    // Use reflection to set stackSize to 0
    Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.setInt(jsonWriter, 0);

    IllegalStateException ise = assertThrows(IllegalStateException.class, () -> jsonWriter.name("name"));
    assertEquals("JsonWriter is closed.", ise.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_validName_setsDeferredNameAndReturnsThis() throws Exception {
    String testName = "testName";

    JsonWriter returned = jsonWriter.name(testName);

    assertSame(jsonWriter, returned);

    Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    String deferredNameValue = (String) deferredNameField.get(jsonWriter);
    assertEquals(testName, deferredNameValue);
  }
}