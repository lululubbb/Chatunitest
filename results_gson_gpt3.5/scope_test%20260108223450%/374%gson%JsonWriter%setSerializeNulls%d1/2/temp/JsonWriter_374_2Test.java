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
import java.lang.reflect.Method;

public class JsonWriter_374_2Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testSetSerializeNulls_true() throws Exception {
    // Initially serializeNulls is true by default
    assertTrue(jsonWriter.getSerializeNulls());

    // Use reflection to get the private field serializeNulls before change
    var serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.set(jsonWriter, false); // forcibly set false to test setter

    assertFalse(jsonWriter.getSerializeNulls());

    // Call setSerializeNulls(true)
    jsonWriter.setSerializeNulls(true);

    // Verify the field is updated to true
    assertTrue(jsonWriter.getSerializeNulls());
    assertEquals(true, serializeNullsField.get(jsonWriter));
  }

  @Test
    @Timeout(8000)
  public void testSetSerializeNulls_false() throws Exception {
    // Initially serializeNulls is true by default
    assertTrue(jsonWriter.getSerializeNulls());

    // Call setSerializeNulls(false)
    jsonWriter.setSerializeNulls(false);

    // Verify the field is updated to false
    assertFalse(jsonWriter.getSerializeNulls());

    // Use reflection to verify private field value
    var serializeNullsField = JsonWriter.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertEquals(false, serializeNullsField.get(jsonWriter));
  }
}