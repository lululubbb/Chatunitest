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
import java.lang.reflect.Field;

class GsonBuilder_18_2Test {

  @Test
    @Timeout(8000)
  void testSetPrettyPrinting() throws Exception {
    GsonBuilder builder = new GsonBuilder();

    // Check default value of prettyPrinting (should be DEFAULT_PRETTY_PRINT)
    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean defaultPrettyPrinting = prettyPrintingField.getBoolean(builder);

    // Call setPrettyPrinting and verify return value is the same instance
    GsonBuilder returned = builder.setPrettyPrinting();
    assertSame(builder, returned, "setPrettyPrinting should return the same GsonBuilder instance");

    // Verify prettyPrinting field is set to true after calling setPrettyPrinting
    boolean prettyPrintingValue = prettyPrintingField.getBoolean(builder);
    assertTrue(prettyPrintingValue, "prettyPrinting field should be true after setPrettyPrinting");

    // If default was already true, then the field remains true; if false, it changed to true
    if (!defaultPrettyPrinting) {
      assertNotEquals(defaultPrettyPrinting, prettyPrintingValue, "prettyPrinting field should change from default to true");
    } else {
      assertEquals(defaultPrettyPrinting, prettyPrintingValue, "prettyPrinting field remains true");
    }
  }
}