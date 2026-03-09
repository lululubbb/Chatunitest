package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

class JsonAdapterAnnotationTypeAdapterFactory_297_1Test {

  @Mock
  private ConstructorConstructor mockConstructorConstructor;
  @Mock
  private Gson mockGson;

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor);
  }

  static class DummyType {}

  @JsonAdapter(DummyAdapter.class)
  static class AnnotatedDummy {}

  static class DummyAdapter extends TypeAdapter<DummyType> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, DummyType value) {
    }

    @Override
    public DummyType read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testConstructor() {
    JsonAdapterAnnotationTypeAdapterFactory f = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor);
    assertNotNull(f);
  }

  @Test
    @Timeout(8000)
  void testCreate_withJsonAdapterAnnotation_returnsTypeAdapter() throws Exception {
    TypeToken<AnnotatedDummy> typeToken = TypeToken.get(AnnotatedDummy.class);

    JsonAdapter jsonAdapterAnnotation = AnnotatedDummy.class.getAnnotation(JsonAdapter.class);
    assertNotNull(jsonAdapterAnnotation);

    // Use reflection to access private method getTypeAdapter
    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    // Mock constructorConstructor to return a dummy ObjectConstructor<DummyAdapter> when requested
    DummyAdapter dummyAdapterInstance = new DummyAdapter();
    when(mockConstructorConstructor.get(TypeToken.get(DummyAdapter.class))).thenReturn(() -> dummyAdapterInstance);

    // Invoke private getTypeAdapter method
    TypeAdapter<?> adapter = (TypeAdapter<?>) getTypeAdapterMethod.invoke(factory,
        mockConstructorConstructor, mockGson, typeToken, jsonAdapterAnnotation);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testCreate_withoutJsonAdapterAnnotation_returnsNull() {
    TypeToken<DummyType> typeToken = TypeToken.get(DummyType.class);
    TypeAdapter<?> adapter = factory.create(mockGson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testCreate_withJsonAdapterAnnotation_returnsTypeAdapter_public() {
    TypeToken<AnnotatedDummy> typeToken = TypeToken.get(AnnotatedDummy.class);

    // Mock constructorConstructor to return a dummy ObjectConstructor<DummyAdapter> when requested
    DummyAdapter dummyAdapterInstance = new DummyAdapter();
    when(mockConstructorConstructor.get(TypeToken.get(DummyAdapter.class))).thenReturn(() -> dummyAdapterInstance);

    TypeAdapter<?> adapter = factory.create(mockGson, typeToken);
    assertNotNull(adapter);
  }
}