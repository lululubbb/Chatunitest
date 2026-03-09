package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class CollectionTypeAdapterFactory_120_3Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Mock
  private TypeAdapter<?> elementTypeAdapter;

  @Mock
  private ObjectConstructor<?> objectConstructor;

  private CollectionTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new CollectionTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNullIfNotCollection() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, stringType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnCollectionTypeAdapter() {
    TypeToken<ArrayList<String>> listType = new TypeToken<ArrayList<String>>() {};
    Type elementType = $Gson$Types.getCollectionElementType(listType.getType(), listType.getRawType());

    // Use wildcard capture to avoid generic type mismatch in Mockito stubbing
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter elementAdapter = (TypeAdapter) elementTypeAdapter;

    when(constructorConstructor.get(listType)).thenReturn((ObjectConstructor) objectConstructor);
    when(gson.getAdapter(TypeToken.get(elementType))).thenReturn(elementAdapter);

    TypeAdapter<?> adapter = factory.create(gson, listType);
    assertNotNull(adapter);

    // Verify the adapter is a CollectionTypeAdapterFactory.Adapter instance (private inner class)
    assertEquals("com.google.gson.internal.bind.CollectionTypeAdapterFactory$Adapter", adapter.getClass().getName());

    // Reflection to invoke write and read to ensure coverage
    try {
      // Prepare mocks for JsonWriter and JsonReader
      var jsonWriter = mock(com.google.gson.stream.JsonWriter.class);
      var jsonReader = mock(com.google.gson.stream.JsonReader.class);

      // Mock objectConstructor construct method
      when(objectConstructor.construct()).thenReturn(new ArrayList<>());

      // Mock JsonReader behavior for reading a collection (empty collection)
      when(jsonReader.peek()).thenReturn(com.google.gson.stream.JsonToken.END_ARRAY);

      //noinspection unchecked
      ((TypeAdapter<Collection<String>>) adapter).read(jsonReader);
      verify(jsonReader).beginArray();
      verify(jsonReader).peek();
      verify(jsonReader).endArray();

      // Write an empty collection
      ((TypeAdapter<Collection<String>>) adapter).write(jsonWriter, new ArrayList<>());
      verify(jsonWriter).beginArray();
      verify(jsonWriter).endArray();

    } catch (Exception e) {
      fail("Exception during adapter read/write invocation: " + e.getMessage());
    }
  }
}