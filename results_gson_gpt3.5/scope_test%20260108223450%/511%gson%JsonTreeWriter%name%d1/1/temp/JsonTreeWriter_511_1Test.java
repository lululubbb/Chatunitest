package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_511_1Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testName_nullName_throwsNullPointerException() {
    NullPointerException ex = assertThrows(NullPointerException.class, () -> writer.name(null));
    assertEquals("name == null", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testName_stackEmpty_throwsIllegalStateException() throws Exception {
    // stack is empty initially
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("any"));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void testName_pendingNameNotNull_throwsIllegalStateException() throws Exception {
    setStackWithJsonObject();
    setPendingName("alreadySet");
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("newName"));
    assertNotNull(ex);
  }

  @Test
    @Timeout(8000)
  public void testName_topOfStackIsJsonObject_setsPendingNameAndReturnsThis() throws Exception {
    setStackWithJsonObject();
    setPendingName(null);
    JsonWriter result = writer.name("myName");
    assertSame(writer, result);
    assertEquals("myName", getPendingName());
  }

  @Test
    @Timeout(8000)
  public void testName_topOfStackNotJsonObject_throwsIllegalStateException() throws Exception {
    setStackWithJsonArray();
    setPendingName(null);
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> writer.name("name"));
    assertNotNull(ex);
  }

  private void setStackWithJsonObject() throws Exception {
    List<JsonElement> stack = getStack();
    stack.clear();
    stack.add(new JsonObject());
    setStack(stack);
  }

  private void setStackWithJsonArray() throws Exception {
    List<JsonElement> stack = getStack();
    stack.clear();
    // Use reflection to create a JsonArray instance
    Class<?> jsonArrayClass = Class.forName("com.google.gson.JsonArray");
    JsonElement jsonArray = (JsonElement) jsonArrayClass.getDeclaredConstructor().newInstance();
    stack.add(jsonArray);
    setStack(stack);
  }

  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack() throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  private void setStack(List<JsonElement> stack) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(writer, stack);
  }

  private String getPendingName() throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    return (String) pendingNameField.get(writer);
  }

  private void setPendingName(String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }
}