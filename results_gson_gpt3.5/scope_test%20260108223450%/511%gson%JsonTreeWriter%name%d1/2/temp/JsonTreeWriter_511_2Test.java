package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_511_2Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void name_nullName_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> writer.name(null));
    assertEquals("name == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_stackEmpty_throwsIllegalStateException() {
    // stack is empty on new instance
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("foo"));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void name_pendingNameNotNull_throwsIllegalStateException() throws Exception {
    // Set up stack with a JsonObject element and pendingName not null
    setStackWithJsonObject();
    setPendingName("pending");

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("foo"));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void name_peekNotJsonObject_throwsIllegalStateException() throws Exception {
    // Set up stack with a JsonArray element (not JsonObject)
    setStackWithJsonArray();

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("foo"));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void name_validName_setsPendingNameAndReturnsThis() throws Exception {
    setStackWithJsonObject();

    JsonWriter result = writer.name("foo");

    assertSame(writer, result);
    assertEquals("foo", getPendingName());
  }

  // Helper to set stack with one JsonObject element
  private void setStackWithJsonObject() throws Exception {
    JsonObject obj = new JsonObject();
    setStack(obj);
  }

  // Helper to set stack with one JsonArray element
  private void setStackWithJsonArray() throws Exception {
    JsonArray arr = new JsonArray();
    setStack(arr);
  }

  // Helper to set the private final field 'stack' to contain the given element
  @SuppressWarnings("unchecked")
  private void setStack(JsonElement element) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();
    stack.add(element);
  }

  // Helper to set the private field 'pendingName'
  private void setPendingName(String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }

  // Helper to get the private field 'pendingName'
  private String getPendingName() throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    return (String) pendingNameField.get(writer);
  }
}