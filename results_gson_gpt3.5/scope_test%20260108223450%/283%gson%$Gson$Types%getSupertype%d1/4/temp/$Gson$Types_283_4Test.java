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
import org.junit.jupiter.api.function.Executable;

class $Gson$Types_283_4Test {

  @Test
    @Timeout(8000)
  void getSupertype_withWildcardType_resolvesUpperBound() throws Exception {
    // Prepare a WildcardType mock with one upper bound
    WildcardType wildcardType = mock(WildcardType.class);
    Type upperBound = String.class;
    when(wildcardType.getUpperBounds()).thenReturn(new Type[] {upperBound});

    // contextRawType assignable to supertype
    Class<?> contextRawType = String.class;
    Class<?> supertype = Object.class;

    // Using reflection to access private static method getSupertype
    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    // Invoke getSupertype with wildcardType context
    Type result = (Type) method.invoke(null, wildcardType, contextRawType, supertype);

    // The result should be the resolution of upperBound, which is Object's supertype
    // Since upperBound is String.class, and supertype is Object.class, result should be Object.class or ParameterizedType
    assertNotNull(result);
    // Because getSupertype calls resolve(...) on the supertype, result should be assignable to supertype
    assertTrue(supertype.isAssignableFrom($Gson$Types.getRawType(result)));
  }

  @Test
    @Timeout(8000)
  void getSupertype_withNormalType_returnsResolvedSupertype() throws Exception {
    Class<?> contextRawType = Integer.class;
    Class<?> supertype = Number.class;

    // Integer is assignable to Number
    assertTrue(supertype.isAssignableFrom(contextRawType));

    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Type result = (Type) method.invoke(null, contextRawType, contextRawType, supertype);

    assertNotNull(result);
    assertTrue(supertype.isAssignableFrom($Gson$Types.getRawType(result)));
  }

  @Test
    @Timeout(8000)
  void getSupertype_whenSupertypeNotAssignable_throwsIllegalArgumentException() throws Exception {
    Class<?> contextRawType = String.class;
    Class<?> supertype = Number.class;

    // String is not assignable to Number, so checkArgument should fail

    Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
    method.setAccessible(true);

    Executable executable = () -> {
      method.invoke(null, contextRawType, contextRawType, supertype);
    };

    Exception exception = assertThrows(InvocationTargetException.class, executable);
    // The cause should be IllegalArgumentException from checkArgument
    assertTrue(exception.getCause() instanceof IllegalArgumentException);
  }
}