package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class Streams_40_3Test {

  @Test
    @Timeout(8000)
  void write_callsTypeAdaptersJsonElementWrite() throws Exception {
    JsonElement element = mock(JsonElement.class);
    JsonWriter writer = mock(JsonWriter.class);

    try (MockedStatic<TypeAdapters> mockedTypeAdapters = Mockito.mockStatic(TypeAdapters.class)) {
      // Access the JSON_ELEMENT field
      Field jsonElementField = TypeAdapters.class.getDeclaredField("JSON_ELEMENT");
      jsonElementField.setAccessible(true);
      Class<?> typeAdapterClass = jsonElementField.getType();

      // Create a mock of the TypeAdapter<JsonElement> type
      Object jsonElementAdapterMock = mock(typeAdapterClass);

      // Mock the static field JSON_ELEMENT to return our mock
      mockedTypeAdapters.when(() -> jsonElementField.get(null)).thenReturn(jsonElementAdapterMock);

      // Get the write method
      Method writeMethod = typeAdapterClass.getMethod("write", JsonWriter.class, JsonElement.class);

      // Stub the write method on the mock to do nothing
      // Use Mockito.doNothing().when(mock).write(writer, element) but we need to invoke via reflection
      // So create a proxy to invoke write with reflection and stub it via Mockito
      // Instead, use Mockito's doAnswer on the mock's write method via a dynamic proxy

      // Since jsonElementAdapterMock is a mock of typeAdapterClass, we can use Mockito's doNothing() with a cast proxy:
      Object castedMock = jsonElementAdapterMock;
      // We cannot cast directly at compile time, so use reflection proxy invocation:
      // Use Mockito's doNothing().when(jsonElementAdapterMock).write(writer, element);
      // But need to invoke via reflection:

      // Use Mockito's doAnswer on the method:
      doAnswer(invocation -> null)
          .when(jsonElementAdapterMock)
          .getClass()
          .getMethod("write", JsonWriter.class, JsonElement.class)
          .invoke(jsonElementAdapterMock, writer, element);

      // The above line causes the same error, so instead stub via Mockito's 'when' on the method call directly:
      // We can use Mockito's doNothing().when(mock).write(writer, element) by creating a typed proxy using java.lang.reflect.Proxy

      // Create a proxy interface to cast the mock:
      // The TypeAdapter class has a public method 'write(JsonWriter, JsonElement)', so create an interface with that method
      interface TypeAdapterInterface {
        void write(JsonWriter writer, JsonElement element) throws IOException;
      }

      // Create a proxy to cast the mock to the interface
      TypeAdapterInterface typedMock = (TypeAdapterInterface) jsonElementAdapterMock;

      // Stub the write method to do nothing
      doNothing().when(typedMock).write(writer, element);

      // Call the method under test
      Streams.write(element, writer);

      // Verify that write was called once with correct arguments
      verify(typedMock, times(1)).write(writer, element);
    }
  }
}