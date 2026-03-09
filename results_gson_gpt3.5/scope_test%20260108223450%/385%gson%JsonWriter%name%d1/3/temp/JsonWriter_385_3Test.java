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

public class JsonWriter_385_3Test {

  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
    // Initialize stackSize to simulate open JsonWriter
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
  public void name_NullName_ThrowsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      jsonWriter.name(null);
    });
    assertEquals("name == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_DeferredNameAlreadySet_ThrowsIllegalStateException() {
    try {
      Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
      deferredNameField.setAccessible(true);
      deferredNameField.set(jsonWriter, "alreadySet");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonWriter.name("newName");
    });
    // No message expected for this exception per source
  }

  @Test
    @Timeout(8000)
  public void name_StackSizeZero_ThrowsIllegalStateException() {
    try {
      Field stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
      stackSizeField.setAccessible(true);
      stackSizeField.setInt(jsonWriter, 0);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonWriter.name("name");
    });
    assertEquals("JsonWriter is closed.", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_ValidName_SetsDeferredNameAndReturnsThis() throws IOException {
    JsonWriter returned = jsonWriter.name("myName");
    assertSame(jsonWriter, returned);

    try {
      Field deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
      deferredNameField.setAccessible(true);
      String deferredNameValue = (String) deferredNameField.get(jsonWriter);
      assertEquals("myName", deferredNameValue);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}