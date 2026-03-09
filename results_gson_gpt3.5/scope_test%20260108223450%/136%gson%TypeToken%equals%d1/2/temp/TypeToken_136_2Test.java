package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeTokenEqualsTest {

  private TypeToken<String> typeTokenString;
  private TypeToken<String> typeTokenStringDifferent;
  private TypeToken<Integer> typeTokenInteger;

  @BeforeEach
  void setUp() {
    typeTokenString = new TypeToken<String>() {};
    typeTokenStringDifferent = new TypeToken<String>() {};
    typeTokenInteger = new TypeToken<Integer>() {};
  }

  @Test
    @Timeout(8000)
  void equals_sameInstance_returnsTrue() {
    assertTrue(typeTokenString.equals(typeTokenString));
  }

  @Test
    @Timeout(8000)
  void equals_null_returnsFalse() {
    assertFalse(typeTokenString.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_differentClass_returnsFalse() {
    assertFalse(typeTokenString.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void equals_differentType_returnsFalse() {
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      Type thisType = getTypeField(typeTokenString);
      Type otherType = getTypeField(typeTokenInteger);
      mocked.when(() -> $Gson$Types.equals(thisType, otherType)).thenReturn(false);
      assertFalse(typeTokenString.equals(typeTokenInteger));
      mocked.verify(() -> $Gson$Types.equals(thisType, otherType));
    }
  }

  @Test
    @Timeout(8000)
  void equals_sameType_returnsTrue() {
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      Type thisType = getTypeField(typeTokenString);
      Type otherType = getTypeField(typeTokenStringDifferent);
      mocked.when(() -> $Gson$Types.equals(thisType, otherType)).thenReturn(true);
      assertTrue(typeTokenString.equals(typeTokenStringDifferent));
      mocked.verify(() -> $Gson$Types.equals(thisType, otherType));
    }
  }

  @Test
    @Timeout(8000)
  void equals_callsGsonTypesEqualsWithCorrectArguments() {
    TypeToken<String> other = new TypeToken<String>() {};
    TypeToken<String> spyThis = spy(typeTokenString);
    TypeToken<String> spyOther = spy(other);

    Type thisType = getTypeField(spyThis);
    Type otherType = getTypeField(spyOther);

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.equals(thisType, otherType)).thenReturn(true);
      assertTrue(spyThis.equals(spyOther));
      mocked.verify(() -> $Gson$Types.equals(thisType, otherType));
    }
  }

  private Type getTypeField(TypeToken<?> token) {
    try {
      var field = TypeToken.class.getDeclaredField("type");
      field.setAccessible(true);
      return (Type) field.get(token);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}