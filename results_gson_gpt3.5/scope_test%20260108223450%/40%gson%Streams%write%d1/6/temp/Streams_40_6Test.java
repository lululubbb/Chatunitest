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

class Streams_40_6Test {

  @Test
    @Timeout(8000)
  void write_invokesTypeAdaptersJsonElementWrite() throws IOException {
    // Create a real JsonElement instance using JsonNull (a concrete subclass)
    JsonElement element = com.google.gson.JsonNull.INSTANCE;
    JsonWriter writer = mock(JsonWriter.class);

    try {
      // Spy on the original JSON_ELEMENT adapter
      @SuppressWarnings("unchecked")
      var jsonElementAdapterSpy = spy(TypeAdapters.JSON_ELEMENT);

      // Remove final modifier and set the static field to the spy
      Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
      jsonElementField.setAccessible(true);

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(jsonElementField, jsonElementField.getModifiers() & ~Modifier.FINAL);

      // Backup original
      Object original = jsonElementField.get(null);

      try {
        // Set spy
        jsonElementField.set(null, jsonElementAdapterSpy);

        // Call method under test
        Streams.write(element, writer);

        // Verify that write was called on the spy
        verify(jsonElementAdapterSpy).write(writer, element);
      } finally {
        // Restore original field value
        jsonElementField.set(null, original);
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}