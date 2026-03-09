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

import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

class GsonTypesTest {

    @Test
    @Timeout(8000)
    void newParameterizedTypeWithOwner_shouldCreateParameterizedTypeWithGivenOwnerRawTypeAndArguments() {
        // Arrange
        Type ownerType = Comparable.class.getDeclaringClass(); // null because Comparable is top-level
        Type rawType = Comparable.class;
        Type[] typeArguments = new Type[] {Integer.class};

        // Act
        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

        // Assert
        assertNotNull(parameterizedType);
        assertEquals(ownerType, parameterizedType.getOwnerType());
        assertEquals(rawType, parameterizedType.getRawType());
        assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    void newParameterizedTypeWithOwner_shouldAllowNullOwnerType() {
        // Arrange
        Type ownerType = null;
        Type rawType = Comparable.class;
        Type[] typeArguments = new Type[] {Integer.class};

        // Act
        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

        // Assert
        assertNotNull(parameterizedType);
        assertNull(parameterizedType.getOwnerType());
        assertEquals(rawType, parameterizedType.getRawType());
        assertArrayEquals(typeArguments, parameterizedType.getActualTypeArguments());
    }

    @Test
    @Timeout(8000)
    void newParameterizedTypeWithOwner_shouldHandleEmptyTypeArguments() {
        // Arrange
        Type ownerType = Comparable.class.getDeclaringClass(); // null because Comparable is top-level
        Type rawType = Comparable.class;
        Type[] typeArguments = new Type[0];

        // Act
        ParameterizedType parameterizedType = $Gson$Types.newParameterizedTypeWithOwner(ownerType, rawType, typeArguments);

        // Assert
        assertNotNull(parameterizedType);
        assertEquals(ownerType, parameterizedType.getOwnerType());
        assertEquals(rawType, parameterizedType.getRawType());
        assertEquals(0, parameterizedType.getActualTypeArguments().length);
    }
}