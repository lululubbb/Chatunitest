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

public class JsonAdapterAnnotationTypeAdapterFactory_298_3Test {

  @Mock
  private ConstructorConstructor constructorConstructor;
  @Mock
  private Gson gson;
  @Mock
  private TypeAdapter<?> typeAdapter;

  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @JsonAdapter(TestTypeAdapter.class)
  private static class AnnotatedClass {}

  private static class NonAnnotatedClass {}

  private static class TestTypeAdapter extends TypeAdapter<Object> {
    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }

    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {}
  }

  @Test
    @Timeout(8000)
  public void create_returnsNullIfNoAnnotation() {
    TypeToken<NonAnnotatedClass> typeToken = TypeToken.get(NonAnnotatedClass.class);
    TypeAdapter<NonAnnotatedClass> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_returnsTypeAdapterFromGetTypeAdapter() throws Exception {
    TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);

    // Use reflection to access private getTypeAdapter method
    Method getTypeAdapterMethod = JsonAdapterAnnotationTypeAdapterFactory.class
        .getDeclaredMethod("getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    getTypeAdapterMethod.setAccessible(true);

    JsonAdapter annotation = AnnotatedClass.class.getAnnotation(JsonAdapter.class);

    // Mock getTypeAdapter to return our mocked typeAdapter
    JsonAdapterAnnotationTypeAdapterFactory spyFactory = spy(factory);
    doReturn(typeAdapter).when(spyFactory).getTypeAdapter(constructorConstructor, gson, typeToken, annotation);

    TypeAdapter<AnnotatedClass> adapter = spyFactory.create(gson, typeToken);

    assertNotNull(adapter);
    assertSame(typeAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_invocationViaReflection_notNull() throws Exception {
    TypeToken<AnnotatedClass> typeToken = TypeToken.get(AnnotatedClass.class);
    JsonAdapter annotation = AnnotatedClass.class.getAnnotation(JsonAdapter.class);

    // Mock constructorConstructor to avoid NPE inside getTypeAdapter
    when(constructorConstructor.get(any())).thenReturn(() -> new TestTypeAdapter());

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class.getDeclaredMethod("getTypeAdapter",
        ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object result = method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

    assertNotNull(result);
    assertTrue(result instanceof TypeAdapter<?>);
  }
}