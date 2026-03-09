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

final class JsonAdapterAnnotationTypeAdapterFactory_297_5Test {

  @Mock ConstructorConstructor constructorConstructor;
  @Mock Gson gson;

  JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  static class TestClass {}

  @JsonAdapter(TestTypeAdapter.class)
  static class AnnotatedClass {}

  static class TestTypeAdapter extends TypeAdapter<TestClass> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, TestClass value) {
      // no-op
    }

    @Override
    public TestClass read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }

  @Test
    @Timeout(8000)
  void create_withNoAnnotation_returnsNull() {
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  void create_withJsonAdapterAnnotation_returnsTypeAdapter() {
    TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);
    // We mock ConstructorConstructor to return a TestTypeAdapter instance when asked for TestTypeAdapter class
    when(constructorConstructor.get(TypeToken.get(TestTypeAdapter.class))).thenReturn(() -> new TestTypeAdapter());

    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNotNull(adapter);
    // Instead of checking exact class, check that adapter delegates to TestTypeAdapter
    // The returned adapter is wrapped by nullSafe() by default, so unwrap if possible
    TypeAdapter<?> unwrapped = unwrapNullSafe(adapter);
    assertTrue(unwrapped instanceof TestTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void getTypeAdapter_withTypeAdapterClass_returnsInstance() throws Exception {
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return JsonAdapter.class;
      }

      @Override
      public Class<?> value() {
        return TestTypeAdapter.class;
      }

      @Override
      public boolean nullSafe() {
        return true;
      }
    };

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    when(constructorConstructor.get(TypeToken.get(TestTypeAdapter.class))).thenReturn(() -> new TestTypeAdapter());

    Object result = method.invoke(factory, constructorConstructor, gson, TypeToken.get(TestClass.class), annotation);
    assertNotNull(result);
    assertTrue(result instanceof TypeAdapter);
    TypeAdapter<?> adapter = (TypeAdapter<?>) result;
    TypeAdapter<?> unwrapped = unwrapNullSafe(adapter);
    assertTrue(unwrapped instanceof TestTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void getTypeAdapter_withSerializerAndDeserializer_returnsCompositeTypeAdapter() throws Exception {
    class Serializer implements JsonSerializer<TestClass> {
      @Override
      public com.google.gson.JsonElement serialize(TestClass src, java.lang.reflect.Type typeOfSrc,
          com.google.gson.JsonSerializationContext context) {
        return null;
      }
    }
    class Deserializer implements JsonDeserializer<TestClass> {
      @Override
      public TestClass deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
          com.google.gson.JsonDeserializationContext context) {
        return null;
      }
    }

    class AdapterBoth implements JsonSerializer<TestClass>, JsonDeserializer<TestClass> {
      @Override
      public com.google.gson.JsonElement serialize(TestClass src, java.lang.reflect.Type typeOfSrc,
          com.google.gson.JsonSerializationContext context) {
        return null;
      }

      @Override
      public TestClass deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
          com.google.gson.JsonDeserializationContext context) {
        return null;
      }
    }

    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return JsonAdapter.class;
      }

      @Override
      public Class<?> value() {
        return AdapterBoth.class;
      }

      @Override
      public boolean nullSafe() {
        return true;
      }
    };

    when(constructorConstructor.get(TypeToken.get(AdapterBoth.class))).thenReturn(AdapterBoth::new);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, constructorConstructor, gson, TypeToken.get(TestClass.class), annotation);

    assertNotNull(result);
    assertTrue(result instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  void getTypeAdapter_withNullSafeFalse_returnsNonNullSafeAdapter() throws Exception {
    JsonAdapter annotation = new JsonAdapter() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return JsonAdapter.class;
      }

      @Override
      public Class<?> value() {
        return TestTypeAdapter.class;
      }

      @Override
      public boolean nullSafe() {
        return false;
      }
    };

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    when(constructorConstructor.get(TypeToken.get(TestTypeAdapter.class))).thenReturn(() -> new TestTypeAdapter());

    Object result = method.invoke(factory, constructorConstructor, gson, TypeToken.get(TestClass.class), annotation);

    assertNotNull(result);
    // The returned adapter should be exactly TestTypeAdapter (no nullSafe wrapper)
    assertEquals(TestTypeAdapter.class, result.getClass());
  }

  /**
   * Helper method to unwrap the nullSafe wrapper TypeAdapter to get the original delegate.
   */
  private static TypeAdapter<?> unwrapNullSafe(TypeAdapter<?> adapter) {
    try {
      // The nullSafe() method returns a new TypeAdapter anonymous class that holds a delegate field
      // We try to reflectively get the delegate field if present
      for (Class<?> clazz = adapter.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
        try {
          var field = clazz.getDeclaredField("delegate");
          field.setAccessible(true);
          Object delegate = field.get(adapter);
          if (delegate instanceof TypeAdapter) {
            return (TypeAdapter<?>) delegate;
          }
        } catch (NoSuchFieldException ignored) {
          // Try superclass
        }
      }
    } catch (Exception ignored) {
    }
    return adapter; // return original if no delegate found
  }
}