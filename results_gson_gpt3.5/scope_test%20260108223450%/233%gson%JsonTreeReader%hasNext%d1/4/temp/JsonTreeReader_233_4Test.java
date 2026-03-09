package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

public class JsonTreeReader_233_4Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // We create a minimal JsonElement mock for constructor since real JsonElement is abstract
    // But since JsonTreeReader constructor requires JsonElement, we can mock it with Mockito
    com.google.gson.JsonElement mockElement = mock(com.google.gson.JsonElement.class);
    jsonTreeReader = new JsonTreeReader(mockElement);
  }

  @Test
    @Timeout(8000)
  public void testHasNext_withEndObject() throws Exception {
    // Mock peek() to return END_OBJECT
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(JsonToken.END_OBJECT).when(spyReader).peek();

    assertFalse(spyReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testHasNext_withEndArray() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(JsonToken.END_ARRAY).when(spyReader).peek();

    assertFalse(spyReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testHasNext_withEndDocument() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);
    doReturn(JsonToken.END_DOCUMENT).when(spyReader).peek();

    assertFalse(spyReader.hasNext());
  }

  @Test
    @Timeout(8000)
  public void testHasNext_withOtherTokens() throws Exception {
    JsonTreeReader spyReader = spy(jsonTreeReader);

    // Test all tokens that should return true
    JsonToken[] tokens = {
      JsonToken.BEGIN_ARRAY,
      JsonToken.BEGIN_OBJECT,
      JsonToken.NAME,
      JsonToken.STRING,
      JsonToken.NUMBER,
      JsonToken.BOOLEAN,
      JsonToken.NULL
    };

    for (JsonToken token : tokens) {
      doReturn(token).when(spyReader).peek();
      assertTrue(spyReader.hasNext(), "Expected hasNext() to be true for token: " + token);
    }
  }
}