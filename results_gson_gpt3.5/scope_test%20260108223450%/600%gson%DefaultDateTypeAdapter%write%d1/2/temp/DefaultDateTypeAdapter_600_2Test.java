package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultDateTypeAdapter_600_2Test {

  private DefaultDateTypeAdapter<Date> adapter;
  private DateType<Date> dateType;

  @BeforeEach
  void setUp() throws Exception {
    // Create a DateType anonymous class for testing
    dateType = new DateType<Date>(Date.class) {
      // Removed @Override annotations to fix compilation errors
      public Date from(Date date) {
        return date;
      }

      public Date to(Date date) {
        return date;
      }

      public Date deserialize(Date json) {
        return json;
      }
    };
    // Use reflection to invoke private constructor DefaultDateTypeAdapter(DateType<T>, String)
    var constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DateType.class, String.class);
    constructor.setAccessible(true);
    adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    // Add a known DateFormat to dateFormats list (the private field)
    var dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
    dateFormats.clear();
    // Add a SimpleDateFormat that matches the pattern used in constructor
    dateFormats.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
  }

  @Test
    @Timeout(8000)
  void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
    verifyNoMoreInteractions(out);
  }

  @Test
    @Timeout(8000)
  void write_nonNullValue_formatsDateAndWritesValue() throws IOException {
    // Prepare JsonWriter writing to a StringWriter to capture output
    StringWriter stringWriter = new StringWriter();
    JsonWriter out = new JsonWriter(stringWriter);

    Date date = new Date(0L); // Epoch time

    adapter.write(out, date);

    // The formatted string should be the first dateFormat applied to date
    DateFormat dateFormat;
    try {
      var dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
      dateFormatsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
      dateFormat = dateFormats.get(0);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    String expected = dateFormat.format(date);

    // The JsonWriter writes JSON string values with quotes
    String jsonOutput = stringWriter.toString();

    // The output should contain the formatted date string quoted
    assertTrue(jsonOutput.contains(expected));
  }
}