package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MapTypeAdapterFactory_595_5Test {

  @Mock
  private ConstructorConstructor mockConstructorConstructor;

  @Mock
  private Gson mockGson;

  @Mock
  private ObjectConstructor<Object> mockObjectConstructor;

  @Mock
  private TypeAdapter<Object> mockKeyAdapter;

  @Mock
  private TypeAdapter<Object> mockValueAdapter;

  private MapTypeAdapterFactory mapTypeAdapterFactory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mapTypeAdapterFactory = new MapTypeAdapterFactory(mockConstructorConstructor, true);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnNullIfNotMapType() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<String> adapter = mapTypeAdapterFactory.create(mockGson, stringTypeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_shouldReturnAdapterForMapType() throws Exception {
    TypeToken<Map<String, String>> mapTypeToken = new TypeToken<Map<String, String>>() {};

    // Mock constructorConstructor.get(TypeToken) to return mockObjectConstructor
    when(mockConstructorConstructor.get(mapTypeToken)).thenReturn((ObjectConstructor) mockObjectConstructor);

    // Mock gson.getAdapter(TypeToken) to return mockKeyAdapter first, then mockValueAdapter
    when(mockGson.getAdapter(any(TypeToken.class)))
        .thenAnswer(invocation -> {
          TypeToken<?> typeToken = invocation.getArgument(0);
          if (typeToken.getType().equals(String.class)) {
            // The first call for key adapter returns mockKeyAdapter
            // The second call for value adapter returns mockValueAdapter
            // We differentiate by call count using a simple counter
            if (!calledOnce) {
              calledOnce = true;
              return mockKeyAdapter;
            } else {
              return mockValueAdapter;
            }
          }
          return null;
        });

    // Use reflection to inject mockConstructorConstructor into mapTypeAdapterFactory
    Field constructorConstructorField = MapTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    constructorConstructorField.set(mapTypeAdapterFactory, mockConstructorConstructor);

    // Create the adapter by calling create()
    TypeAdapter<Map<String, String>> adapter = mapTypeAdapterFactory.create(mockGson, mapTypeToken);

    assertNotNull(adapter);

    // Use reflection to get the Adapter inner class
    Class<?> adapterClass = null;
    for (Class<?> innerClass : MapTypeAdapterFactory.class.getDeclaredClasses()) {
      if ("Adapter".equals(innerClass.getSimpleName())) {
        adapterClass = innerClass;
        break;
      }
    }
    assertNotNull(adapterClass);
    assertEquals(adapterClass, adapter.getClass());

    // Now replace the keyAdapter field inside Adapter instance with mockKeyAdapter via reflection
    Field keyAdapterField = adapterClass.getDeclaredField("keyTypeAdapter");
    keyAdapterField.setAccessible(true);
    keyAdapterField.set(adapter, mockKeyAdapter);

    // Verify interactions
    verify(mockConstructorConstructor).get(mapTypeToken);
    verify(mockGson, times(2)).getAdapter(TypeToken.get(String.class));
  }

  // Helper field for tracking calls in the Answer
  private boolean calledOnce = false;
}