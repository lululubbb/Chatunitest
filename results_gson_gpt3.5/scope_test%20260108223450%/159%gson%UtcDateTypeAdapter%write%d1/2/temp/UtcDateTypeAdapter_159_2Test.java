package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import static org.mockito.Mockito.*;

import com.google.gson.typeadapters.UtcDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimeZone;

class UtcDateTypeAdapter_159_2Test {

  private UtcDateTypeAdapter adapter;
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    adapter = new UtcDateTypeAdapter();
    stringWriter = new StringWriter();
    jsonWriter = spy(new JsonWriter(stringWriter));
  }

  @Test
    @Timeout(8000)
  void write_nullDate_shouldCallNullValue() throws IOException {
    adapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
    verify(jsonWriter, never()).value(anyString());
  }

  @Test
    @Timeout(8000)
  void write_nonNullDate_shouldFormatAndWriteValue() throws Exception {
    Date date = new Date(1625077800000L); // Fixed timestamp for deterministic test

    // Use reflection to call private format method to get expected value
    Method formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
    formatMethod.setAccessible(true);
    String expectedValue = (String) formatMethod.invoke(adapter, date, true, TimeZone.getTimeZone("UTC"));

    adapter.write(jsonWriter, date);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).value(expectedValue);
    inOrder.verify(jsonWriter, never()).nullValue();
  }

}