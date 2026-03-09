package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_512_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() throws IOException {
    jsonTreeWriter = new JsonTreeWriter();
    // Begin an object to initialize the stack properly for all tests
    jsonTreeWriter.beginObject();
    // Set a pending name to allow putting a value inside the object
    jsonTreeWriter.name("key");
  }

  @Test
    @Timeout(8000)
  public void value_withNonNullString_callsPutAndReturnsThis() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String testValue = "testString";

    JsonWriter returned = jsonTreeWriter.value(testValue);

    // Validate returned reference
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private peek method and verify stack content
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Object topElement = peekMethod.invoke(jsonTreeWriter);
    assertNotNull(topElement);
    assertTrue(topElement instanceof JsonPrimitive);
    assertEquals(testValue, ((JsonPrimitive) topElement).getAsString());
  }

  @Test
    @Timeout(8000)
  public void value_withNullString_callsNullValueAndReturnsThis() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    jsonTreeWriter.name("key");

    JsonWriter returned = jsonTreeWriter.value((String) null);

    // Validate returned reference
    assertNotNull(returned);
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private peek method and verify stack content is JsonNull
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    Object topElement = peekMethod.invoke(jsonTreeWriter);
    assertNotNull(topElement);
    assertEquals("null", topElement.toString());
  }
}