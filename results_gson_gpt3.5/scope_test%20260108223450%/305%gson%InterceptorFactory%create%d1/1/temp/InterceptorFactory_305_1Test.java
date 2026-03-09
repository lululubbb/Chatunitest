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

class InterceptorFactory_305_1Test {

  InterceptorFactory interceptorFactory;

  @Mock
  Gson gson;

  @Mock
  TypeAdapter<?> delegateAdapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    interceptorFactory = new InterceptorFactory();
  }

  @Test
    @Timeout(8000)
  void create_ReturnsNull_WhenNoInterceptAnnotation() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> castedDelegateAdapter = (TypeAdapter<String>) delegateAdapter;
    when(gson.getDelegateAdapter(interceptorFactory, typeToken)).thenReturn(castedDelegateAdapter);

    TypeAdapter<String> adapter = interceptorFactory.create(gson, typeToken);

    assertNull(adapter);
    verify(gson, never()).getDelegateAdapter(any(), any());
  }

}