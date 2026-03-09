package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.ReflectionAccessFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ReflectiveTypeAdapterFactory_73_6Test {

  private ConstructorConstructor constructorConstructor;
  private FieldNamingStrategy fieldNamingStrategy;
  private Excluder excluder;
  private JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
  private List<ReflectionAccessFilter> reflectionFilters;

  private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

  @BeforeEach
  void setUp() {
    constructorConstructor = mock(ConstructorConstructor.class);
    fieldNamingStrategy = mock(FieldNamingStrategy.class);
    excluder = mock(Excluder.class);
    jsonAdapterFactory = mock(JsonAdapterAnnotationTypeAdapterFactory.class);
    reflectionFilters = Collections.emptyList();

    reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
        constructorConstructor,
        fieldNamingStrategy,
        excluder,
        jsonAdapterFactory,
        reflectionFilters
    );
  }

  static class DummyClass {
    public int publicField = 1;
    private String privateField = "private";
  }

  @Test
    @Timeout(8000)
  void testCreate_withRegularClass_returnsTypeAdapter() {
    Gson gson = mock(Gson.class);
    TypeToken<DummyClass> typeToken = TypeToken.get(DummyClass.class);

    // Mock ConstructorConstructor.newConstructor to return an ObjectConstructor
    when(constructorConstructor.get(ArgumentMatchers.any(TypeToken.class)))
        .thenReturn(() -> new DummyClass());

    // Mock FieldNamingStrategy to return field names as is
    when(fieldNamingStrategy.translateName(any(Field.class))).thenAnswer(invocation -> {
      Field f = invocation.getArgument(0);
      return f.getName();
    });

    // Mock Excluder to not exclude any fields
    when(excluder.excludeClass(any(Class.class), anyBoolean())).thenReturn(false);
    when(excluder.excludeField(any(Field.class), anyBoolean())).thenReturn(false);

    // Mock JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter to return null (no custom adapter)
    // The actual method signature is getTypeAdapter(ConstructorConstructor, Gson, TypeToken<?>, JsonAdapter)
    // So we mock it with any() for all 4 params
    when(jsonAdapterFactory.getTypeAdapter(
        any(ConstructorConstructor.class),
        any(Gson.class),
        any(TypeToken.class),
        any()))
        .thenReturn(null);

    // Mock Gson.getAdapter to return a TypeAdapter for Integer for publicField serialization
    when(gson.getAdapter(TypeToken.get(int.class))).thenReturn(new TypeAdapter<Integer>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, Integer value) {
        // no-op
      }
      @Override
      public Integer read(com.google.gson.stream.JsonReader in) {
        return 1;
      }
      @Override
      public com.google.gson.JsonElement toJsonTree(Integer value) {
        return new com.google.gson.JsonPrimitive(value);
      }
    });

    // Mock Gson.getAdapter to return a TypeAdapter for String for privateField (if needed)
    when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(new TypeAdapter<String>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, String value) {
        // no-op
      }
      @Override
      public String read(com.google.gson.stream.JsonReader in) {
        return "private";
      }
      @Override
      public com.google.gson.JsonElement toJsonTree(String value) {
        return new com.google.gson.JsonPrimitive(value);
      }
    });

    TypeAdapter<DummyClass> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);

    assertNotNull(adapter);

    DummyClass dummy = new DummyClass();
    assertEquals(dummy.publicField, adapter.toJsonTree(dummy).getAsJsonObject().get("publicField").getAsInt());
  }

  @Test
    @Timeout(8000)
  void testIncludeField_andGetFieldNames_viaReflection() throws Exception {
    Field privateField = DummyClass.class.getDeclaredField("privateField");

    // includeField is private, use reflection
    var includeFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("includeField", Field.class, boolean.class);
    includeFieldMethod.setAccessible(true);

    // Mock Excluder to exclude privateField when serialize=true
    when(excluder.excludeField(privateField, true)).thenReturn(true);
    boolean includedSerialize = (boolean) includeFieldMethod.invoke(reflectiveTypeAdapterFactory, privateField, true);
    assertFalse(includedSerialize);

    // excludeField returns false when serialize=false
    when(excluder.excludeField(privateField, false)).thenReturn(false);
    boolean includedDeserialize = (boolean) includeFieldMethod.invoke(reflectiveTypeAdapterFactory, privateField, false);
    assertTrue(includedDeserialize);

    // getFieldNames is private, use reflection
    var getFieldNamesMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("getFieldNames", Field.class);
    getFieldNamesMethod.setAccessible(true);

    // For a field without SerializedName annotation, returns list with one name
    List<String> names = (List<String>) getFieldNamesMethod.invoke(reflectiveTypeAdapterFactory, privateField);
    assertEquals(1, names.size());
    assertEquals("privateField", names.get(0));
  }

  @Test
    @Timeout(8000)
  void testCheckAccessible_setsAccessible() throws Exception {
    Method checkAccessibleMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Member.class);
    checkAccessibleMethod.setAccessible(true);

    Field privateField = DummyClass.class.getDeclaredField("privateField");
    assertFalse(privateField.canAccess(new DummyClass()));

    checkAccessibleMethod.invoke(null, privateField);

    assertTrue(privateField.canAccess(new DummyClass()));
  }

  @Test
    @Timeout(8000)
  void testCreateBoundField_serializationAndDeserialization() throws Exception {
    Gson gson = mock(Gson.class);
    Field field = DummyClass.class.getDeclaredField("publicField");
    Method accessor = null; // no accessor method
    String name = "publicField";
    TypeToken<?> fieldType = TypeToken.get(int.class);
    boolean serialize = true;
    boolean deserialize = true;
    boolean blockInaccessible = false;

    var createBoundFieldMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "createBoundField", Gson.class, Field.class, Method.class, String.class, TypeToken.class, boolean.class, boolean.class, boolean.class);
    createBoundFieldMethod.setAccessible(true);

    // Mock jsonAdapterFactory.getTypeAdapter with correct signature to return null for custom adapter
    when(jsonAdapterFactory.getTypeAdapter(
        any(ConstructorConstructor.class),
        any(Gson.class),
        any(TypeToken.class),
        any()))
        .thenReturn(null);

    // Mock Gson.getAdapter for the field type
    when(gson.getAdapter(fieldType)).thenReturn(new TypeAdapter<Integer>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, Integer value) {}
      @Override
      public Integer read(com.google.gson.stream.JsonReader in) { return 1; }
      @Override
      public com.google.gson.JsonElement toJsonTree(Integer value) { return new com.google.gson.JsonPrimitive(value); }
    });

    var boundField = createBoundFieldMethod.invoke(
        reflectiveTypeAdapterFactory,
        gson, field, accessor, name, fieldType, serialize, deserialize, blockInaccessible);

    assertNotNull(boundField);
  }

  @Test
    @Timeout(8000)
  void testGetBoundFields_returnsFieldsMap() throws Exception {
    Gson gson = mock(Gson.class);
    TypeToken<DummyClass> typeToken = TypeToken.get(DummyClass.class);
    Class<?> raw = DummyClass.class;

    var getBoundFieldsMethod = ReflectiveTypeAdapterFactory.class.getDeclaredMethod(
        "getBoundFields", Gson.class, TypeToken.class, Class.class, boolean.class, boolean.class);
    getBoundFieldsMethod.setAccessible(true);

    // Setup mocks for constructorConstructor and excluder to allow fields
    when(constructorConstructor.get(typeToken)).thenReturn(() -> new DummyClass());
    when(excluder.excludeClass(raw, true)).thenReturn(false);
    when(excluder.excludeField(any(Field.class), anyBoolean())).thenReturn(false);
    when(fieldNamingStrategy.translateName(any(Field.class))).thenAnswer(invocation -> {
      Field f = invocation.getArgument(0);
      return f.getName();
    });
    // Mock jsonAdapterFactory.getTypeAdapter with correct signature to return null
    when(jsonAdapterFactory.getTypeAdapter(
        any(ConstructorConstructor.class),
        any(Gson.class),
        any(TypeToken.class),
        any()))
        .thenReturn(null);

    // Mock Gson.getAdapter for int.class (publicField)
    when(gson.getAdapter(TypeToken.get(int.class))).thenReturn(new TypeAdapter<Integer>() {
      @Override
      public void write(com.google.gson.stream.JsonWriter out, Integer value) {}
      @Override
      public Integer read(com.google.gson.stream.JsonReader in) { return 1; }
      @Override
      public com.google.gson.JsonElement toJsonTree(Integer value) { return new com.google.gson.JsonPrimitive(value); }
    });

    Map<String, ?> boundFields = (Map<String, ?>) getBoundFieldsMethod.invoke(
        reflectiveTypeAdapterFactory, gson, typeToken, raw, false, false);

    assertNotNull(boundFields);
    assertTrue(boundFields.containsKey("publicField"));
  }
}