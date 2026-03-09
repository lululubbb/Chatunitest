package com.google.gson.interceptors;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class InterceptorFactory_305_5Test {

  @Mock
  private Gson gson;

  @Mock
  private TypeAdapter<AnnotatedClass.Inner> delegateAdapter;

  private InterceptorFactory interceptorFactory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    interceptorFactory = new InterceptorFactory();
  }

  @Retention(RetentionPolicy.RUNTIME)
  public @interface Intercept {
  }

  static class AnnotatedClass {
    @Intercept
    static class Inner {
    }
  }

  static class NonAnnotatedClass {
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnNullWhenNoInterceptAnnotation() {
    TypeToken<NonAnnotatedClass> typeToken = TypeToken.get(NonAnnotatedClass.class);

    TypeAdapter<?> adapter = interceptorFactory.create(gson, typeToken);

    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnInterceptorAdapterWhenInterceptAnnotationPresent() {
    TypeToken<AnnotatedClass.Inner> typeToken = TypeToken.get(AnnotatedClass.Inner.class);

    when(gson.getDelegateAdapter(interceptorFactory, typeToken)).thenReturn(delegateAdapter);

    TypeAdapter<?> adapter = interceptorFactory.create(gson, typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.interceptors.InterceptorAdapter", adapter.getClass().getName());
  }
}