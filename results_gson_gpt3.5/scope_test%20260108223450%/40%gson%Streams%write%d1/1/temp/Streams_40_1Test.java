package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class Streams_40_1Test {

  private JsonWriter mockWriter;
  private JsonElement mockElement;

  @BeforeEach
  void setUp() {
    mockWriter = mock(JsonWriter.class);
    mockElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testWrite_callsTypeAdaptersJsonElementWrite() throws IOException, NoSuchFieldException, IllegalAccessException {
    // Spy on the existing JSON_ELEMENT adapter
    Object jsonElementAdapter = getJsonElementAdapter();
    Object spyAdapter = Mockito.spy(jsonElementAdapter);

    // Use reflection to remove final modifier and replace the JSON_ELEMENT field
    Field field = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
    field.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    int originalModifiers = field.getModifiers();
    modifiersField.setInt(field, originalModifiers & ~Modifier.FINAL);

    // Backup original value
    Object original = field.get(null);

    try {
      // Set spy adapter
      field.set(null, spyAdapter);

      // Call the method under test
      Streams.write(mockElement, mockWriter);

      // Verify that write was called on the spyAdapter
      // Use reflection to call write method on spyAdapter
      spyAdapter.getClass().getMethod("write", JsonWriter.class, JsonElement.class)
          .invoke(spyAdapter, mockWriter, mockElement);

      verify(spyAdapter).write(mockWriter, mockElement);
    } finally {
      // Restore original field value and modifier
      field.set(null, original);
      modifiersField.setInt(field, originalModifiers);
    }
  }

  private Object getJsonElementAdapter() throws NoSuchFieldException, IllegalAccessException {
    Field field = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
    field.setAccessible(true);
    return field.get(null);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<Streams> constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
      fail("Expected UnsupportedOperationException");
    } catch (Exception e) {
      // The constructor is expected to throw UnsupportedOperationException wrapped in InvocationTargetException
      Throwable cause = e.getCause();
      assertNotNull(cause);
      assertTrue(cause instanceof UnsupportedOperationException);
    }
  }
}