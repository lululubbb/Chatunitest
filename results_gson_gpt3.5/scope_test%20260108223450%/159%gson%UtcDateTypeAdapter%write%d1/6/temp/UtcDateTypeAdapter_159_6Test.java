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
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class UtcDateTypeAdapter_159_6Test {

  private UtcDateTypeAdapter adapter;
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() {
    adapter = new UtcDateTypeAdapter();
    stringWriter = new StringWriter();
    jsonWriter = new JsonWriter(stringWriter);
  }

  @Test
    @Timeout(8000)
  void write_nullDate_writesNullValue() throws IOException {
    JsonWriter spyWriter = Mockito.spy(jsonWriter);

    adapter.write(spyWriter, null);

    verify(spyWriter).nullValue();
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void write_nonNullDate_writesFormattedValue() throws Exception {
    Date date = new Date(1672531200000L); // 2023-01-01T00:00:00Z

    // Use reflection to invoke private format method to get expected formatted string
    Method formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
    formatMethod.setAccessible(true);
    String expectedValue = (String) formatMethod.invoke(adapter, date, true, TimeZone.getTimeZone("UTC"));

    adapter.write(jsonWriter, date);
    jsonWriter.flush();

    String jsonOutput = stringWriter.toString();
    // JsonWriter.value(String) writes the string surrounded by quotes in output
    assertTrue(jsonOutput.contains(expectedValue));
  }
}