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

import org.junit.jupiter.api.Test;

import com.google.gson.internal.$Gson$Types;

class GsonTypesResolveTest {

    @Test
    @Timeout(8000)
    void testResolve_withClassToResolve() throws Exception {
        Class<?> contextRawType = String.class;
        Type toResolve = Integer.class;
        Type context = Object.class;

        // invoke private resolve with empty visited map
        Type result = invokeResolve(context, contextRawType, toResolve, new HashMap<>());

        assertEquals(Integer.class, result);
    }

    @Test
    @Timeout(8000)
    void testResolve_withTypeVariableResolved() throws Exception {
        // Prepare a class with a type variable
        class GenericClass<T> {
            TypeVariable<?> getTypeVariable() {
                return GenericClass.class.getTypeParameters()[0];
            }
        }

        TypeVariable<?> typeVariable = new GenericClass<String>().getTypeVariable();
        Class<?> contextRawType = GenericClass.class;
        Type context = newParameterizedTypeWithOwner(null, GenericClass.class, String.class);

        Type result = invokeResolve(context, contextRawType, typeVariable, new HashMap<>());

        assertEquals(String.class, result);
    }

    @Test
    @Timeout(8000)
    void testResolve_withGenericArrayType() throws Exception {
        Type componentType = Integer.class;
        GenericArrayType genericArrayType = $Gson$Types.arrayOf(componentType);

        Type result = invokeResolve(Object.class, Object.class, genericArrayType, new HashMap<>());

        assertTrue(result instanceof GenericArrayType);
        assertEquals(componentType, ((GenericArrayType) result).getGenericComponentType());
    }

    @Test
    @Timeout(8000)
    void testResolve_withParameterizedType() throws Exception {
        Type rawType = Map.class;
        Type[] typeArgs = new Type[] {String.class, Integer.class};
        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(null, (Class<?>) rawType, typeArgs);

        Type result = invokeResolve(Object.class, Object.class, parameterizedType, new HashMap<>());

        assertTrue(result instanceof ParameterizedType);
        ParameterizedType pt = (ParameterizedType) result;
        assertEquals(rawType, pt.getRawType());
        assertArrayEquals(typeArgs, pt.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    void testResolve_withWildcardTypeSubtype() throws Exception {
        Type bound = Number.class;
        WildcardType wildcardType = $Gson$Types.subtypeOf(bound);

        Type result = invokeResolve(Object.class, Object.class, wildcardType, new HashMap<>());

        assertTrue(result instanceof WildcardType);
        WildcardType wt = (WildcardType) result;
        assertArrayEquals(new Type[] {bound}, wt.getUpperBounds());
    }

    @Test
    @Timeout(8000)
    void testResolve_withWildcardTypeSupertype() throws Exception {
        Type bound = Integer.class;
        WildcardType wildcardType = $Gson$Types.supertypeOf(bound);

        Type result = invokeResolve(Object.class, Object.class, wildcardType, new HashMap<>());

        assertTrue(result instanceof WildcardType);
        WildcardType wt = (WildcardType) result;
        assertArrayEquals(new Type[] {bound}, wt.getLowerBounds());
    }

    @Test
    @Timeout(8000)
    void testResolve_withTypeVariableLoop() throws Exception {
        // Create a type variable that maps to itself to test cycle detection
        class Loop<T> {
            TypeVariable<?> getTypeVariable() {
                return Loop.class.getTypeParameters()[0];
            }
        }
        TypeVariable<?> typeVariable = new Loop<>().getTypeVariable();
        Class<?> contextRawType = Loop.class;
        Type context = newParameterizedTypeWithOwner(null, Loop.class, typeVariable);

        Map<TypeVariable<?>, Type> visited = new HashMap<>();
        visited.put(typeVariable, typeVariable);

        Type result = invokeResolve(context, contextRawType, typeVariable, visited);

        // Should return typeVariable itself to prevent infinite recursion
        assertEquals(typeVariable, result);
    }

    private Type invokeResolve(Type context, Class<?> contextRawType, Type toResolve,
            Map<TypeVariable<?>, Type> visited) throws Exception {
        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class,
                Map.class);
        resolveMethod.setAccessible(true);
        return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
    }

    private ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Type rawType, Type... typeArguments)
            throws Exception {
        Method method = $Gson$Types.class.getDeclaredMethod("newParameterizedTypeWithOwner", Type.class, Type.class,
                Type[].class);
        method.setAccessible(true);
        // Wrap typeArguments into Object[] to avoid varargs ambiguity
        return (ParameterizedType) method.invoke(null, ownerType, rawType, (Object) typeArguments);
    }
}