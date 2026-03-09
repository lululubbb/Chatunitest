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
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTypeAdapter_294_3Test {

  private DateTypeAdapter dateTypeAdapter;

  @BeforeEach
  void setUp() {
    dateTypeAdapter = new DateTypeAdapter();
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnNullWhenJsonTokenIsNull() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    Date result = dateTypeAdapter.read(jsonReader);

    verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnDateFromDeserializeToDate() throws Exception {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("Jan 1, 2020 12:00:00 AM");

    // Use reflection to access private method deserializeToDate
    Method deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    deserializeToDateMethod.setAccessible(true);

    // Call deserializeToDate directly to get expectedDate
    Date expectedDate = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);

    // Call read() method, which internally calls deserializeToDate()
    Date actualDate = dateTypeAdapter.read(jsonReader);

    assertEquals(expectedDate, actualDate);
  }
}