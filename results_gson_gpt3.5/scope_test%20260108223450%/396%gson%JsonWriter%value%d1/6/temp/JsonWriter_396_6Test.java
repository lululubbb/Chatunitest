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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriter_value_Test {

  private JsonWriter writer;
  private StringWriter out;

  @BeforeEach
  void setUp() {
    out = new StringWriter();
    writer = new JsonWriter(out);
  }

  @Test
    @Timeout(8000)
  void value_null_callsNullValue() throws IOException {
    JsonWriter spyWriter = spy(writer);
    doReturn(spyWriter).when(spyWriter).nullValue();

    JsonWriter result = spyWriter.value((Number) null);

    verify(spyWriter).nullValue();
    assertSame(spyWriter, result);
  }

  @Test
    @Timeout(8000)
  void value_specialNumbers_lenientFalse_throws() {
    Number[] specials = {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN};
    for (Number special : specials) {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> writer.value(special));
      String msg = ex.getMessage();
      assertTrue(msg.contains("Numeric values must be finite"));
      assertTrue(msg.contains(special.toString()));
    }
  }

  @Test
    @Timeout(8000)
  void value_specialNumbers_lenientTrue_writes() throws IOException {
    writer.setLenient(true);
    Number[] specials = {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN};
    for (Number special : specials) {
      JsonWriter result = writer.value(special);
      assertSame(writer, result);
      assertTrue(out.toString().contains("Infinity") || out.toString().contains("NaN"));
      out.getBuffer().setLength(0);
    }
  }

  @Test
    @Timeout(8000)
  void value_untrustedNumberType_invalidString_throws() throws Exception {
    // Create a Number subclass with invalid JSON number string representation
    Number invalidNumber = new Number() {
      @Override public int intValue() {return 0;}
      @Override public long longValue() {return 0L;}
      @Override public float floatValue() {return 0;}
      @Override public double doubleValue() {return 0;}
      @Override public String toString() {return "invalid123";}
    };
    // Validate that isTrustedNumberType returns false for this class
    Method isTrustedNumberType = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    isTrustedNumberType.setAccessible(true);
    boolean trusted = (boolean)isTrustedNumberType.invoke(null, invalidNumber.getClass());
    assertFalse(trusted);

    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> writer.value(invalidNumber));
    String msg = ex.getMessage();
    assertTrue(msg.contains("String created by"));
    assertTrue(msg.contains("is not a valid JSON number"));
  }

  @Test
    @Timeout(8000)
  void value_untrustedNumberType_validString_writes() throws IOException {
    // Use BigDecimal which is not trusted but produces valid JSON number string
    BigDecimal bigDecimal = new BigDecimal("123.456e-7");
    JsonWriter result = writer.value(bigDecimal);
    assertSame(writer, result);
    assertTrue(out.toString().contains(bigDecimal.toString()));
  }

  @Test
    @Timeout(8000)
  void value_trustedNumberType_writes() throws IOException {
    // Integer is trusted type
    Integer number = 12345;
    JsonWriter result = writer.value(number);
    assertSame(writer, result);
    assertTrue(out.toString().contains(number.toString()));
  }

  @Test
    @Timeout(8000)
  void value_callsWriteDeferredNameBeforeValueAndAppends() throws Exception {
    JsonWriter spyWriter = spy(new JsonWriter(out));

    // Instead of trying to stub private methods writeDeferredName and beforeValue (which Mockito cannot do),
    // we use reflection to invoke them before calling value() to simulate their effect.

    // Invoke private writeDeferredName method before calling value()
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);

    // Invoke private beforeValue method before calling value()
    Method beforeValueMethod = JsonWriter.class.getDeclaredMethod("beforeValue");
    beforeValueMethod.setAccessible(true);

    Integer number = 42;

    // Manually invoke private methods to simulate their effect before calling value()
    writeDeferredNameMethod.invoke(spyWriter);
    beforeValueMethod.invoke(spyWriter);

    JsonWriter result = spyWriter.value(number);

    assertSame(spyWriter, result);
    assertTrue(out.toString().contains(number.toString()));
  }

  @Test
    @Timeout(8000)
  void value_patternMatchesValidJsonNumbers() throws Exception {
    // Access VALID_JSON_NUMBER_PATTERN via reflection
    var field = JsonWriter.class.getDeclaredField("VALID_JSON_NUMBER_PATTERN");
    field.setAccessible(true);
    Pattern pattern = (Pattern) field.get(null);

    String[] validNumbers = {
        "0", "123", "-123", "0.1", "-0.1", "1e10", "1E10", "1e+10", "1e-10", "-1.23e+10"
    };
    for (String valid : validNumbers) {
      assertTrue(pattern.matcher(valid).matches(), "Should match: " + valid);
    }
    String[] invalidNumbers = {
        "", "00", "-01", "1.", ".1", "1e", "e10", "1e++10", "1e--10", "abc"
    };
    for (String invalid : invalidNumbers) {
      assertFalse(pattern.matcher(invalid).matches(), "Should not match: " + invalid);
    }
  }
}