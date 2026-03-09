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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonWriter_374_5Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void testSetSerializeNulls_trueAndFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Initially serializeNulls should be true (default)
    Method getSerializeNullsMethod = JsonWriter.class.getMethod("getSerializeNulls");
    boolean initialValue = (boolean) getSerializeNullsMethod.invoke(jsonWriter);
    assertTrue(initialValue);

    // Set serializeNulls to false
    jsonWriter.setSerializeNulls(false);
    boolean afterSetFalse = (boolean) getSerializeNullsMethod.invoke(jsonWriter);
    assertFalse(afterSetFalse);

    // Set serializeNulls back to true
    jsonWriter.setSerializeNulls(true);
    boolean afterSetTrue = (boolean) getSerializeNullsMethod.invoke(jsonWriter);
    assertTrue(afterSetTrue);
  }

}