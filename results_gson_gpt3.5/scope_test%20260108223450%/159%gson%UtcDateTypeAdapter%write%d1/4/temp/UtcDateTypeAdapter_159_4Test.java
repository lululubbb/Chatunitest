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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.UtcDateTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class UtcDateTypeAdapter_159_4Test {

  private UtcDateTypeAdapter adapter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    adapter = new UtcDateTypeAdapter();
    jsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void write_nullDate_callsNullValue() throws IOException {
    adapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
    verifyNoMoreInteractions(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void write_nonNullDate_callsValueWithFormattedString() throws Exception {
    Date date = new Date(0L);

    // Use reflection to access private format method: 
    Method formatMethod = UtcDateTypeAdapter.class.getDeclaredMethod("format", Date.class, boolean.class, TimeZone.class);
    formatMethod.setAccessible(true);
    String formatted = (String) formatMethod.invoke(adapter, date, true, TimeZone.getTimeZone("UTC"));

    adapter.write(jsonWriter, date);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).value(formatted);
    verifyNoMoreInteractions(jsonWriter);
  }
}