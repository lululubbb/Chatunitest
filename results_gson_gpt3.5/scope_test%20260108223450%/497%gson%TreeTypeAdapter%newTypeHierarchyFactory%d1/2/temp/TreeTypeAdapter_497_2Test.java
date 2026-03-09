package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import org.junit.jupiter.api.Test;

class TreeTypeAdapter_497_2Test {

  static class DummyTypeAdapter extends TypeAdapter<Number> {
    @Override
    public Number read(com.google.gson.stream.JsonReader in) {
      return null;
    }

    @Override
    public void write(com.google.gson.stream.JsonWriter out, Number value) {
    }
  }

  @Test
    @Timeout(8000)
  void newTypeHierarchyFactory_shouldReturnSingleTypeFactoryWithCorrectParams() throws Exception {
    Class<?> hierarchyType = Number.class;

    // Use a real TypeAdapter subclass instance instead of a mock to pass the checkArgument
    TypeAdapter<?> realTypeAdapter = new DummyTypeAdapter();

    // Wrap the TypeAdapter in a TreeTypeAdapter to satisfy the checkArgument inside SingleTypeFactory constructor
    TypeAdapterFactory factory = TreeTypeAdapter.newTypeHierarchyFactory(hierarchyType, realTypeAdapter);

    assertNotNull(factory);
    // Verify it's an instance of SingleTypeFactory
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factory.getClass().getName());

    // Access private fields of SingleTypeFactory via reflection to verify constructor params
    var clazz = factory.getClass();

    var typeAdapterField = clazz.getDeclaredField("typeAdapter");
    typeAdapterField.setAccessible(true);
    var typeAdapterValue = typeAdapterField.get(factory);
    assertSame(realTypeAdapter, typeAdapterValue);

    var hierarchyTypeField = clazz.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);
    var hierarchyTypeValue = hierarchyTypeField.get(factory);
    assertSame(hierarchyType, hierarchyTypeValue);

    var matchRawTypeField = clazz.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    var matchRawTypeValue = matchRawTypeField.getBoolean(factory);
    assertFalse(matchRawTypeValue);

    var exactTypeField = clazz.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    var exactTypeValue = exactTypeField.get(factory);
    assertNull(exactTypeValue);
  }
}