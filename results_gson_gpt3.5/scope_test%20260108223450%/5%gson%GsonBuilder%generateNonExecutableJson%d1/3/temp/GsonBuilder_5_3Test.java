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
import com.google.gson.GsonBuilder;
import java.lang.reflect.Field;

public class GsonBuilder_5_3Test {

  @Test
    @Timeout(8000)
  public void testGenerateNonExecutableJson() throws Exception {
    GsonBuilder builder = new GsonBuilder();

    // Initially, generateNonExecutableJson field should be false (default)
    Field field = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    field.setAccessible(true);
    boolean initialValue = (boolean) field.get(builder);
    assertFalse(initialValue, "Initial generateNonExecutableJson should be false");

    // Call generateNonExecutableJson method
    GsonBuilder returnedBuilder = builder.generateNonExecutableJson();

    // Method should return the same instance (this)
    assertSame(builder, returnedBuilder, "generateNonExecutableJson should return this");

    // The generateNonExecutableJson field should now be true
    boolean updatedValue = (boolean) field.get(builder);
    assertTrue(updatedValue, "generateNonExecutableJson should be true after method call");
  }
}