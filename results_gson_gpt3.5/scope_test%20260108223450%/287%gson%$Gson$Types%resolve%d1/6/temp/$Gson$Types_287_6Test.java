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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonTypesResolveTest {

    private Class<?> contextRawType;
    private Type context;

    @BeforeEach
    public void setup() {
        contextRawType = SampleClass.class;
        context = SampleClass.class;
    }

    @Test
    @Timeout(8000)
    public void testResolve_withClassType_returnsSameClass() throws Exception {
        Type toResolve = String.class;

        Type result = $Gson$Types.resolve(context, contextRawType, toResolve);
        assertSame(String.class, result);
    }

    @Test
    @Timeout(8000)
    public void testResolve_withTypeVariable_resolvesCorrectly() throws Exception {
        // Access private resolve method with 4 params via reflection to test recursion and map use
        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        TypeVariable<Class<GenericClass>>[] typeParams = GenericClass.class.getTypeParameters();
        TypeVariable<?> typeVariable = typeParams[0];

        Map<TypeVariable<?>, Type> visited = new HashMap<>();
        // Correct the order of parameters: context, contextRawType, toResolve (TypeVariable), visited map
        Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, typeVariable, visited);
        // It should resolve to Object since no mapping provided
        assertEquals(Object.class, resolved);
    }

    @Test
    @Timeout(8000)
    public void testResolve_withParameterizedType_resolvesArguments() throws Exception {
        ParameterizedType parameterizedType = (ParameterizedType) SampleClass.class.getDeclaredField("listField").getGenericType();

        Type resolved = $Gson$Types.resolve(context, contextRawType, parameterizedType);
        assertTrue(resolved instanceof ParameterizedType);
        ParameterizedType pt = (ParameterizedType) resolved;
        assertEquals(parameterizedType.getRawType(), pt.getRawType());
        assertArrayEquals(parameterizedType.getActualTypeArguments(), pt.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    public void testResolve_withGenericArrayType_resolvesComponent() throws Exception {
        // Create GenericArrayType mock
        GenericArrayType genericArrayType = mock(GenericArrayType.class);
        when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

        Type resolved = $Gson$Types.resolve(context, contextRawType, genericArrayType);
        assertTrue(resolved instanceof GenericArrayType || resolved instanceof Class);
        if (resolved instanceof GenericArrayType) {
            GenericArrayType gat = (GenericArrayType) resolved;
            assertEquals(String.class, gat.getGenericComponentType());
        } else if (resolved instanceof Class) {
            assertEquals(String[].class, resolved);
        }
    }

    @Test
    @Timeout(8000)
    public void testResolve_withWildcardType_subtypeOf() throws Exception {
        // Create WildcardType mock with upper bound Object
        WildcardType wildcardType = mock(WildcardType.class);
        when(wildcardType.getUpperBounds()).thenReturn(new Type[] {Object.class});
        when(wildcardType.getLowerBounds()).thenReturn(new Type[] {});

        Type resolved = $Gson$Types.resolve(context, contextRawType, wildcardType);
        assertTrue(resolved instanceof WildcardType);
    }

    @Test
    @Timeout(8000)
    public void testResolve_withWildcardType_supertypeOf() throws Exception {
        // Create WildcardType mock with lower bound String
        WildcardType wildcardType = mock(WildcardType.class);
        when(wildcardType.getUpperBounds()).thenReturn(new Type[] {Object.class});
        when(wildcardType.getLowerBounds()).thenReturn(new Type[] {String.class});

        Type resolved = $Gson$Types.resolve(context, contextRawType, wildcardType);
        assertTrue(resolved instanceof WildcardType);
    }

    @Test
    @Timeout(8000)
    public void testResolve_withTypeVariableInVisitedMap_returnsMappedType() throws Exception {
        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        TypeVariable<Class<GenericClass>>[] typeParams = GenericClass.class.getTypeParameters();
        TypeVariable<?> typeVariable = typeParams[0];

        Map<TypeVariable<?>, Type> visited = new HashMap<>();
        visited.put(typeVariable, String.class);

        // Correct the order of parameters: context, contextRawType, toResolve (TypeVariable), visited map
        Type resolved = (Type) resolveMethod.invoke(null, context, contextRawType, typeVariable, visited);
        assertEquals(String.class, resolved);
    }

    @Test
    @Timeout(8000)
    public void testResolve_withNullToResolve_returnsNull() throws Exception {
        Type result = $Gson$Types.resolve(context, contextRawType, null);
        assertNull(result);
    }

    // Sample classes for testing generics
    static class SampleClass {
        java.util.List<String> listField;
    }

    static class GenericClass<T> {
    }
}