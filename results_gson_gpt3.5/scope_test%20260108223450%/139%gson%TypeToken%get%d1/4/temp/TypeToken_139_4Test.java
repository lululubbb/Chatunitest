package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

public class TypeToken_139_4Test {

  @Test
    @Timeout(8000)
  public void testGet_withClass() {
    // Arrange
    Class<String> clazz = String.class;

    // Act
    TypeToken<String> typeToken = TypeToken.get(clazz);

    // Assert
    assertNotNull(typeToken);
    assertEquals(String.class, typeToken.getRawType());
    assertEquals(String.class, typeToken.getType());
    assertEquals(typeToken.hashCode(), typeToken.hashCode());
    assertTrue(typeToken.equals(typeToken));
    assertFalse(typeToken.equals(null));
    assertFalse(typeToken.equals("some string"));
    assertEquals("java.lang.String", typeToken.toString());
  }

  @Test
    @Timeout(8000)
  public void testGet_withPrimitiveClass() {
    // Arrange
    Class<Integer> clazz = int.class;

    // Act
    TypeToken<Integer> typeToken = TypeToken.get(clazz);

    // Assert
    assertNotNull(typeToken);
    assertEquals(int.class, typeToken.getRawType());
    assertEquals(int.class, typeToken.getType());
  }

  @Test
    @Timeout(8000)
  public void testGet_withVoidClass() {
    // Arrange
    Class<Void> clazz = void.class;

    // Act
    TypeToken<Void> typeToken = TypeToken.get(clazz);

    // Assert
    assertNotNull(typeToken);
    assertEquals(void.class, typeToken.getRawType());
    assertEquals(void.class, typeToken.getType());
  }
}