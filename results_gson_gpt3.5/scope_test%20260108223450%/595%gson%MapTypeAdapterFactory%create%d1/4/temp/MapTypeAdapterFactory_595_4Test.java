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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MapTypeAdapterFactory_595_4Test {

  @Mock
  private ConstructorConstructor constructorConstructor;

  @Mock
  private Gson gson;

  @Mock
  private ObjectConstructor<HashMap<String, String>> objectConstructor;

  @Mock
  private TypeAdapter<String> keyAdapter;

  @Mock
  private TypeAdapter<String> valueAdapter;

  private MapTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new MapTypeAdapterFactory(constructorConstructor, true);
  }

  @Test
    @Timeout(8000)
  void create_withNonMapType_returnsNull() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = factory.create(gson, stringTypeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_withMapType_returnsAdapter() throws Exception {
    TypeToken<HashMap<String, String>> typeToken = new TypeToken<HashMap<String, String>>() {};
    Type keyType = String.class;
    Type valueType = String.class;

    // Use reflection to access private method getKeyAdapter
    Method getKeyAdapterMethod = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
    getKeyAdapterMethod.setAccessible(true);

    // Create a helper class that delegates getKeyAdapter calls
    class MapTypeAdapterFactoryHelper extends MapTypeAdapterFactory {
      MapTypeAdapterFactoryHelper(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization) {
        super(constructorConstructor, complexMapKeySerialization);
      }

      @Override
      protected TypeAdapter<?> getKeyAdapter(Gson context, Type keyTypeParam) {
        if (keyTypeParam.equals(keyType)) {
          return keyAdapter;
        }
        try {
          return (TypeAdapter<?>) getKeyAdapterMethod.invoke(this, context, keyTypeParam);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    MapTypeAdapterFactoryHelper factoryWithDelegate = new MapTypeAdapterFactoryHelper(constructorConstructor, true);

    // Mock gson.getAdapter(TypeToken) to return valueAdapter, suppress unchecked warnings
    @SuppressWarnings({"unchecked", "rawtypes"})
    TypeAdapter<?> uncheckedValueAdapter = valueAdapter;
    when(gson.getAdapter((TypeToken) TypeToken.get(valueType))).thenReturn(uncheckedValueAdapter);

    // Mock constructorConstructor.get(typeToken) to return objectConstructor
    @SuppressWarnings("unchecked")
    ObjectConstructor<HashMap<String, String>> uncheckedObjectConstructor = objectConstructor;
    when(constructorConstructor.get(typeToken)).thenReturn(uncheckedObjectConstructor);

    // Call create on factoryWithDelegate to use overridden getKeyAdapter
    @SuppressWarnings("unchecked")
    TypeAdapter<HashMap<String, String>> adapter = (TypeAdapter<HashMap<String, String>>) factoryWithDelegate.create(gson, typeToken);

    assertNotNull(adapter);

    // Access private Adapter class via reflection
    Class<?> adapterClass = null;
    for (Class<?> innerClass : MapTypeAdapterFactory.class.getDeclaredClasses()) {
      if ("Adapter".equals(innerClass.getSimpleName())) {
        adapterClass = innerClass;
        break;
      }
    }
    assertNotNull(adapterClass);
    assertEquals(adapterClass, adapter.getClass());

    // Verify interactions
    verify(gson).getAdapter((TypeToken) TypeToken.get(valueType));
    verify(constructorConstructor).get(typeToken);
  }
}