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
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class $Gson$Types_281_4Test {

  @Test
    @Timeout(8000)
  void testTypeToString_withClassType() {
    Type type = String.class;
    String result = $Gson$Types.typeToString(type);
    assertEquals("java.lang.String", result);
  }

  @Test
    @Timeout(8000)
  void testTypeToString_withNonClassType() {
    Type type = new Type() {
      @Override
      public String toString() {
        return "customTypeToString";
      }
    };
    String result = $Gson$Types.typeToString(type);
    assertEquals("customTypeToString", result);
  }
}