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
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class $Gson$Types_283_6Test {

  private Class<?> contextRawType;
  private Class<?> supertype;

  @BeforeEach
  void setup() {
    contextRawType = java.util.ArrayList.class;
    supertype = java.util.Collection.class;
  }

  @Test
    @Timeout(8000)
  void getSupertype_withWildcardType_resolvesToUpperBound() throws Exception {
    // Create a mock WildcardType with one upper bound: ArrayList.class
    WildcardType wildcardType = mock(WildcardType.class);
    when(wildcardType.getUpperBounds()).thenReturn(new Type[] { contextRawType });

    // Use reflection to access private static getSupertype method
    var getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    // Call getSupertype with wildcardType as context
    Object result = getSupertypeMethod.invoke(null, wildcardType, contextRawType, supertype);

    assertNotNull(result);
    // The returned type should be the resolved generic supertype of the upper bound
  }

  @Test
    @Timeout(8000)
  void getSupertype_withAssignableContextRawType_returnsResolvedType() throws Exception {
    // Use ArrayList.class as contextRawType and Collection.class as supertype (Collection is assignable from ArrayList)
    Type context = contextRawType;

    var getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    Object result = getSupertypeMethod.invoke(null, context, contextRawType, supertype);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void getSupertype_withNonAssignable_throwsIllegalArgumentException() throws Exception {
    var getSupertypeMethod = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    getSupertypeMethod.setAccessible(true);

    Class<?> nonAssignableSupertype = String.class;

    // Since invoke wraps exceptions in InvocationTargetException, we unwrap it and check cause
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      getSupertypeMethod.invoke(null, contextRawType, contextRawType, nonAssignableSupertype);
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
  }

}