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
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_237_1Test {

  private JsonTreeReader jsonTreeReader;

  @BeforeEach
  public void setUp() throws Exception {
    // Create a mock JsonElement to pass to constructor, since it's required
    // Use null or a mock, as constructor details are not given
    jsonTreeReader = new JsonTreeReader(null);
  }

  @Test
    @Timeout(8000)
  public void expect_whenPeekMatches_expectedDoesNotThrow() throws Throwable {
    // We need to mock peek() to return the expected token
    JsonToken expectedToken = JsonToken.BEGIN_ARRAY;

    // Spy on jsonTreeReader to mock peek()
    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(expectedToken).when(spyReader).peek();

    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    // Should not throw
    expectMethod.invoke(spyReader, expectedToken);
  }

  @Test
    @Timeout(8000)
  public void expect_whenPeekDoesNotMatch_throwsIllegalStateException() throws Throwable {
    JsonToken expectedToken = JsonToken.BEGIN_OBJECT;
    JsonToken actualToken = JsonToken.END_OBJECT;

    JsonTreeReader spyReader = Mockito.spy(jsonTreeReader);
    doReturn(actualToken).when(spyReader).peek();

    Method expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
    expectMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      expectMethod.invoke(spyReader, expectedToken);
    });
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof IllegalStateException);
    String msg = cause.getMessage();
    assertTrue(msg.contains("Expected " + expectedToken.toString()));
    assertTrue(msg.contains("but was " + actualToken.toString()));
  }
}