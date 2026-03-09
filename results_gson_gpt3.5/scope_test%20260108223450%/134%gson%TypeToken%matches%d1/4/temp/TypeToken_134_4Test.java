package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class TypeTokenMatchesTest {

  @Test
    @Timeout(8000)
  void matches_shouldReturnTrue_whenTypesAreEqual() throws Exception {
    Type type = String.class;
    Map<String, Type> typeMap = new HashMap<>();

    boolean result = invokeMatches(type, type, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_shouldReturnTrue_whenFromIsTypeVariable_andToEqualsMappedType() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    when(typeVariable.getName()).thenReturn("T");

    Type mappedType = Integer.class;
    Map<String, Type> typeMap = new HashMap<>();
    typeMap.put("T", mappedType);

    boolean result = invokeMatches(typeVariable, mappedType, typeMap);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void matches_shouldReturnFalse_whenFromIsTypeVariable_andToDoesNotEqualMappedType() throws Exception {
    @SuppressWarnings("unchecked")
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    when(typeVariable.getName()).thenReturn("T");

    Type mappedType = Integer.class;
    Map<String, Type> typeMap = new HashMap<>();
    typeMap.put("T", mappedType);

    Type differentType = String.class;

    boolean result = invokeMatches(typeVariable, differentType, typeMap);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void matches_shouldReturnFalse_whenTypesAreDifferentAndFromIsNotTypeVariable() throws Exception {
    Type from = Integer.class;
    Type to = String.class;
    Map<String, Type> typeMap = new HashMap<>();

    boolean result = invokeMatches(from, to, typeMap);

    assertFalse(result);
  }

  private boolean invokeMatches(Type from, Type to, Map<String, Type> typeMap) throws Exception {
    var method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);
    return (boolean) method.invoke(null, from, to, typeMap);
  }
}