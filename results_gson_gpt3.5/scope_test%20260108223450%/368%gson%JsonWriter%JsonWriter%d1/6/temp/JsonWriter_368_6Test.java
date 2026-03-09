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
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonWriter_368_6Test {

  private StringWriter stringWriter;
  private JsonWriter jsonWriter;

  @BeforeEach
  public void setUp() {
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_nullWriter_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> new JsonWriter(null));
    assertEquals("out == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_initialStackAndStackSize() throws Exception {
    // Use reflection to check private fields stack and stackSize
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    assertNotNull(stack);
    assertEquals(32, stack.length);

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(jsonWriter);
    assertEquals(0, stackSize);
  }

  @Test
    @Timeout(8000)
  public void testOpen_pushesStackAndWritesOpenBracket() throws Exception {
    Method openMethod = JsonWriter.class.getDeclaredMethod("open", int.class, char.class);
    openMethod.setAccessible(true);

    // invoke open with EMPTY_ARRAY and '['
    JsonWriter returned = (JsonWriter) openMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, '[');

    // Check returned is this instance
    assertSame(jsonWriter, returned);

    // Check stackSize incremented to 1
    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    int stackSize = (int) stackSizeField.get(jsonWriter);
    assertEquals(1, stackSize);

    // Check top of stack is EMPTY_ARRAY
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    assertEquals(JsonScope.EMPTY_ARRAY, stack[stackSize - 1]);

    // Check output contains '['
    assertEquals("[", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testClose_replacesStackAndWritesCloseBracket() throws Exception {
    // Prepare stack with one element EMPTY_ARRAY and stackSize 1
    var stackField = JsonWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    int[] stack = (int[]) stackField.get(jsonWriter);
    stack[0] = JsonScope.EMPTY_ARRAY;

    var stackSizeField = JsonWriter.class.getDeclaredField("stackSize");
    stackSizeField.setAccessible(true);
    stackSizeField.set(jsonWriter, 1);

    Method closeMethod = JsonWriter.class.getDeclaredMethod("close", int.class, int.class, char.class);
    closeMethod.setAccessible(true);

    // invoke close with EMPTY_ARRAY, NONEMPTY_ARRAY, ']'
    JsonWriter returned = (JsonWriter) closeMethod.invoke(jsonWriter, JsonScope.EMPTY_ARRAY, JsonScope.NONEMPTY_ARRAY, ']');

    assertSame(jsonWriter, returned);

    // stackSize should be decremented to 0
    int stackSize = (int) stackSizeField.get(jsonWriter);
    assertEquals(0, stackSize);

    // output should contain ']'
    assertEquals("]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testPush_and_peek_and_replaceTop() throws Exception {
    Method pushMethod = JsonWriter.class.getDeclaredMethod("push", int.class);
    pushMethod.setAccessible(true);
    Method peekMethod = JsonWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Method replaceTopMethod = JsonWriter.class.getDeclaredMethod("replaceTop", int.class);
    replaceTopMethod.setAccessible(true);

    // push 5
    pushMethod.invoke(jsonWriter, 5);

    // peek should return 5
    int top = (int) peekMethod.invoke(jsonWriter);
    assertEquals(5, top);

    // replace top with 10
    replaceTopMethod.invoke(jsonWriter, 10);

    // peek should return 10
    top = (int) peekMethod.invoke(jsonWriter);
    assertEquals(10, top);
  }

  @Test
    @Timeout(8000)
  public void testName_and_writeDeferredName_and_beforeName() throws IOException, Exception {
    // name sets deferredName and calls beforeName
    String testName = "testName";

    // Call name() with testName
    JsonWriter returned = jsonWriter.name(testName);
    assertSame(jsonWriter, returned);

    // Use reflection to check deferredName set
    var deferredNameField = JsonWriter.class.getDeclaredField("deferredName");
    deferredNameField.setAccessible(true);
    String deferredName = (String) deferredNameField.get(jsonWriter);
    assertEquals(testName, deferredName);

    // Call writeDeferredName() private method to write the name
    Method writeDeferredNameMethod = JsonWriter.class.getDeclaredMethod("writeDeferredName");
    writeDeferredNameMethod.setAccessible(true);
    writeDeferredNameMethod.invoke(jsonWriter);

    // deferredName should be null after writing
    deferredName = (String) deferredNameField.get(jsonWriter);
    assertNull(deferredName);

    // Output should contain the name string with quotes and separator
    String output = stringWriter.toString();
    assertTrue(output.contains("\"" + testName + "\""));
    assertTrue(output.contains(":") || output.contains(separator()));
  }

  @Test
    @Timeout(8000)
  public void testValue_String_and_nullValue() throws IOException {
    // Write a string value
    jsonWriter.value("hello");
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("\"hello\""));

    // Write null value
    jsonWriter.nullValue();
    jsonWriter.flush();
    String output2 = stringWriter.toString();
    assertTrue(output2.contains("null"));
  }

  @Test
    @Timeout(8000)
  public void testValue_boolean_and_Boolean() throws IOException {
    jsonWriter.value(true);
    jsonWriter.value(Boolean.FALSE);
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("true"));
    assertTrue(output.contains("false"));
  }

  @Test
    @Timeout(8000)
  public void testValue_float_double_long_Number() throws IOException {
    jsonWriter.value(1.23f);
    jsonWriter.value(4.56d);
    jsonWriter.value(789L);
    jsonWriter.value(Integer.valueOf(10));
    jsonWriter.flush();
    String output = stringWriter.toString();
    assertTrue(output.contains("1.23"));
    assertTrue(output.contains("4.56"));
    assertTrue(output.contains("789"));
    assertTrue(output.contains("10"));
  }

  @Test
    @Timeout(8000)
  public void testIsTrustedNumberType() throws Exception {
    Method isTrustedNumberTypeMethod = JsonWriter.class.getDeclaredMethod("isTrustedNumberType", Class.class);
    isTrustedNumberTypeMethod.setAccessible(true);

    assertTrue((boolean) isTrustedNumberTypeMethod.invoke(null, Integer.class));
    assertTrue((boolean) isTrustedNumberTypeMethod.invoke(null, Double.class));
    assertTrue((boolean) isTrustedNumberTypeMethod.invoke(null, Long.class));
    assertTrue((boolean) isTrustedNumberTypeMethod.invoke(null, Float.class));
    assertTrue((boolean) isTrustedNumberTypeMethod.invoke(null, Byte.class));
    assertTrue((boolean) isTrustedNumberTypeMethod.invoke(null, Short.class));

    assertFalse((boolean) isTrustedNumberTypeMethod.invoke(null, BigDecimal.class));
    assertFalse((boolean) isTrustedNumberTypeMethod.invoke(null, BigInteger.class));
    assertFalse((boolean) isTrustedNumberTypeMethod.invoke(null, AtomicInteger.class));
    assertFalse((boolean) isTrustedNumberTypeMethod.invoke(null, AtomicLong.class));
  }

  @Test
    @Timeout(8000)
  public void testFlush_and_Close() throws IOException {
    jsonWriter.flush();
    // flush should not throw

    jsonWriter.close();
    // close should close underlying writer
    assertThrows(IOException.class, () -> stringWriter.write("fail"));
  }

  @Test
    @Timeout(8000)
  public void testString_method_writesEscapedString() throws Exception {
    Method stringMethod = JsonWriter.class.getDeclaredMethod("string", String.class);
    stringMethod.setAccessible(true);

    String testString = "a\"b\\c\nd\re\t\u2028\u2029";
    stringMethod.invoke(jsonWriter, testString);
    jsonWriter.flush();

    String output = stringWriter.toString();
    // Should start and end with quotes
    assertTrue(output.startsWith("\""));
    assertTrue(output.endsWith("\""));
    // Should escape quotes, backslash, newline, carriage return, tab, and unicode line/paragraph separator
    assertTrue(output.contains("\\\""));
    assertTrue(output.contains("\\\\"));
    assertTrue(output.contains("\\n"));
    assertTrue(output.contains("\\r"));
    assertTrue(output.contains("\\t"));
    assertTrue(output.contains("\\u2028"));
    assertTrue(output.contains("\\u2029"));
  }

  // Helper to get separator field value
  private String separator() throws Exception {
    var separatorField = JsonWriter.class.getDeclaredField("separator");
    separatorField.setAccessible(true);
    return (String) separatorField.get(jsonWriter);
  }
}