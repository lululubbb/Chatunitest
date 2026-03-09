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
import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;

public class JsonWriter_369_2Test {
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testSetIndent_emptyString_setsIndentNullAndSeparatorColon() {
    jsonWriter.setIndent("");
    // Use reflection to access private fields indent and separator
    try {
      var indentField = JsonWriter.class.getDeclaredField("indent");
      var separatorField = JsonWriter.class.getDeclaredField("separator");
      indentField.setAccessible(true);
      separatorField.setAccessible(true);
      Object indentValue = indentField.get(jsonWriter);
      Object separatorValue = separatorField.get(jsonWriter);

      assertNull(indentValue);
      assertEquals(":", separatorValue);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testSetIndent_nonEmptyString_setsIndentAndSeparatorWithSpace() {
    String indentString = "  ";
    jsonWriter.setIndent(indentString);
    try {
      var indentField = JsonWriter.class.getDeclaredField("indent");
      var separatorField = JsonWriter.class.getDeclaredField("separator");
      indentField.setAccessible(true);
      separatorField.setAccessible(true);
      Object indentValue = indentField.get(jsonWriter);
      Object separatorValue = separatorField.get(jsonWriter);

      assertEquals(indentString, indentValue);
      assertEquals(": ", separatorValue);
    } catch (Exception e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}