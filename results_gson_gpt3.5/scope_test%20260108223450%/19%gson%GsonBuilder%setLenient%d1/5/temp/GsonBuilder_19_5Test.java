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

public class GsonBuilder_19_5Test {

  @Test
    @Timeout(8000)
  public void testSetLenient() throws Exception {
    GsonBuilder gsonBuilder = new GsonBuilder();

    // Before calling setLenient(), lenient should be default (false)
    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean initialLenient = (boolean) lenientField.get(gsonBuilder);
    assertFalse(initialLenient, "Initial lenient field should be false");

    // Call setLenient()
    GsonBuilder returned = gsonBuilder.setLenient();

    // Check that setLenient returns the same instance (this)
    assertSame(gsonBuilder, returned, "setLenient should return the same GsonBuilder instance");

    // Check that lenient field is now true
    boolean updatedLenient = (boolean) lenientField.get(gsonBuilder);
    assertTrue(updatedLenient, "Lenient field should be true after setLenient");
  }
}