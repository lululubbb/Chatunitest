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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.lang.reflect.Method;

public class JsonAdapterAnnotationTypeAdapterFactory_297_4Test {

  @Mock
  private ConstructorConstructor mockConstructorConstructor;

  @Mock
  private Gson mockGson;

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(mockConstructorConstructor);

    // Mock behavior for ConstructorConstructor to return a constructor that returns the adapter instance
    when(mockConstructorConstructor.get(TypeToken.get(TestAdapter.class)))
        .thenReturn(() -> new TestAdapter());
    when(mockConstructorConstructor.get(TypeToken.get(AdapterReturningNull.class)))
        .thenReturn(() -> new AdapterReturningNull());

    // Mock Gson behavior to return a dummy TypeAdapter when requested (to handle delegation)
    when(mockGson.getAdapter(any(TypeToken.class))).thenAnswer(invocation -> {
      TypeToken<?> typeToken = invocation.getArgument(0);
      // Return a dummy TypeAdapter that returns null for read and does nothing for write
      return new TypeAdapter<Object>() {
        @Override
        public void write(JsonWriter out, Object value) {}

        @Override
        public Object read(JsonReader in) {
          return null;
        }
      };
    });
  }

  @JsonAdapter(TestAdapter.class)
  private static class AnnotatedClass {}

  private static class TestAdapter extends TypeAdapter<AnnotatedClass> {
    @Override
    public void write(JsonWriter out, AnnotatedClass value) {
    }

    @Override
    public AnnotatedClass read(JsonReader in) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withJsonAdapterAnnotation_returnsExpectedAdapter() throws Exception {
    TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);
    JsonAdapter annotation = AnnotatedClass.class.getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);

    // Use reflection to access private getTypeAdapter method
    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) getTypeAdapterMethod.invoke(
        factory, mockConstructorConstructor, mockGson, typeToken, annotation);

    assertNotNull(adapter);
    // Use isInstance instead of equals to handle possible proxy or wrapping
    assertTrue(adapter instanceof TestAdapter);
  }

  @JsonAdapter(AdapterReturningNull.class)
  private static class ClassWithNullAdapter {}

  private static class AdapterReturningNull extends TypeAdapter<ClassWithNullAdapter> {
    @Override
    public void write(JsonWriter out, ClassWithNullAdapter value) {}

    @Override
    public ClassWithNullAdapter read(JsonReader in) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withAdapterReturningNull() throws Exception {
    TypeToken<ClassWithNullAdapter> typeToken = TypeToken.get(ClassWithNullAdapter.class);
    JsonAdapter annotation = ClassWithNullAdapter.class.getAnnotation(JsonAdapter.class);
    assertNotNull(annotation);

    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) getTypeAdapterMethod.invoke(
        factory, mockConstructorConstructor, mockGson, typeToken, annotation);

    assertNotNull(adapter);
    // Use isInstance instead of equals to handle possible proxy or wrapping
    assertTrue(adapter instanceof AdapterReturningNull);
  }

  private static class NoJsonAdapter {}

  @Test
    @Timeout(8000)
  void testCreate_withoutJsonAdapterAnnotation_returnsNull() {
    TypeToken<NoJsonAdapter> typeToken = TypeToken.get(NoJsonAdapter.class);
    TypeAdapter<?> adapter = factory.create(mockGson, typeToken);
    assertNull(adapter);
  }
}