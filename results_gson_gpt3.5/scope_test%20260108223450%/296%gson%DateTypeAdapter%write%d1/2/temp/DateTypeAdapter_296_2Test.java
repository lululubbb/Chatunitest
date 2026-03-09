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
import java.util.ArrayList;
import java.util.Locale;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

class DateTypeAdapter_296_2Test {

  private DateTypeAdapter dateTypeAdapter;
  private JsonWriter jsonWriter;

  @BeforeEach
  void setUp() {
    dateTypeAdapter = new DateTypeAdapter();
    jsonWriter = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  void write_nullDate_writesNullValue() throws IOException {
    dateTypeAdapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
    verifyNoMoreInteractions(jsonWriter);
  }

  @Test
    @Timeout(8000)
  void write_nonNullDate_writesFormattedDateValue() throws IOException, Exception {
    Date date = new Date();

    // Access private field dateFormats via reflection to get the first DateFormat
    Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(dateTypeAdapter);

    DateFormat dateFormat = dateFormats.get(0);
    String expectedFormattedDate;
    synchronized (dateFormats) {
      expectedFormattedDate = dateFormat.format(date);
    }

    dateTypeAdapter.write(jsonWriter, date);

    InOrder inOrder = inOrder(jsonWriter);
    inOrder.verify(jsonWriter).value(expectedFormattedDate);
    verifyNoMoreInteractions(jsonWriter);
  }
}