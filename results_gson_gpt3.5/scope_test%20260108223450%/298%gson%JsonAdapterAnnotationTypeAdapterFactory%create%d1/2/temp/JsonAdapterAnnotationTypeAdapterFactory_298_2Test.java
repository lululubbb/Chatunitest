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

public class JsonAdapterAnnotationTypeAdapterFactory_298_2Test {

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
  public void create_NoAnnotation_ReturnsNull() {
    TypeToken<FooWithoutAnnotation> typeToken = TypeToken.get(FooWithoutAnnotation.class);
    TypeAdapter<FooWithoutAnnotation> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_WithAnnotation_ReturnsTypeAdapter() throws Exception {
    TypeToken<FooWithAnnotation> typeToken = TypeToken.get(FooWithAnnotation.class);

    // Use reflection to get private method getTypeAdapter
    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    JsonAdapter annotation = FooWithAnnotation.class.getAnnotation(JsonAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<FooWithAnnotation> expectedAdapter = mock(TypeAdapter.class);

    // Mock getTypeAdapter to return expectedAdapter when called
    JsonAdapterAnnotationTypeAdapterFactory spyFactory = spy(factory);
    doReturn(expectedAdapter).when(spyFactory).getTypeAdapter(constructorConstructor, gson, typeToken, annotation);

    TypeAdapter<FooWithAnnotation> adapter = spyFactory.create(gson, typeToken);

    assertNotNull(adapter);
    assertEquals(expectedAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_InvokedViaReflection_ReturnsNonNull() throws Exception {
    // Mock ConstructorConstructor to return a TypeAdapter instance for MockTypeAdapter
    when(constructorConstructor.get(TypeToken.get(MockTypeAdapter.class))).thenReturn(() -> new MockTypeAdapter());

    JsonAdapterAnnotationTypeAdapterFactory factoryLocal = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    JsonAdapter annotation = FooWithAnnotation.class.getAnnotation(JsonAdapter.class);
    TypeToken<FooWithAnnotation> typeToken = TypeToken.get(FooWithAnnotation.class);

    Object result = method.invoke(factoryLocal, constructorConstructor, gson, typeToken, annotation);

    assertNotNull(result);
  }

  // Helper classes for tests
  private static class FooWithoutAnnotation {
  }

  @JsonAdapter(MockTypeAdapter.class)
  private static class FooWithAnnotation {
  }

  private static class MockTypeAdapter extends TypeAdapter<FooWithAnnotation> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, FooWithAnnotation value) {
    }

    @Override
    public FooWithAnnotation read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }
}