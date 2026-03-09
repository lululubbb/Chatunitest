package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class JsonAdapterAnnotationTypeAdapterFactory_298_4Test {

  @Mock
  private ConstructorConstructor constructorConstructor;
  @Mock
  private Gson gson;

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  public void create_noAnnotation_returnsNull() {
    TypeToken<NoAnnotationClass> targetType = TypeToken.get(NoAnnotationClass.class);
    TypeAdapter<?> adapter = factory.create(gson, targetType);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_withJsonAdapterAnnotation_returnsTypeAdapter() throws Exception {
    TypeToken<WithJsonAdapterClass> targetType = TypeToken.get(WithJsonAdapterClass.class);

    // Prepare a TypeAdapter mock to be returned by getTypeAdapter
    TypeAdapter<?> expectedAdapter = mock(TypeAdapter.class);

    // Use reflection to invoke private getTypeAdapter method
    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    JsonAdapter annotation = WithJsonAdapterClass.class.getAnnotation(JsonAdapter.class);

    // Mock getTypeAdapter call by spying factory instance
    JsonAdapterAnnotationTypeAdapterFactory spyFactory = spy(factory);
    doReturn(expectedAdapter).when(spyFactory).getTypeAdapter(constructorConstructor, gson, targetType, annotation);

    TypeAdapter<?> actualAdapter = spyFactory.create(gson, targetType);

    assertSame(expectedAdapter, actualAdapter);
  }

  // Helper class without @JsonAdapter annotation
  private static class NoAnnotationClass {
  }

  // Helper class with @JsonAdapter annotation
  @JsonAdapter(MockTypeAdapter.class)
  private static class WithJsonAdapterClass {
  }

  // Mock TypeAdapter for annotation value
  private static class MockTypeAdapter extends TypeAdapter<Object> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }

    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }
}