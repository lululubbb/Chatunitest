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

public class InterceptorFactory_305_3Test {

  @Mock
  private Gson mockGson;

  @Mock
  private TypeAdapter<ClassWithIntercept> mockDelegateAdapter;

  private InterceptorFactory interceptorFactory;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    interceptorFactory = new InterceptorFactory();
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnNullWhenNoInterceptAnnotation() {
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    TypeAdapter<?> adapter = interceptorFactory.create(mockGson, typeToken);
    assertNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void create_shouldReturnInterceptorAdapterWhenInterceptAnnotationPresent() {
    TypeToken<ClassWithIntercept> typeToken = TypeToken.get(ClassWithIntercept.class);
    when(mockGson.getDelegateAdapter(interceptorFactory, typeToken)).thenReturn(mockDelegateAdapter);

    TypeAdapter<ClassWithIntercept> adapter = interceptorFactory.create(mockGson, typeToken);

    assertNotNull(adapter);
    assertEquals("com.google.gson.interceptors.InterceptorAdapter", adapter.getClass().getName());
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface Intercept {
  }

  @Intercept
  static class ClassWithIntercept {
  }
}