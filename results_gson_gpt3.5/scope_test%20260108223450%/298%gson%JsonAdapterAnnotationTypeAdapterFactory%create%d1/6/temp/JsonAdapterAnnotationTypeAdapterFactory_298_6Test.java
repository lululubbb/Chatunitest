package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapterFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonAdapterAnnotationTypeAdapterFactory_298_6Test {

  @Mock
  private ConstructorConstructor mockConstructorConstructor;
  @Mock
  private Gson mockGson;
  @Mock
  private TypeAdapter<?> mockTypeAdapter;

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor);
  }

  @JsonAdapter(Adapter.class)
  private static class AnnotatedClass {}

  private static class Adapter extends TypeAdapter<Object> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }

    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }

  private static class NoAnnotationClass {}

  @Test
    @Timeout(8000)
  public void create_WithNoAnnotation_ReturnsNull() {
    TypeToken<NoAnnotationClass> typeToken = TypeToken.get(NoAnnotationClass.class);
    TypeAdapter<?> adapter = factory.create(mockGson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_WithJsonAdapterAnnotation_ReturnsTypeAdapter() throws Exception {
    TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);

    JsonAdapter annotation = AnnotatedClass.class.getAnnotation(JsonAdapter.class);

    // Spy on factory to mock getTypeAdapter call
    JsonAdapterAnnotationTypeAdapterFactory spyFactory = spy(factory);
    doReturn(mockTypeAdapter).when(spyFactory).getTypeAdapter(mockConstructorConstructor, mockGson, typeToken, annotation);

    TypeAdapter<?> adapter = spyFactory.create(mockGson, typeToken);
    assertNotNull(adapter);
    assertEquals(mockTypeAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_ReturnsTypeAdapterInstance() throws Exception {
    TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);
    JsonAdapter annotation = AnnotatedClass.class.getAnnotation(JsonAdapter.class);

    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    // Create a real ConstructorConstructor with required parameters
    ConstructorConstructor realConstructorConstructor = new ConstructorConstructor(
        Collections.emptyMap(),
        false,
        Collections.emptyList()
    );

    Object adapter = getTypeAdapterMethod.invoke(factory, realConstructorConstructor, mockGson, typeToken, annotation);
    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }
}