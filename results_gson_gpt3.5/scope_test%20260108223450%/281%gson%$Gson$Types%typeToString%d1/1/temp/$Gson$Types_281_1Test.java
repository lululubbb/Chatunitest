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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Type;

class $Gson$Types_281_1Test {

  @Test
    @Timeout(8000)
  void typeToString_withClassType_returnsClassName() {
    Type type = String.class;
    String result = $Gson$Types.typeToString(type);
    assertEquals("java.lang.String", result);
  }

  @Test
    @Timeout(8000)
  void typeToString_withNonClassType_returnsToString() {
    Type type = new Type() {
      @Override
      public String toString() {
        return "customTypeString";
      }
    };
    String result = $Gson$Types.typeToString(type);
    assertEquals("customTypeString", result);
  }
}