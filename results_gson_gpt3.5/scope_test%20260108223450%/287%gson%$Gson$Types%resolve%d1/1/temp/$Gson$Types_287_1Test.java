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

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

class GsonTypesResolveTest {

    @Test
    @Timeout(8000)
    void testResolve_withNullToResolve_returnsNull() throws Exception {
        Type result = $Gson$Types.resolve(Object.class, Object.class, null);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testResolve_withClassToResolve_returnsClass() throws Exception {
        Type toResolve = String.class;
        Type result = $Gson$Types.resolve(Object.class, Object.class, toResolve);
        assertSame(toResolve, result);
    }

    @Test
    @Timeout(8000)
    void testResolve_withTypeVariable_resolvesCorrectly() throws Exception {
        class GenericClass<T> {}
        TypeVariable<Class<GenericClass>>[] typeParameters = GenericClass.class.getTypeParameters();
        TypeVariable<?> typeVariable = typeParameters[0];

        Type resolved = $Gson$Types.resolve(GenericClass.class, GenericClass.class, typeVariable);
        // Should resolve to itself if no mapping exists
        assertEquals(typeVariable, resolved);
    }

    @Test
    @Timeout(8000)
    void testResolve_withParameterizedType_resolvesArguments() throws Exception {
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] {String.class};
            }

            @Override
            public Type getRawType() {
                return Map.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        Type resolved = $Gson$Types.resolve(Object.class, Object.class, parameterizedType);
        assertTrue(resolved instanceof ParameterizedType);
        ParameterizedType pt = (ParameterizedType) resolved;
        assertEquals(Map.class, pt.getRawType());
        assertArrayEquals(new Type[]{String.class}, pt.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    void testResolve_withGenericArrayType_resolvesComponentType() throws Exception {
        GenericArrayType genericArrayType = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return String.class;
            }
        };

        Type resolved = $Gson$Types.resolve(Object.class, Object.class, genericArrayType);
        assertTrue(resolved instanceof GenericArrayType);
        GenericArrayType gat = (GenericArrayType) resolved;
        assertEquals(String.class, gat.getGenericComponentType());
    }

    @Test
    @Timeout(8000)
    void testResolve_withWildcardType_resolvesBounds() throws Exception {
        WildcardType wildcardType = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[] {Number.class};
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[] {};
            }
        };

        Type resolved = $Gson$Types.resolve(Object.class, Object.class, wildcardType);
        assertTrue(resolved instanceof WildcardType);
        WildcardType wt = (WildcardType) resolved;
        assertArrayEquals(new Type[]{Number.class}, wt.getUpperBounds());
        assertArrayEquals(new Type[]{}, wt.getLowerBounds());
    }

    @Test
    @Timeout(8000)
    void testResolve_privateResolveViaReflection_withVisitedTypeVariables() throws Exception {
        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);

        // Create a dummy generic class to get a TypeVariable
        class Dummy<T> {}
        TypeVariable<?> typeVariable = Dummy.class.getTypeParameters()[0];

        Map<TypeVariable<?>, Type> visited = new HashMap<>();
        visited.put(typeVariable, String.class);

        Type result = (Type) resolveMethod.invoke(null, Dummy.class, Dummy.class, typeVariable, visited);
        assertNotNull(result);
        assertSame(String.class, result);
    }

}