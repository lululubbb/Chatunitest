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

import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.bind.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class Streams_40_4Test {

  @Test
    @Timeout(8000)
  void write_callsTypeAdaptersJsonElementWrite() throws Exception {
    JsonElement element = mock(JsonElement.class);
    JsonWriter writer = mock(JsonWriter.class);

    // Get original JSON_ELEMENT adapter
    Object jsonElementAdapter = getStaticField(TypeAdapters.class, "JSON_ELEMENT");

    // Cast to TypeAdapter<JsonElement> to access write method and spy properly
    @SuppressWarnings("unchecked")
    TypeAdapter<JsonElement> adapter = (TypeAdapter<JsonElement>) jsonElementAdapter;

    // Create a spy on the adapter
    TypeAdapter<JsonElement> spyAdapter = spy(adapter);

    // Replace the static final field with the spy
    setStaticFinalField(TypeAdapters.class, "JSON_ELEMENT", spyAdapter);

    // Call the method under test
    Streams.write(element, writer);

    // Verify that write(writer, element) was called on the spy
    verify(spyAdapter).write(writer, element);

    // Restore original JSON_ELEMENT field value
    setStaticFinalField(TypeAdapters.class, "JSON_ELEMENT", jsonElementAdapter);
  }

  @Test
    @Timeout(8000)
  void privateConstructor_coverage() throws Exception {
    var constructor = Streams.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    try {
      constructor.newInstance();
    } catch (UnsupportedOperationException e) {
      // Expected exception - ignore for coverage
    }
  }

  private static Object getStaticField(Class<?> clazz, String fieldName) throws Exception {
    var field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(null);
  }

  private static void setStaticFinalField(Class<?> clazz, String fieldName, Object value) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier using reflection
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(null, value);
  }
}