package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_511_6Test {

  private JsonTreeWriter writer;

  @BeforeEach
  void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void name_nullName_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> writer.name(null));
    assertEquals("name == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void name_stackEmpty_throwsIllegalStateException() throws Exception {
    // stack is empty initially
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.name("any"));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void name_pendingNameNotNull_throwsIllegalStateException() throws Exception {
    // Prepare stack with a JsonObject and set pendingName not null by reflection
    setStackWithJsonObject();
    setPendingName("pending");

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.name("newName"));
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void name_topOfStackIsJsonObject_setsPendingNameAndReturnsThis() throws Exception {
    setStackWithJsonObject();

    JsonWriter returned = writer.name("myName");

    // Verify returned this
    assertSame(writer, returned);

    // Verify pendingName is set to "myName"
    String pendingName = getPendingName();
    assertEquals("myName", pendingName);
  }

  @Test
    @Timeout(8000)
  void name_topOfStackNotJsonObject_throwsIllegalStateException() throws Exception {
    setStackWithJsonArray();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.name("myName"));
    assertNotNull(thrown);
  }

  // Helper method to set stack with a single JsonObject element
  private void setStackWithJsonObject() throws Exception {
    JsonObject jsonObject = new JsonObject();
    setStack(List.of(jsonObject));
    setPendingName(null);
  }

  // Helper method to set stack with a single JsonArray element
  private void setStackWithJsonArray() throws Exception {
    JsonArray jsonArray = new JsonArray();
    setStack(List.of(jsonArray));
    setPendingName(null);
  }

  // Reflection helper to set stack field
  @SuppressWarnings("unchecked")
  private void setStack(List<JsonElement> elements) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();
    stack.addAll(elements);
  }

  // Reflection helper to set pendingName field
  private void setPendingName(String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }

  // Reflection helper to get pendingName field
  private String getPendingName() throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    return (String) pendingNameField.get(writer);
  }
}