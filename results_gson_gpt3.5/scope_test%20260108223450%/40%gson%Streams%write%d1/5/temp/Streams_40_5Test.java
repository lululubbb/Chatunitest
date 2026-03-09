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
import com.google.gson.internal.bind.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Streams_40_5Test {

  @Test
    @Timeout(8000)
  void write_CallsTypeAdaptersJsonElementWrite() throws Exception {
    JsonElement element = mock(JsonElement.class);
    JsonWriter writer = mock(JsonWriter.class);

    try (MockedStatic<TypeAdapters> typeAdaptersMockedStatic = Mockito.mockStatic(TypeAdapters.class)) {
      // Get the JSON_ELEMENT field via reflection
      Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
      jsonElementField.setAccessible(true);
      Object jsonElementAdapter = jsonElementField.get(null);

      // Spy on the existing JSON_ELEMENT instance (cast to TypeAdapter<?>)
      TypeAdapter<?> jsonElementAdapterSpy = spy((TypeAdapter<?>) jsonElementAdapter);

      // Use a block lambda to satisfy compiler for Supplier interface
      typeAdaptersMockedStatic.when(() -> TypeAdapters.JSON_ELEMENT).thenAnswer(invocation -> jsonElementAdapterSpy);

      // Call the method under test
      Streams.write(element, writer);

      // Verify the write method was called exactly once on the spy
      verify(jsonElementAdapterSpy, times(1)).write(writer, element);
    }
  }
}