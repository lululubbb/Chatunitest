package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import org.junit.jupiter.api.Test;
import com.google.gson.internal.$Gson$Types;

class $Gson$Types_278_6Test {

  @Test
    @Timeout(8000)
  void testGetRawType_withClass() {
    Class<String> clazz = String.class;
    Class<?> result = $Gson$Types.getRawType(clazz);
    assertSame(clazz, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withParameterizedType() throws Exception {
    // Create a ParameterizedType for Map<String, Integer>
    ParameterizedType parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      public String toString() {
        return "java.util.Map<java.lang.String, java.lang.Integer>";
      }
    };
    Class<?> result = $Gson$Types.getRawType(parameterizedType);
    assertSame(Map.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withGenericArrayType() throws Exception {
    // Create a GenericArrayType for List<String>[]
    GenericArrayType genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return new ParameterizedType() {
          @Override
          public Type[] getActualTypeArguments() {
            return new Type[] {String.class};
          }
          @Override
          public Type getRawType() {
            return java.util.List.class;
          }
          @Override
          public Type getOwnerType() {
            return null;
          }
        };
      }
    };
    Class<?> result = $Gson$Types.getRawType(genericArrayType);
    assertTrue(result.isArray());
    assertEquals(java.util.List.class, result.getComponentType());
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withTypeVariable() throws Exception {
    // Create a TypeVariable mock
    TypeVariable<?> typeVariable = mock(TypeVariable.class);
    Class<?> result = $Gson$Types.getRawType(typeVariable);
    assertSame(Object.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withWildcardType() throws Exception {
    WildcardType wildcardType = new WildcardType() {
      @Override
      public Type[] getUpperBounds() {
        return new Type[] {Number.class};
      }
      @Override
      public Type[] getLowerBounds() {
        return new Type[0];
      }
    };
    Class<?> result = $Gson$Types.getRawType(wildcardType);
    assertSame(Number.class, result);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withNullType_throws() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(null);
    });
    assertTrue(exception.getMessage().contains("null"));
  }

  @Test
    @Timeout(8000)
  void testGetRawType_withUnknownType_throws() {
    Type unknownType = new Type() {};
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Types.getRawType(unknownType);
    });
    assertTrue(exception.getMessage().contains(unknownType.getClass().getName()));
  }
}