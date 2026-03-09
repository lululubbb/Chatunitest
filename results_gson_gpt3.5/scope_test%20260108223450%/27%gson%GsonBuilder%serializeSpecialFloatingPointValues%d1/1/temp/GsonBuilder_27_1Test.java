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
import org.junit.jupiter.api.Test;

class GsonBuilder_27_1Test {

  @Test
    @Timeout(8000)
  void serializeSpecialFloatingPointValues_setsFlagToTrue_andReturnsThis() {
    GsonBuilder builder = new GsonBuilder();
    // Initially the flag should be false (default from DEFAULT_SPECIALIZE_FLOAT_VALUES)
    // but since DEFAULT_SPECIALIZE_FLOAT_VALUES is not given here, we assume false to test effect.
    // Call the focal method
    GsonBuilder returned = builder.serializeSpecialFloatingPointValues();
    // Check that the method returns the same instance (for chaining)
    assertSame(builder, returned);

    // Use reflection to access the private field serializeSpecialFloatingPointValues
    boolean flagValue;
    try {
      var field = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
      field.setAccessible(true);
      flagValue = field.getBoolean(builder);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
    // The flag should now be true
    assertTrue(flagValue);
  }
}