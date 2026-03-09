package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TypeAdapterRuntimeTypeWrapper_178_1Test {

  Gson context;
  TypeAdapter<Object> delegate;
  JsonWriter out;
  TypeAdapterRuntimeTypeWrapper<Object> wrapper;

  // Reflection handles for private methods
  private Method getRuntimeTypeIfMoreSpecificMethod;
  private Method isReflectiveMethod;

  // Reflection handle for private field 'type'
  private Field typeField;

  @BeforeEach
  void setup() throws Exception {
    context = mock(Gson.class);
    delegate = mock(TypeAdapter.class);
    out = mock(JsonWriter.class);
    wrapper = new TypeAdapterRuntimeTypeWrapper<>(context, delegate, Object.class);

    getRuntimeTypeIfMoreSpecificMethod = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("getRuntimeTypeIfMoreSpecific", Type.class, Object.class);
    getRuntimeTypeIfMoreSpecificMethod.setAccessible(true);

    isReflectiveMethod = TypeAdapterRuntimeTypeWrapper.class.getDeclaredMethod("isReflective", TypeAdapter.class);
    isReflectiveMethod.setAccessible(true);

    typeField = TypeAdapterRuntimeTypeWrapper.class.getDeclaredField("type");
    typeField.setAccessible(true);
  }

  // Helper to invoke private getRuntimeTypeIfMoreSpecific
  private Type getRuntimeTypeIfMoreSpecific(Type declaredType, Object value) throws Exception {
    return (Type) getRuntimeTypeIfMoreSpecificMethod.invoke(null, declaredType, value);
  }

  // Helper to invoke private isReflective
  private boolean isReflective(TypeAdapter<?> adapter) throws Exception {
    return (boolean) isReflectiveMethod.invoke(null, adapter);
  }

  // Helper to create a spy wrapper that overrides getRuntimeTypeIfMoreSpecific and isReflective via reflection
  private TypeAdapterRuntimeTypeWrapper<Object> createSpyWrapperWithOverrides(Type runtimeType, Boolean runtimeTypeAdapterIsReflective, Boolean delegateIsReflective, Object value, TypeAdapter<Object> runtimeTypeAdapter) throws Exception {
    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = Mockito.spy(wrapper);

    doAnswer(invocation -> {
      JsonWriter outArg = invocation.getArgument(0);
      Object valueArg = invocation.getArgument(1);

      Type declaredType = (Type) typeField.get(spyWrapper);

      // Use overridden runtimeType if provided, else call real private method
      Type actualRuntimeType = runtimeType != null ? runtimeType : getRuntimeTypeIfMoreSpecific(declaredType, valueArg);

      TypeAdapter<Object> chosen = delegate;

      if (actualRuntimeType != declaredType) {
        @SuppressWarnings("unchecked")
        TypeAdapter<Object> runtimeAdapter = runtimeTypeAdapter != null ? runtimeTypeAdapter : (TypeAdapter<Object>) context.getAdapter(TypeToken.get((Class<?>) actualRuntimeType));

        boolean runtimeReflective = runtimeTypeAdapterIsReflective != null ? runtimeTypeAdapterIsReflective : isReflective(runtimeAdapter);
        boolean delegateReflective = delegateIsReflective != null ? delegateIsReflective : isReflective(delegate);

        if (!(runtimeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
          chosen = runtimeAdapter;
        } else if (!delegateReflective) {
          chosen = delegate;
        } else {
          chosen = runtimeAdapter;
        }
      }
      chosen.write(outArg, valueArg);
      return null;
    }).when(spyWrapper).write(any(JsonWriter.class), any());

    return spyWrapper;
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeSameAsDeclared_usesDelegateWrite() throws Exception {
    Object value = new Object();

    Type runtimeType = Object.class;

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = createSpyWrapperWithOverrides(runtimeType, null, null, value, null);

    spyWrapper.write(out, value);

    verify(delegate).write(out, value);
    verifyNoMoreInteractions(delegate);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterNotReflective_usesRuntimeTypeAdapter() throws Exception {
    Object value = "test";
    Type runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> runtimeTypeAdapter = mock(TypeAdapter.class);

    // Fix: Remove explicit generic type parameter and cast to Class<?> for TypeToken.get
    when(context.getAdapter(TypeToken.get((Class<?>) runtimeType))).thenReturn(runtimeTypeAdapter);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = createSpyWrapperWithOverrides(runtimeType, false, false, value, runtimeTypeAdapter);

    spyWrapper.write(out, value);

    verify(runtimeTypeAdapter).write(out, value);
    verifyNoMoreInteractions(delegate);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterReflective_delegateNotReflective_usesDelegate() throws Exception {
    Object value = "test";
    Type runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> runtimeTypeAdapter = mock(TypeAdapter.class);

    // Fix: Remove explicit generic type parameter and cast to Class<?> for TypeToken.get
    when(context.getAdapter(TypeToken.get((Class<?>) runtimeType))).thenReturn(runtimeTypeAdapter);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = createSpyWrapperWithOverrides(runtimeType, true, false, value, runtimeTypeAdapter);

    spyWrapper.write(out, value);

    verify(delegate).write(out, value);
    verifyNoMoreInteractions(runtimeTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void write_runtimeTypeDifferent_runtimeTypeAdapterReflective_delegateReflective_usesRuntimeTypeAdapter() throws Exception {
    Object value = "test";
    Type runtimeType = String.class;

    @SuppressWarnings("unchecked")
    TypeAdapter<Object> runtimeTypeAdapter = mock(TypeAdapter.class);

    // Fix: Remove explicit generic type parameter and cast to Class<?> for TypeToken.get
    when(context.getAdapter(TypeToken.get((Class<?>) runtimeType))).thenReturn(runtimeTypeAdapter);

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = createSpyWrapperWithOverrides(runtimeType, true, true, value, runtimeTypeAdapter);

    spyWrapper.write(out, value);

    verify(runtimeTypeAdapter).write(out, value);
    verifyNoMoreInteractions(delegate);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_runtimeTypeSame_usesDelegate() throws Exception {
    Object value = null;

    TypeAdapterRuntimeTypeWrapper<Object> spyWrapper = createSpyWrapperWithOverrides(Object.class, null, null, value, null);

    spyWrapper.write(out, value);

    verify(delegate).write(out, value);
  }
}