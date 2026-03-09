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
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

class Streams_40_2Test {

  @Test
    @Timeout(8000)
  void write_delegatesToTypeAdaptersJsonElementWrite() throws IOException {
    JsonElement element = mock(JsonElement.class);
    JsonWriter writer = mock(JsonWriter.class);

    try {
      // Use reflection to get the JSON_ELEMENT field
      Field field = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
      field.setAccessible(true);

      // Remove final modifier from the field
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

      Object original = field.get(null);

      // Spy on the original JSON_ELEMENT adapter
      Object jsonElementAdapterSpy = spy(original);

      // Replace the JSON_ELEMENT field with spy temporarily
      field.set(null, jsonElementAdapterSpy);

      try {
        Streams.write(element, writer);
        // Verify that the spy's write method was called with writer and element
        verify(jsonElementAdapterSpy).write(writer, element);
      } finally {
        // Restore original field value and modifiers
        field.set(null, original);
        modifiersField.setInt(field, modifiersField.getInt(field) | Modifier.FINAL);
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}