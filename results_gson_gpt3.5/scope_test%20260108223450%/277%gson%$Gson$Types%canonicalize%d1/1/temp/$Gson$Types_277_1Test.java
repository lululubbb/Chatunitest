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
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.*;

import java.util.Map;

class GsonTypesTest {

    @Test
    @Timeout(8000)
    public void testCanonicalize_withClass_nonArray() {
        Class<String> type = String.class;
        Type result = $Gson$Types.canonicalize(type);
        assertSame(type, result);
    }

    @Test
    @Timeout(8000)
    public void testCanonicalize_withClass_array() {
        Class<int[]> type = int[].class;
        Type result = $Gson$Types.canonicalize(type);
        assertTrue(result instanceof GenericArrayType);
        GenericArrayType gat = (GenericArrayType) result;
        assertEquals(int.class, gat.getGenericComponentType());
    }

    @Test
    @Timeout(8000)
    public void testCanonicalize_withParameterizedType() throws Exception {
        ParameterizedType pt = mock(ParameterizedType.class);
        Type ownerType = String.class;
        Type rawType = Map.class;
        Type[] typeArgs = new Type[] {String.class, Integer.class};
        when(pt.getOwnerType()).thenReturn(ownerType);
        when(pt.getRawType()).thenReturn(rawType);
        when(pt.getActualTypeArguments()).thenReturn(typeArgs);

        Type result = $Gson$Types.canonicalize(pt);
        assertNotNull(result);
        assertTrue(result.getClass().getName().endsWith("$ParameterizedTypeImpl"));
        ParameterizedType newPt = (ParameterizedType) result;
        assertEquals(ownerType, newPt.getOwnerType());
        assertEquals(rawType, newPt.getRawType());
        assertArrayEquals(typeArgs, newPt.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    public void testCanonicalize_withGenericArrayType() throws Exception {
        GenericArrayType gat = mock(GenericArrayType.class);
        Type componentType = String.class;
        when(gat.getGenericComponentType()).thenReturn(componentType);

        Type result = $Gson$Types.canonicalize(gat);
        assertNotNull(result);
        assertTrue(result.getClass().getName().endsWith("$GenericArrayTypeImpl"));
        GenericArrayType newGat = (GenericArrayType) result;
        assertEquals(componentType, newGat.getGenericComponentType());
    }

    @Test
    @Timeout(8000)
    public void testCanonicalize_withWildcardType() throws Exception {
        WildcardType wt = mock(WildcardType.class);
        Type[] upperBounds = new Type[] {Number.class};
        Type[] lowerBounds = new Type[] {};
        when(wt.getUpperBounds()).thenReturn(upperBounds);
        when(wt.getLowerBounds()).thenReturn(lowerBounds);

        Type result = $Gson$Types.canonicalize(wt);
        assertNotNull(result);
        assertTrue(result.getClass().getName().endsWith("$WildcardTypeImpl"));
        WildcardType newWt = (WildcardType) result;
        assertArrayEquals(upperBounds, newWt.getUpperBounds());
        assertArrayEquals(lowerBounds, newWt.getLowerBounds());
    }

    @Test
    @Timeout(8000)
    public void testCanonicalize_withOtherType() {
        Type anonType = new Type() {};
        Type result = $Gson$Types.canonicalize(anonType);
        assertSame(anonType, result);
    }
}