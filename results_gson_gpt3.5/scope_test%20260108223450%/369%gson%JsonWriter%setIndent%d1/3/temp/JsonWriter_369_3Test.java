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
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.StringWriter;
import com.google.gson.stream.JsonWriter;

class JsonWriter_369_3Test {

  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    jsonWriter = new JsonWriter(new StringWriter());
  }

  @Test
    @Timeout(8000)
  void testSetIndent_emptyString() throws Exception {
    // Set indent with empty string should set indent to null and separator to ":"
    jsonWriter.setIndent("");
    // Use reflection to access private fields
    String indent = (String) getPrivateField(jsonWriter, "indent");
    String separator = (String) getPrivateField(jsonWriter, "separator");
    assertNull(indent);
    assertEquals(":", separator);
  }

  @Test
    @Timeout(8000)
  void testSetIndent_nonEmptyString() throws Exception {
    // Set indent with non-empty string should set indent and separator to ": "
    String indentValue = "  ";
    jsonWriter.setIndent(indentValue);
    String indent = (String) getPrivateField(jsonWriter, "indent");
    String separator = (String) getPrivateField(jsonWriter, "separator");
    assertEquals(indentValue, indent);
    assertEquals(": ", separator);
  }

  private Object getPrivateField(Object instance, String fieldName) throws Exception {
    var field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}