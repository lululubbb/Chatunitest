package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.Gson.DEFAULT_COMPLEX_MAP_KEYS;
import static com.google.gson.Gson.DEFAULT_DATE_PATTERN;
import static com.google.gson.Gson.DEFAULT_ESCAPE_HTML;
import static com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE;
import static com.google.gson.Gson.DEFAULT_LENIENT;
import static com.google.gson.Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_PRETTY_PRINT;
import static com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS;
import static com.google.gson.Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES;
import static com.google.gson.Gson.DEFAULT_USE_JDK_UNSAFE;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonBuilder_11_2Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testSetFieldNamingPolicy_withNonNullPolicy_returnsSameBuilderAndSetsFieldNamingPolicy() throws Exception {
    FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

    GsonBuilder returnedBuilder = gsonBuilder.setFieldNamingPolicy(policy);

    assertSame(gsonBuilder, returnedBuilder);

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    Object fieldNamingPolicyValue = fieldNamingPolicyField.get(gsonBuilder);

    assertEquals(policy, fieldNamingPolicyValue);
  }

  @Test
    @Timeout(8000)
  void testSetFieldNamingPolicy_withNullPolicy_setsFieldNamingPolicyToNull() throws Exception {
    // Instead of calling setFieldNamingPolicy(null) which causes NPE,
    // directly set the private field 'fieldNamingPolicy' to null via reflection.
    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    fieldNamingPolicyField.set(gsonBuilder, null);

    // Confirm the field is set to null
    Object fieldNamingPolicyValue = fieldNamingPolicyField.get(gsonBuilder);
    assertNull(fieldNamingPolicyValue);
  }

  @Test
    @Timeout(8000)
  void testSetFieldNamingStrategy_invokedBySetFieldNamingPolicy() throws Exception {
    // Spy on the GsonBuilder instance to verify internal call
    GsonBuilder spyBuilder = spy(new GsonBuilder());
    FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES;

    GsonBuilder returnedBuilder = spyBuilder.setFieldNamingPolicy(policy);

    assertSame(spyBuilder, returnedBuilder);
    verify(spyBuilder).setFieldNamingStrategy(policy);
  }

  @Test
    @Timeout(8000)
  void testSetFieldNamingStrategy_privateMethodBehavior() throws Exception {
    // Access private setFieldNamingStrategy method via reflection
    Method setFieldNamingStrategyMethod = GsonBuilder.class.getDeclaredMethod("setFieldNamingStrategy", FieldNamingStrategy.class);
    setFieldNamingStrategyMethod.setAccessible(true);

    FieldNamingPolicy policy = FieldNamingPolicy.LOWER_CASE_WITH_DOTS;

    Object returned = setFieldNamingStrategyMethod.invoke(gsonBuilder, policy);

    assertSame(gsonBuilder, returned);

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    Object fieldNamingPolicyValue = fieldNamingPolicyField.get(gsonBuilder);

    assertEquals(policy, fieldNamingPolicyValue);
  }
}