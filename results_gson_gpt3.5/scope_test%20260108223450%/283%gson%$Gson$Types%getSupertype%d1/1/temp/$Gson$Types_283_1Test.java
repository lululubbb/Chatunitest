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
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

class GsonTypesGetSupertypeTest {

    @Test
    @Timeout(8000)
    void testGetSupertypeWithWildcardType() throws Exception {
        // Prepare a WildcardType mock with upper bound List.class
        WildcardType wildcardType = mock(WildcardType.class);
        Type[] upperBounds = new Type[] { List.class };
        when(wildcardType.getUpperBounds()).thenReturn(upperBounds);
        when(wildcardType.getLowerBounds()).thenReturn(new Type[0]);

        // contextRawType is List.class
        Class<?> contextRawType = List.class;

        // supertype is Collection.class, which is assignable from List.class
        Class<?> supertype = Collection.class;

        // Use reflection to access private static method getSupertype
        Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
        method.setAccessible(true);

        Object result = method.invoke(null, wildcardType, contextRawType, supertype);

        // Result should be a Type representing Collection as supertype of List
        assertNotNull(result);
        // The result should be assignable to Collection.class raw type
        Class<?> rawType = $Gson$Types.getRawType((Type) result);
        assertEquals(supertype, rawType);
    }

    @Test
    @Timeout(8000)
    void testGetSupertypeWithClassContext() throws Exception {
        // context: ArrayList.class
        Class<?> context = java.util.ArrayList.class;

        // supertype: List.class (ArrayList implements List)
        Class<?> supertype = List.class;

        // Use reflection to access private static method getSupertype
        Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
        method.setAccessible(true);

        Object result = method.invoke(null, context, context, supertype);

        assertNotNull(result);
        Class<?> rawType = $Gson$Types.getRawType((Type) result);
        assertEquals(supertype, rawType);
    }

    @Test
    @Timeout(8000)
    void testGetSupertypeWithExactSameType() throws Exception {
        // context: String.class
        Class<?> context = String.class;

        // supertype: Object.class (String extends Object)
        Class<?> supertype = Object.class;

        Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
        method.setAccessible(true);

        Object result = method.invoke(null, context, context, supertype);

        assertNotNull(result);
        Class<?> rawType = $Gson$Types.getRawType((Type) result);
        assertEquals(supertype, rawType);
    }

    @Test
    @Timeout(8000)
    void testGetSupertypeThrowsIfNotAssignable() throws Exception {
        // context: String.class
        Class<?> context = String.class;

        // supertype: Integer.class (not assignable from String.class)
        Class<?> supertype = Integer.class;

        Method method = $Gson$Types.class.getDeclaredMethod("getSupertype", Type.class, Class.class, Class.class);
        method.setAccessible(true);

        // Should throw IllegalArgumentException due to checkArgument failure
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(null, context, context, supertype);
        });
        // InvocationTargetException wraps the actual exception
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
    }
}