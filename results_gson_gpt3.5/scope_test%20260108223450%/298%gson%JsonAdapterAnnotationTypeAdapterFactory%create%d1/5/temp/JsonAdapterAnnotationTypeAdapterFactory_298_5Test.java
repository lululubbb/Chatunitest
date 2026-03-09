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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JsonAdapterAnnotationTypeAdapterFactory_298_5Test {

  private ConstructorConstructor constructorConstructor;
  private Gson gson;
  private JsonAdapterAnnotationTypeAdapterFactory factory;

  @BeforeEach
  public void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    gson = mock(Gson.class);
    factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
  }

  @Test
    @Timeout(8000)
  public void create_whenNoAnnotation_returnsNull() {
    TypeToken<NoAnnotationClass> typeToken = TypeToken.get(NoAnnotationClass.class);
    TypeAdapter<?> adapter = factory.create(gson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_whenAnnotationPresent_returnsTypeAdapter() throws Exception {
    TypeToken<WithJsonAdapter> typeToken = TypeToken.get(WithJsonAdapter.class);

    // Spy on factory to mock getTypeAdapter call
    JsonAdapterAnnotationTypeAdapterFactory spyFactory = Mockito.spy(factory);

    TypeAdapter<?> expectedAdapter = mock(TypeAdapter.class);
    JsonAdapter annotation = WithJsonAdapter.class.getAnnotation(JsonAdapter.class);

    doReturn(expectedAdapter).when(spyFactory)
        .getTypeAdapter(constructorConstructor, gson, typeToken, annotation);

    TypeAdapter<?> actualAdapter = spyFactory.create(gson, typeToken);

    assertSame(expectedAdapter, actualAdapter);
  }

  @Test
    @Timeout(8000)
  public void getTypeAdapter_invocation_withMockedParams() throws Exception {
    // Use real annotation from WithJsonAdapter class instead of a mock
    JsonAdapter annotation = WithJsonAdapter.class.getAnnotation(JsonAdapter.class);
    TypeToken<WithJsonAdapter> typeToken = TypeToken.get(WithJsonAdapter.class);

    // Mock ConstructorConstructor to return an instance of Adapter when called
    when(constructorConstructor.get(TypeToken.get(Adapter.class))).thenReturn(() -> new Adapter());

    Method method = JsonAdapterAnnotationTypeAdapterFactory.class
        .getDeclaredMethod("getTypeAdapter", ConstructorConstructor.class, Gson.class, TypeToken.class, JsonAdapter.class);
    method.setAccessible(true);

    Object adapter = method.invoke(factory, constructorConstructor, gson, typeToken, annotation);

    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  // Helper classes for test

  private static class NoAnnotationClass {
  }

  @JsonAdapter(Adapter.class)
  private static class WithJsonAdapter {
  }

  private static class Adapter extends TypeAdapter<Object> {
    @Override
    public void write(com.google.gson.stream.JsonWriter out, Object value) {
    }

    @Override
    public Object read(com.google.gson.stream.JsonReader in) {
      return null;
    }
  }
}