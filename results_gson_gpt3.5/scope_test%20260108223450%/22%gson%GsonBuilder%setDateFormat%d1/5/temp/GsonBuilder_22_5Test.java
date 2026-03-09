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
import java.text.DateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GsonBuilder_22_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_withStyle_setsDateStyleAndNullifiesPattern() throws Exception {
    int style = DateFormat.MEDIUM;

    GsonBuilder returnedBuilder = gsonBuilder.setDateFormat(style);

    // Verify returned instance is the same
    assertSame(gsonBuilder, returnedBuilder);

    // Use reflection to check private fields dateStyle and datePattern
    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int actualDateStyle = dateStyleField.getInt(gsonBuilder);
    assertEquals(style, actualDateStyle);

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String actualDatePattern = (String) datePatternField.get(gsonBuilder);
    assertNull(actualDatePattern);
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_withDifferentStyles() throws Exception {
    int[] styles = {DateFormat.DEFAULT, DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL};

    for (int style : styles) {
      GsonBuilder builder = new GsonBuilder();
      GsonBuilder returned = builder.setDateFormat(style);
      assertSame(builder, returned);

      Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
      dateStyleField.setAccessible(true);
      int actualDateStyle = dateStyleField.getInt(builder);
      assertEquals(style, actualDateStyle);

      Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
      datePatternField.setAccessible(true);
      String actualDatePattern = (String) datePatternField.get(builder);
      assertNull(actualDatePattern);
    }
  }

  @Test
    @Timeout(8000)
  public void testSetDateFormat_reflectionAccess() throws Exception {
    // Use reflection to invoke private addTypeAdaptersForDate to verify interaction indirectly
    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);

    GsonBuilder builder = new GsonBuilder();
    builder.setDateFormat(DateFormat.SHORT);

    // Should set dateStyle and nullify datePattern
    assertEquals(DateFormat.SHORT, dateStyleField.getInt(builder));
    assertNull((String) datePatternField.get(builder));

    // The factories list should remain accessible and modifiable
    @SuppressWarnings("unchecked")
    java.util.List<TypeAdapterFactory> factories = (java.util.List<TypeAdapterFactory>) factoriesField.get(builder);
    assertNotNull(factories);
  }
}