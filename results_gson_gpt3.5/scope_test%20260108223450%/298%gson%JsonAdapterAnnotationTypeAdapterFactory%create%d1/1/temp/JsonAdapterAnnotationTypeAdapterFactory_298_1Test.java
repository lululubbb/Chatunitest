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
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonAdapterAnnotationTypeAdapterFactory_298_1Test {

  JsonAdapterAnnotationTypeAdapterFactory factory;
  ConstructorConstructor constructorConstructor;
  Gson gson;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  public void create_NoJsonAdapterAnnotation_ReturnsNull() {
    TypeToken<FooWithoutAnnotation> typeToken = TypeToken.get(FooWithoutAnnotation.class);
    TypeAdapter<FooWithoutAnnotation> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_WithJsonAdapterAnnotation_ReturnsTypeAdapter() throws Exception {
    TypeToken<FooWithAnnotation> typeToken = TypeToken.get(FooWithAnnotation.class);

    // Use reflection to access private getTypeAdapter method
    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    JsonAdapter annotation = FooWithAnnotation.class.getAnnotation(JsonAdapter.class);
    TypeAdapter<?> expectedAdapter = mock(TypeAdapter.class);

    // Mock behavior of getTypeAdapter method by spying on factory
    JsonAdapterAnnotationTypeAdapterFactory spyFactory = Mockito.spy(factory);
    doReturn(expectedAdapter).when(spyFactory).getTypeAdapter(constructorConstructor, gson, typeToken, annotation);

    TypeAdapter<FooWithAnnotation> adapter = spyFactory.create(gson, typeToken);
    assertNotNull(adapter);
    assertSame(expectedAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_ReturnsSerializerAdapter() throws Exception {
    JsonAdapter annotation = FooWithSerializer.class.getAnnotation(JsonAdapter.class);
    TypeToken<FooWithSerializer> typeToken = TypeToken.get(FooWithSerializer.class);

    JsonSerializer<FooWithSerializer> serializer = mock(JsonSerializer.class);
    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<?> objectConstructor = () -> serializer;

    when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(objectConstructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_ReturnsDeserializerAdapter() throws Exception {
    JsonAdapter annotation = FooWithDeserializer.class.getAnnotation(JsonAdapter.class);
    TypeToken<FooWithDeserializer> typeToken = TypeToken.get(FooWithDeserializer.class);

    JsonDeserializer<FooWithDeserializer> deserializer = mock(JsonDeserializer.class);
    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<?> objectConstructor = () -> deserializer;

    when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(objectConstructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    TypeAdapter<?> adapter = (TypeAdapter<?>) method.invoke(factory, constructorConstructor, gson, typeToken, annotation);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_ThrowsExceptionWhenNoValidAdapter() throws Exception {
    JsonAdapter annotation = FooWithInvalidAdapter.class.getAnnotation(JsonAdapter.class);
    TypeToken<FooWithInvalidAdapter> typeToken = TypeToken.get(FooWithInvalidAdapter.class);

    Object invalidInstance = new Object();
    @SuppressWarnings("unchecked")
    ConstructorConstructor.ObjectConstructor<?> objectConstructor = () -> invalidInstance;

    when(constructorConstructor.get(TypeToken.get(annotation.value()))).thenReturn(objectConstructor);

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod(
        "getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(factory, constructorConstructor, gson, typeToken, annotation);
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
  }

  // Test classes with annotations for testing

  static class FooWithoutAnnotation {}

  @JsonAdapter(FooAdapter.class)
  static class FooWithAnnotation {}

  static class FooAdapter extends TypeAdapter<FooWithAnnotation> {
    @Override public void write(com.google.gson.stream.JsonWriter out, FooWithAnnotation value) {}
    @Override public FooWithAnnotation read(com.google.gson.stream.JsonReader in) {return null;}
  }

  @JsonAdapter(FooSerializer.class)
  static class FooWithSerializer {}

  static class FooSerializer implements JsonSerializer<FooWithSerializer> {
    @Override
    public com.google.gson.JsonElement serialize(FooWithSerializer src, java.lang.reflect.Type typeOfSrc,
                                                com.google.gson.JsonSerializationContext context) {
      return null;
    }
  }

  @JsonAdapter(FooDeserializer.class)
  static class FooWithDeserializer {}

  static class FooDeserializer implements JsonDeserializer<FooWithDeserializer> {
    @Override
    public FooWithDeserializer deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
                                           com.google.gson.JsonDeserializationContext context) {
      return null;
    }
  }

  @JsonAdapter(Object.class)
  static class FooWithInvalidAdapter {}
}