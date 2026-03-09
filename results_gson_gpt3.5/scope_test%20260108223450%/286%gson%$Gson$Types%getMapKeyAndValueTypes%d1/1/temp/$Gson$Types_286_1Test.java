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
import java.util.NoSuchElementException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.Test;

class $Gson$Types_286_1Test {

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withPropertiesClass() {
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(Properties.class, Properties.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(String.class, types[0]);
    assertEquals(String.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withParameterizedMap() throws Exception {
    // Prepare a ParameterizedType mock for Map<String, Integer>
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    Type[] actualTypeArguments = new Type[] { String.class, Integer.class };
    when(parameterizedType.getActualTypeArguments()).thenReturn(actualTypeArguments);

    // Use reflection to replace the private static getSupertype method call inside getMapKeyAndValueTypes
    // by temporarily using a spy on $Gson$Types class and reflection to inject the mock.

    // Since getSupertype is private static, we cannot mock it directly with Mockito.
    // Instead, we create a subclass with a public static method that calls getMapKeyAndValueTypes
    // but overrides getSupertype via reflection.

    // Alternatively, use reflection to invoke getMapKeyAndValueTypes but before that,
    // temporarily replace the getSupertype method via reflection.

    // Here, we'll use reflection to call getMapKeyAndValueTypes but inject the mocked getSupertype return.

    // So we create a dynamic proxy for $Gson$Types.getSupertype method by using a custom InvocationHandler
    // is not possible because it's static and private.

    // Instead, we use reflection to get the Method object for getSupertype and make it accessible,
    // then replace its implementation by a proxy is not possible in Java without bytecode manipulation.

    // So the best approach: use reflection to call getMapKeyAndValueTypes but override getSupertype by
    // calling a custom method that returns our mocked ParameterizedType.

    // Since this is complicated, here is a workaround:
    // Create a subclass with a public static method that calls getMapKeyAndValueTypes but intercepts getSupertype calls.
    // But since $Gson$Types is final and has only static methods, we cannot subclass.

    // So, as a practical approach, use reflection to call getMapKeyAndValueTypes,
    // but before that, temporarily swap the getSupertype method via method handle or use a library like PowerMock.
    // Since only Mockito 3 is allowed, we cannot do that.

    // Therefore, we will simulate the test by invoking getMapKeyAndValueTypes with a ParameterizedType context.

    // So we create an anonymous Type that returns the parameterizedType when getSupertype is called.

    // Instead, we directly test getMapKeyAndValueTypes by passing the parameterizedType as the context.

    Type[] types = $Gson$Types.getMapKeyAndValueTypes(parameterizedType, StringIntegerMap.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(String.class, types[0]);
    assertEquals(Integer.class, types[1]);
  }

  @Test
    @Timeout(8000)
  void testGetMapKeyAndValueTypes_withRawMap() throws Exception {
    // Pass raw Map.class as the supertype to simulate raw map type
    Type[] types = $Gson$Types.getMapKeyAndValueTypes(RawMap.class, RawMap.class);
    assertNotNull(types);
    assertEquals(2, types.length);
    assertEquals(Object.class, types[0]);
    assertEquals(Object.class, types[1]);
  }

  // Helper interface for parameterized Map<String, Integer>
  interface StringIntegerMap extends Map<String, Integer> {}

  // Helper interface for raw Map type
  interface RawMap extends Map {}

}