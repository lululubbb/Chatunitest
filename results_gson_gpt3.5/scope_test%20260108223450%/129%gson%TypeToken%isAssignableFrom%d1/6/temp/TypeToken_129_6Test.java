package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TypeTokenIsAssignableFromTest {

  private TypeToken<String> stringTypeToken;
  private TypeToken<Integer> integerTypeToken;

  @BeforeEach
  public void setUp() {
    stringTypeToken = TypeToken.get(String.class);
    integerTypeToken = TypeToken.get(Integer.class);
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withSameTypeToken() {
    TypeToken<String> anotherStringTypeToken = TypeToken.get(String.class);
    assertTrue(stringTypeToken.isAssignableFrom(anotherStringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withSubtype() {
    TypeToken<Object> objectTypeToken = TypeToken.get(Object.class);
    assertTrue(objectTypeToken.isAssignableFrom(stringTypeToken));
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withUnrelatedType() {
    assertFalse(stringTypeToken.isAssignableFrom(integerTypeToken));
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withNullToken() {
    assertFalse(stringTypeToken.isAssignableFrom((TypeToken<?>) null));
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withMockedTypeToken() {
    TypeToken<?> mockToken = Mockito.mock(TypeToken.class);
    Mockito.when(mockToken.getType()).thenReturn(String.class);
    assertTrue(stringTypeToken.isAssignableFrom(mockToken));
  }

  @Test
    @Timeout(8000)
  public void testIsAssignableFrom_withAnonymousSubclass() {
    TypeToken<String> anonymousSubclass = new TypeToken<String>() {};
    assertTrue(stringTypeToken.isAssignableFrom(anonymousSubclass));
  }
}