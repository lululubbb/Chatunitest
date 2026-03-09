package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_246_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a JsonElement for constructor (can be any, here JsonNull)
    JsonElement element = JsonNull.INSTANCE;
    jsonTreeReader = new JsonTreeReader(element);
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekReturnsName() throws Exception {
    setPeekReturnValue(JsonToken.NAME);

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      nextJsonElementMethod.invoke(jsonTreeReader);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
    assertTrue(exception.getCause().getMessage().contains("Unexpected NAME"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekReturnsEndArray() throws Exception {
    setPeekReturnValue(JsonToken.END_ARRAY);

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      nextJsonElementMethod.invoke(jsonTreeReader);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
    assertTrue(exception.getCause().getMessage().contains("Unexpected END_ARRAY"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekReturnsEndObject() throws Exception {
    setPeekReturnValue(JsonToken.END_OBJECT);

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      nextJsonElementMethod.invoke(jsonTreeReader);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
    assertTrue(exception.getCause().getMessage().contains("Unexpected END_OBJECT"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldThrowIllegalStateException_whenPeekReturnsEndDocument() throws Exception {
    setPeekReturnValue(JsonToken.END_DOCUMENT);

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
      nextJsonElementMethod.invoke(jsonTreeReader);
    });
    assertTrue(exception.getCause() instanceof IllegalStateException);
    assertTrue(exception.getCause().getMessage().contains("Unexpected END_DOCUMENT"));
  }

  @Test
    @Timeout(8000)
  public void nextJsonElement_shouldReturnElementAndCallSkipValue_whenPeekReturnsValidToken() throws Exception {
    // Setup stack with known JsonElement
    JsonPrimitive primitive = new JsonPrimitive("testValue");
    pushToStack(primitive);

    setPeekReturnValue(JsonToken.STRING);

    Method nextJsonElementMethod = JsonTreeReader.class.getDeclaredMethod("nextJsonElement");
    nextJsonElementMethod.setAccessible(true);

    // Spy on jsonTreeReader to verify skipValue call
    JsonTreeReader spyReader = spy(jsonTreeReader);
    // We must set the stack and stackSize fields of spyReader to match jsonTreeReader's
    setField(spyReader, "stack", getField(jsonTreeReader, "stack"));
    setField(spyReader, "stackSize", getField(jsonTreeReader, "stackSize"));

    JsonElement result = (JsonElement) nextJsonElementMethod.invoke(spyReader);

    assertSame(primitive, result);
    verify(spyReader).skipValue();
  }

  // Helper: set peek() return value by reflection (override peek method)
  private void setPeekReturnValue(JsonToken token) throws Exception {
    // Use spy and override peek() method
    JsonTreeReader spy = spy(jsonTreeReader);
    doReturn(token).when(spy).peek();

    // Replace jsonTreeReader with spy
    jsonTreeReader = spy;
  }

  // Helper: push an element onto stack via reflection
  private void pushToStack(JsonElement element) throws Exception {
    Method pushMethod = JsonTreeReader.class.getDeclaredMethod("push", Object.class);
    pushMethod.setAccessible(true);
    pushMethod.invoke(jsonTreeReader, element);
  }

  // Helper: get private field value
  private Object getField(Object target, String fieldName) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  // Helper: set private field value
  private void setField(Object target, String fieldName, Object value) throws Exception {
    java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}