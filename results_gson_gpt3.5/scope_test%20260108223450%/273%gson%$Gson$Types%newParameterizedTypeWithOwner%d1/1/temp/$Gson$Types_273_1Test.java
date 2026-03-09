package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.internal.$Gson$Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Modifier;
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

public class $Gson$Types_273_1Test {

    @Test
    @Timeout(8000)
    public void testNewParameterizedTypeWithOwner_withOwnerType() {
        Type ownerType = String.class;
        Type rawType = java.util.Map.class;
        Type[] typeArguments = new Type[] {Integer.class, String.class};

        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

        assertNotNull(parameterizedType);
        assertEquals(ownerType, parameterizedType.getOwnerType());
        assertEquals(rawType, parameterizedType.getRawType());
        assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    public void testNewParameterizedTypeWithOwner_withNullOwnerType() {
        Type ownerType = null;
        Type rawType = java.util.List.class;
        Type[] typeArguments = new Type[] {String.class};

        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

        assertNotNull(parameterizedType);
        assertNull(parameterizedType.getOwnerType());
        assertEquals(rawType, parameterizedType.getRawType());
        assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    public void testNewParameterizedTypeWithOwner_withEmptyTypeArguments() {
        Type ownerType = String.class;
        Type rawType = java.util.Map.class;
        Type[] typeArguments = new Type[0];

        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

        assertNotNull(parameterizedType);
        assertEquals(ownerType, parameterizedType.getOwnerType());
        assertEquals(rawType, parameterizedType.getRawType());
        assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    }
}