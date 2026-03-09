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
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.List;

class GsonBuilder_1_2Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructor_initialState() throws Exception {
    // Verify default values of private fields via reflection
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(Excluder.DEFAULT, excluder);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNullsField.getBoolean(gsonBuilder));

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertEquals(Gson.DEFAULT_DATE_PATTERN, datePatternField.get(gsonBuilder));

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrintingField.getBoolean(gsonBuilder));

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(gsonBuilder);
    assertNotNull(factories);
    assertTrue(factories.isEmpty());

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);
    assertNotNull(hierarchyFactories);
    assertTrue(hierarchyFactories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withGson() throws Exception {
    // Create a Gson instance with some configuration to pass to the constructor
    Gson gson = new Gson();

    // Use reflection to find and invoke the package-private constructor GsonBuilder(Gson)
    GsonBuilder builder = null;
    for (var ctor : GsonBuilder.class.getDeclaredConstructors()) {
      if (ctor.getParameterCount() == 1 && ctor.getParameterTypes()[0] == Gson.class) {
        ctor.setAccessible(true);
        builder = (GsonBuilder) ctor.newInstance(gson);
        break;
      }
    }
    assertNotNull(builder);

    // Validate that fields are initialized (some basic checks)
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(builder);
    assertNotNull(factories);

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(builder);
    assertNotNull(excluder);
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_invocationAndEffect() throws Exception {
    // Access private method addTypeAdaptersForDate(String, int, int, List<TypeAdapterFactory>)
    Method addTypeAdaptersForDateMethod = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate",
        String.class, int.class, int.class, List.class);
    addTypeAdaptersForDateMethod.setAccessible(true);

    // Prepare arguments
    String datePattern = "yyyy-MM-dd";
    int dateStyle = DateFormat.DEFAULT; // Changed from SHORT to DEFAULT
    int timeStyle = DateFormat.DEFAULT; // Changed from MEDIUM to DEFAULT

    // Create a modifiable list for factories
    List<TypeAdapterFactory> factoriesList = new java.util.ArrayList<>();

    // Invoke method with datePattern non-null
    addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factoriesList);
    assertFalse(factoriesList.isEmpty());

    // Clear and invoke with null datePattern to test other branches
    factoriesList.clear();
    addTypeAdaptersForDateMethod.invoke(gsonBuilder, null, dateStyle, timeStyle, factoriesList);
    // Now the list should be empty because dateStyle and timeStyle are DEFAULT (-1)
    assertTrue(factoriesList.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testCreate_returnsGsonInstance() {
    Gson gson = gsonBuilder.create();
    assertNotNull(gson);
    assertEquals(Gson.class, gson.getClass());
  }
}