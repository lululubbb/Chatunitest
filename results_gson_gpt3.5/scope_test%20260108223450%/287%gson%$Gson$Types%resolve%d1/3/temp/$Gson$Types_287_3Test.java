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
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class GsonTypesResolveTest {

    // Helper to invoke private static resolve(Type, Class<?>, Type, Map<TypeVariable<?>, Type>)
    private Type invokePrivateResolve(Type context, Class<?> contextRawType, Type toResolve, Map<TypeVariable<?>, Type> visited) throws Exception {
        Method resolveMethod = $Gson$Types.class.getDeclaredMethod("resolve", Type.class, Class.class, Type.class, Map.class);
        resolveMethod.setAccessible(true);
        return (Type) resolveMethod.invoke(null, context, contextRawType, toResolve, visited);
    }

    @Test
    @Timeout(8000)
    void testResolve_withNullToResolve_returnsNull() {
        Type result = $Gson$Types.resolve(Object.class, Object.class, null);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testResolve_withClassToResolve_returnsCanonicalized() {
        Type toResolve = String.class;
        Type result = $Gson$Types.resolve(Object.class, Object.class, toResolve);
        assertEquals(String.class, result);
    }

    @Test
    @Timeout(8000)
    void testResolve_withTypeVariable_resolvesCorrectly() {
        class GenericClass<T> {}
        TypeVariable<?>[] typeVars = GenericClass.class.getTypeParameters();
        TypeVariable<?> tVar = typeVars[0];

        // context is GenericClass<String>
        ParameterizedType context = $Gson$Types.newParameterizedTypeWithOwner(null, GenericClass.class, String.class);
        Type resolved = $Gson$Types.resolve(context, GenericClass.class, tVar);
        assertEquals(String.class, resolved);
    }

    @Test
    @Timeout(8000)
    void testResolve_withParameterizedType_resolvesArguments() {
        class GenericClass<T> {}
        ParameterizedType context = $Gson$Types.newParameterizedTypeWithOwner(null, GenericClass.class, String.class);
        ParameterizedType toResolve = $Gson$Types.newParameterizedTypeWithOwner(null, GenericClass.class, GenericClass.class.getTypeParameters()[0]);

        Type resolved = $Gson$Types.resolve(context, GenericClass.class, toResolve);
        assertTrue(resolved instanceof ParameterizedType);
        ParameterizedType pt = (ParameterizedType) resolved;
        assertEquals(String.class, pt.getActualTypeArguments()[0]);
    }

    @Test
    @Timeout(8000)
    void testResolve_withGenericArrayType_resolvesComponent() {
        GenericArrayType genericArrayType = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return Integer.class;
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof GenericArrayType)) return false;
                GenericArrayType that = (GenericArrayType) obj;
                return $Gson$Types.equals(getGenericComponentType(), that.getGenericComponentType());
            }

            @Override
            public int hashCode() {
                return getGenericComponentType().hashCode();
            }

            @Override
            public String toString() {
                return $Gson$Types.typeToString(this);
            }
        };
        Type resolved = $Gson$Types.resolve(Object.class, Object.class, genericArrayType);
        assertTrue(resolved instanceof GenericArrayType);
        GenericArrayType gat = (GenericArrayType) resolved;
        assertEquals(Integer.class, gat.getGenericComponentType());
    }

    @Test
    @Timeout(8000)
    void testResolve_withWildcardType_resolvesBounds() {
        WildcardType wildcard = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[]{Number.class};
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }

            @Override
            public boolean equals(Object obj) {
                if (!(obj instanceof WildcardType)) return false;
                WildcardType that = (WildcardType) obj;
                return Arrays.equals(getUpperBounds(), that.getUpperBounds()) &&
                       Arrays.equals(getLowerBounds(), that.getLowerBounds());
            }

            @Override
            public int hashCode() {
                return Arrays.hashCode(getUpperBounds()) ^ Arrays.hashCode(getLowerBounds());
            }

            @Override
            public String toString() {
                return $Gson$Types.typeToString(this);
            }
        };
        Type resolved = $Gson$Types.resolve(Object.class, Object.class, wildcard);
        assertTrue(resolved instanceof WildcardType);
        WildcardType wt = (WildcardType) resolved;
        assertArrayEquals(new Type[]{Number.class}, wt.getUpperBounds());
        assertEquals(0, wt.getLowerBounds().length);
    }

    @Test
    @Timeout(8000)
    void testResolve_withRecursiveTypeVariable_avoidsInfiniteLoop() {
        class Recursive<T extends Comparable<T>> {}
        TypeVariable<?> tVar = Recursive.class.getTypeParameters()[0];
        ParameterizedType context = $Gson$Types.newParameterizedTypeWithOwner(null, Recursive.class, Comparable.class);
        Type resolved = $Gson$Types.resolve(context, Recursive.class, tVar);
        assertNotNull(resolved);
    }

    @Test
    @Timeout(8000)
    void testResolve_withMapType_resolvesKeyAndValue() throws NoSuchFieldException {
        class MapHolder<K, V> {
            Map<K, V> map;
        }
        ParameterizedType context = $Gson$Types.newParameterizedTypeWithOwner(null, MapHolder.class, String.class, Integer.class);
        Type mapFieldType = MapHolder.class.getDeclaredField("map").getGenericType();
        Type resolved = $Gson$Types.resolve(context, MapHolder.class, mapFieldType);
        assertTrue(resolved instanceof ParameterizedType);
        ParameterizedType pt = (ParameterizedType) resolved;
        assertEquals(String.class, pt.getActualTypeArguments()[0]);
        assertEquals(Integer.class, pt.getActualTypeArguments()[1]);
    }

    @Test
    @Timeout(8000)
    void testResolve_withArrayType_resolvesComponentType() {
        Type toResolve = String[].class;
        Type resolved = $Gson$Types.resolve(Object.class, Object.class, toResolve);
        assertEquals(String[].class, resolved);
    }
}