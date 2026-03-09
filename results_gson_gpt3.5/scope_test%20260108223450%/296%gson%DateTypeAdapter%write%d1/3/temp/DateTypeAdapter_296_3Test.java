package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTypeAdapter_296_3Test {

  private DateTypeAdapter dateTypeAdapter;
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    dateTypeAdapter = new DateTypeAdapter();
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void write_nullValue_writesNullValue() throws IOException {
    dateTypeAdapter.write(jsonWriter, null);
    jsonWriter.flush();
    // JsonWriter writes "null" for null values
    assert stringWriter.toString().equals("null");
  }

  @Test
    @Timeout(8000)
  void write_nonNullValue_writesFormattedDate() throws Exception {
    Date date = new Date(0); // Epoch start

    // Use reflection to get and modify the private dateFormats field to a known format
    Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(dateTypeAdapter);

    // Clear existing formats and add a fixed known format
    dateFormats.clear();
    DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    dateFormats.add(simpleDateFormat);

    dateTypeAdapter.write(jsonWriter, date);
    jsonWriter.flush();

    String expected = simpleDateFormat.format(date);
    String actual = stringWriter.toString();

    // The output should be the date string wrapped in quotes because JsonWriter.value(String) writes JSON string
    assert actual.equals("\"" + expected + "\"");
  }
}