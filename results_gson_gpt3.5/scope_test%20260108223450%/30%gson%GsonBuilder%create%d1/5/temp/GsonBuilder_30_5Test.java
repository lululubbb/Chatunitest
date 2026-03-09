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
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
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

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;

public class GsonBuilder_30_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testCreate_defaultSettings() throws Exception {
    // Arrange
    // Use reflection to spy on private method addTypeAdaptersForDate
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // We will verify that addTypeAdaptersForDate is called with correct parameters
    // Use reflection to get private method
    Method addTypeAdaptersForDateMethod = GsonBuilder.class.getDeclaredMethod(
        "addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    addTypeAdaptersForDateMethod.setAccessible(true);

    // Prepare to capture argument passed to addTypeAdaptersForDate
    doAnswer(invocation -> {
      String datePattern = invocation.getArgument(0);
      int dateStyle = invocation.getArgument(1);
      int timeStyle = invocation.getArgument(2);
      List<TypeAdapterFactory> factories = invocation.getArgument(3);

      // Verify date pattern and styles are defaults
      assertEquals(Gson.DEFAULT_DATE_PATTERN, datePattern);
      assertEquals(DateFormat.DEFAULT, dateStyle);
      assertEquals(DateFormat.DEFAULT, timeStyle);
      assertNotNull(factories);
      return null;
    }).when(spyBuilder).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());

    // Act
    Gson gson = spyBuilder.create();

    // Assert
    assertNotNull(gson);

    // Check fields of Gson set from GsonBuilder's default fields
    Field excluderField = Gson.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gson);
    assertEquals(gsonBuilder.excluder, excluder);

    Field fieldNamingPolicyField = Gson.class.getDeclaredField("fieldNamingStrategy");
    fieldNamingPolicyField.setAccessible(true);
    FieldNamingStrategy fieldNamingPolicy = (FieldNamingStrategy) fieldNamingPolicyField.get(gson);
    assertEquals(gsonBuilder.fieldNamingPolicy, fieldNamingPolicy);

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = serializeNullsField.getBoolean(gson);
    assertEquals(gsonBuilder.serializeNulls, serializeNulls);

    Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gson);
    assertEquals(gsonBuilder.complexMapKeySerialization, complexMapKeySerialization);

    Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gson);
    assertEquals(gsonBuilder.generateNonExecutableJson, generateNonExecutableJson);

    Field escapeHtmlCharsField = Gson.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    boolean escapeHtmlChars = escapeHtmlCharsField.getBoolean(gson);
    assertEquals(gsonBuilder.escapeHtmlChars, escapeHtmlChars);

    Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = prettyPrintingField.getBoolean(gson);
    assertEquals(gsonBuilder.prettyPrinting, prettyPrinting);

    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = lenientField.getBoolean(gson);
    assertEquals(gsonBuilder.lenient, lenient);

    Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gson);
    assertEquals(gsonBuilder.serializeSpecialFloatingPointValues, serializeSpecialFloatingPointValues);

    Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gson);
    assertEquals(gsonBuilder.useJdkUnsafe, useJdkUnsafe);

    Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    LongSerializationPolicy longSerializationPolicy = (LongSerializationPolicy) longSerializationPolicyField.get(gson);
    assertEquals(gsonBuilder.longSerializationPolicy, longSerializationPolicy);

    Field datePatternField = Gson.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String datePattern = (String) datePatternField.get(gson);
    assertEquals(gsonBuilder.datePattern, datePattern);

    Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyle = dateStyleField.getInt(gson);
    assertEquals(gsonBuilder.dateStyle, dateStyle);

    Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int timeStyle = timeStyleField.getInt(gson);
    assertEquals(gsonBuilder.timeStyle, timeStyle);

    // Verify factories list contains factories from builder and hierarchyFactories reversed and combined
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    assertNotNull(factories);
    assertTrue(factories.containsAll(gsonBuilder.factories));
    assertTrue(factories.containsAll(gsonBuilder.hierarchyFactories));
  }

  @Test
    @Timeout(8000)
  public void testCreate_withCustomFactoriesAndHierarchyFactories() throws Exception {
    // Arrange
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory2 = mock(TypeAdapterFactory.class);

    gsonBuilder.factories.add(factory1);
    gsonBuilder.factories.add(factory2);
    gsonBuilder.hierarchyFactories.add(hierarchyFactory1);
    gsonBuilder.hierarchyFactories.add(hierarchyFactory2);

    // Act
    Gson gson = gsonBuilder.create();

    // Assert
    assertNotNull(gson);

    // Access the factories list inside Gson
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    assertNotNull(factories);

    // The combined factories list should contain all from factories and hierarchyFactories reversed
    assertTrue(factories.containsAll(gsonBuilder.factories));
    assertTrue(factories.containsAll(gsonBuilder.hierarchyFactories));

    // The order: original factories reversed, then hierarchyFactories reversed, then addTypeAdaptersForDate adds 3 more factories
    // Check that the first factories are reversed factories list
    List<TypeAdapterFactory> expectedFactoriesOrder = new ArrayList<>();
    List<TypeAdapterFactory> reversedFactories = new ArrayList<>(gsonBuilder.factories);
    Collections.reverse(reversedFactories);
    expectedFactoriesOrder.addAll(reversedFactories);
    List<TypeAdapterFactory> reversedHierarchyFactories = new ArrayList<>(gsonBuilder.hierarchyFactories);
    Collections.reverse(reversedHierarchyFactories);
    expectedFactoriesOrder.addAll(reversedHierarchyFactories);

    // The factories list inside Gson ends with addTypeAdaptersForDate additions, so factories.size() >= expectedFactoriesOrder.size()
    assertTrue(factories.size() >= expectedFactoriesOrder.size());

    // Check order of first elements
    for (int i = 0; i < expectedFactoriesOrder.size(); i++) {
      assertSame(expectedFactoriesOrder.get(i), factories.get(i));
    }
  }

  @Test
    @Timeout(8000)
  public void testCreate_withCustomDatePatternAndStyles() throws Exception {
    // Arrange
    String customPattern = "yyyy-MM-dd'T'HH:mm:ss";
    int customDateStyle = DateFormat.SHORT;
    int customTimeStyle = DateFormat.MEDIUM;

    // Use reflection to set private fields datePattern, dateStyle, timeStyle
    setPrivateField(gsonBuilder, "datePattern", customPattern);
    setPrivateField(gsonBuilder, "dateStyle", customDateStyle);
    setPrivateField(gsonBuilder, "timeStyle", customTimeStyle);

    // Spy on builder to verify addTypeAdaptersForDate called with custom values
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    doAnswer(invocation -> {
      String datePattern = invocation.getArgument(0);
      int dateStyle = invocation.getArgument(1);
      int timeStyle = invocation.getArgument(2);
      List<TypeAdapterFactory> factories = invocation.getArgument(3);

      assertEquals(customPattern, datePattern);
      assertEquals(customDateStyle, dateStyle);
      assertEquals(customTimeStyle, timeStyle);
      assertNotNull(factories);
      return null;
    }).when(spyBuilder).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());

    // Act
    Gson gson = spyBuilder.create();

    // Assert
    assertNotNull(gson);
  }

  private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}