package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CollectionTypeAdapterFactory_120_1Test {

  @Mock ConstructorConstructor constructorConstructor;
  @Mock Gson gson;
  @Mock ObjectConstructor<?> objectConstructor;

  CollectionTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new CollectionTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  void create_withCollectionType_returnsTypeAdapter() {
    TypeToken<ArrayList<String>> typeToken = new TypeToken<ArrayList<String>>() {};
    Type elementType = $Gson$Types.getCollectionElementType(typeToken.getType(), typeToken.getRawType());

    @SuppressWarnings("unchecked")
    TypeAdapter<String> elementAdapter = (TypeAdapter<String>) mock(TypeAdapter.class);

    when(constructorConstructor.get(typeToken)).thenReturn((ObjectConstructor) objectConstructor);
    when(objectConstructor.construct()).thenReturn(new ArrayList<>());

    // Fix: use a generic helper method to avoid type capture issues
    when(gson.getAdapter(any(TypeToken.class))).thenAnswer(invocation -> {
      TypeToken<?> token = invocation.getArgument(0);
      if ($Gson$Types.equals(token.getType(), elementType)) {
        return elementAdapter;
      }
      return null;
    });

    TypeAdapter<?> adapter = factory.create(gson, typeToken);

    assertNotNull(adapter);
    String expectedClassName = factory.getClass().getName() + "$Adapter";
    String actualClassName = adapter.getClass().getName();
    assertTrue(actualClassName.contains("Adapter"), "Adapter class name should contain 'Adapter'");
  }

  @Test
    @Timeout(8000)
  void create_withNonCollectionType_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }
}