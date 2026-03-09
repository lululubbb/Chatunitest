package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_508_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endArray_whenStackEmpty_throwsIllegalStateException() {
    // stack is empty by default
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  public void endArray_whenPendingNameNotNull_throwsIllegalStateException() throws Exception {
    setField(jsonTreeWriter, "pendingName", "someName");
    // stack must not be empty, add dummy element
    List<JsonElement> stack = getStack(jsonTreeWriter);
    stack.add(new JsonArray());

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsJsonArray_removesTopAndReturnsThis() throws Exception {
    List<JsonElement> stack = getStack(jsonTreeWriter);
    JsonArray jsonArray = new JsonArray();
    stack.add(jsonArray);
    setField(jsonTreeWriter, "pendingName", null);

    JsonWriter returned = jsonTreeWriter.endArray();

    assertSame(jsonTreeWriter, returned);
    assertFalse(stack.contains(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsNotJsonArray_throwsIllegalStateException() throws Exception {
    List<JsonElement> stack = getStack(jsonTreeWriter);
    // Add JsonPrimitive which is not JsonArray
    stack.add(new JsonPrimitive("not array"));
    setField(jsonTreeWriter, "pendingName", null);

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  private List<JsonElement> getStack(JsonTreeWriter writer) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  private void setField(Object obj, String fieldName, Object value) throws Exception {
    Field field = JsonTreeWriter.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(obj, value);
  }
}