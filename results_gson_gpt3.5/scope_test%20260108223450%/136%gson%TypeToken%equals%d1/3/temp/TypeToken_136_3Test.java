package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TypeTokenEqualsTest {

  private TypeToken<?> typeTokenMock1;
  private TypeToken<?> typeTokenMock2;
  private Type typeMock1;
  private Type typeMock2;
  private MockedStatic<$Gson$Types> mockedStatic;

  @BeforeEach
  void setUp() {
    typeMock1 = mock(Type.class);
    typeMock2 = mock(Type.class);

    typeTokenMock1 = createTypeTokenWithType(typeMock1);
    typeTokenMock2 = createTypeTokenWithType(typeMock2);
  }

  @AfterEach
  void tearDown() {
    clearStaticGsonTypesEquals();
  }

  @Test
    @Timeout(8000)
  void equals_SameInstance_ReturnsTrue() {
    assertTrue(typeTokenMock1.equals(typeTokenMock1));
  }

  @Test
    @Timeout(8000)
  void equals_Null_ReturnsFalse() {
    assertFalse(typeTokenMock1.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_DifferentClass_ReturnsFalse() {
    assertFalse(typeTokenMock1.equals(new Object()));
  }

  @Test
    @Timeout(8000)
  void equals_TypeEqualsReturnsTrue_ReturnsTrue() {
    mockStaticGsonTypesEquals(true);

    TypeToken<?> other = createTypeTokenWithType(typeMock2);

    assertTrue(typeTokenMock1.equals(other));
  }

  @Test
    @Timeout(8000)
  void equals_TypeEqualsReturnsFalse_ReturnsFalse() {
    mockStaticGsonTypesEquals(false);

    TypeToken<?> other = createTypeTokenWithType(typeMock2);

    assertFalse(typeTokenMock1.equals(other));
  }

  private void mockStaticGsonTypesEquals(boolean returnValue) {
    clearStaticGsonTypesEquals();
    mockedStatic = mockStatic($Gson$Types.class);
    mockedStatic.when(() -> $Gson$Types.equals(any(Type.class), any(Type.class))).thenReturn(returnValue);
  }

  private void clearStaticGsonTypesEquals() {
    if (mockedStatic != null) {
      mockedStatic.close();
      mockedStatic = null;
    }
  }

  private TypeToken<?> createTypeTokenWithType(Type type) {
    return new TypeToken<Object>() {
      {
        setField(this, "type", type);
        setField(this, "rawType", Object.class);
        setField(this, "hashCode", 0);
      }
    };
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = TypeToken.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}