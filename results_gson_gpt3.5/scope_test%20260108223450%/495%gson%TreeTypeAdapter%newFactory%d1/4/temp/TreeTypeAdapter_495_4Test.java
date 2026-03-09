package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;

class TreeTypeAdapter_495_4Test {

  @Test
    @Timeout(8000)
  void newFactory_returnsSingleTypeFactoryWithCorrectProperties() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    Object serializer = mock(com.google.gson.JsonSerializer.class);

    TypeAdapterFactory factory = TreeTypeAdapter.newFactory(typeToken, serializer);

    assertNotNull(factory);
    assertEquals("com.google.gson.internal.bind.TreeTypeAdapter$SingleTypeFactory", factory.getClass().getName());

    var clazz = factory.getClass();

    var serializerField = clazz.getDeclaredField("serializer");
    serializerField.setAccessible(true);
    Object internalSerializer = serializerField.get(factory);
    assertSame(serializer, internalSerializer);

    var exactTypeField = clazz.getDeclaredField("exactType");
    exactTypeField.setAccessible(true);
    Object internalExactType = exactTypeField.get(factory);
    assertEquals(typeToken, internalExactType);

    var matchRawTypeField = clazz.getDeclaredField("matchRawType");
    matchRawTypeField.setAccessible(true);
    boolean matchRawType = matchRawTypeField.getBoolean(factory);
    assertFalse(matchRawType);

    var hierarchyTypeField = clazz.getDeclaredField("hierarchyType");
    hierarchyTypeField.setAccessible(true);
    Object hierarchyType = hierarchyTypeField.get(factory);
    assertNull(hierarchyType);
  }
}