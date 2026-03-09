package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_511_4Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void name_nullName_throwsNullPointerException() {
    NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
      writer.name(null);
    });
    assertEquals("name == null", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void name_emptyStack_throwsIllegalStateException() {
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      writer.name("test");
    });
    // message is not specified, so just check type
  }

  @Test
    @Timeout(8000)
  public void name_pendingNameNotNull_throwsIllegalStateException() throws Exception {
    // Setup stack with one JsonObject element
    List<JsonElement> stack = getStack(writer);
    stack.add(new JsonObject());

    // Set pendingName to non-null via reflection
    setPendingName(writer, "pending");

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      writer.name("test");
    });
  }

  @Test
    @Timeout(8000)
  public void name_topOfStackIsJsonObject_setsPendingNameAndReturnsThis() throws Exception {
    List<JsonElement> stack = getStack(writer);
    JsonObject jsonObject = new JsonObject();
    stack.add(jsonObject);

    // Change declared type to JsonWriter to match method signature
    // and cast return to JsonTreeWriter
    JsonWriter returned = writer.name("myName");

    // pendingName field should be set to "myName"
    String pendingName = getPendingName(writer);
    assertEquals("myName", pendingName);
    // returned should be the writer instance itself
    assertSame(writer, returned);
  }

  @Test
    @Timeout(8000)
  public void name_topOfStackIsNotJsonObject_throwsIllegalStateException() throws Exception {
    List<JsonElement> stack = getStack(writer);
    // Add a JsonArray (not JsonObject)
    Class<?> jsonArrayClass = Class.forName("com.google.gson.JsonArray");
    JsonElement jsonArray = (JsonElement) jsonArrayClass.getDeclaredConstructor().newInstance();
    stack.add(jsonArray);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      writer.name("test");
    });
  }

  // Helper to get private field 'stack'
  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack(JsonTreeWriter writer) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  // Helper to get private field 'pendingName'
  private String getPendingName(JsonTreeWriter writer) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    return (String) pendingNameField.get(writer);
  }

  // Helper to set private field 'pendingName'
  private void setPendingName(JsonTreeWriter writer, String value) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, value);
  }
}