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
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DateTypeAdapter_293_1Test {

  private DateTypeAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new DateTypeAdapter();
  }

  @Test
    @Timeout(8000)
  void testConstructor_initializesDateFormats() {
    // The constructor adds at least one DateFormat for US locale
    // and possibly more depending on default locale and Java version.
    // We check that dateFormats list is not empty via reflection.
    try {
      var field = DateTypeAdapter.class.getDeclaredField("dateFormats");
      field.setAccessible(true);
      var dateFormats = (List<DateFormat>) field.get(adapter);
      assertNotNull(dateFormats);
      assertTrue(dateFormats.size() >= 1);
      // The first DateFormat should be for Locale.US - check by formatting a known date string containing US locale month name
      DateFormat first = dateFormats.get(0);
      String formatted = first.format(new Date(0L)); // The epoch time
      // The formatted string should contain English month abbreviation (e.g., "Jan")
      assertTrue(formatted.toLowerCase(Locale.ROOT).contains("jan"));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void testRead_nullJson() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Date result = adapter.read(in);
    assertNull(result);

    verify(in).peek();
    verify(in).nextNull();
  }

  @Test
    @Timeout(8000)
  void testRead_validDateString() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("Jan 1, 2000, 12:00:00 AM");

    Date result = adapter.read(in);
    assertNotNull(result);

    verify(in).peek();
    verify(in).nextString();
  }

  @Test
    @Timeout(8000)
  void testRead_invalidDateString_throwsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("invalid-date");

    assertThrows(com.google.gson.JsonSyntaxException.class, () -> adapter.read(in));

    verify(in).peek();
    verify(in).nextString();
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_validDate() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Date date = new Date(0L);

    adapter.write(out, date);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(out).value(captor.capture());
    String dateString = captor.getValue();
    assertNotNull(dateString);
    assertFalse(dateString.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_validDateString() throws Exception {
    Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    JsonReader in = mock(JsonReader.class);
    when(in.nextString()).thenReturn("Jan 1, 2000, 12:00:00 AM");

    Date date = (Date) method.invoke(adapter, in);
    assertNotNull(date);
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_invalidDateString_throwsJsonSyntaxException() throws Exception {
    Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    JsonReader in = mock(JsonReader.class);
    when(in.nextString()).thenReturn("not-a-date");

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(adapter, in));
    assertTrue(thrown.getCause() instanceof com.google.gson.JsonSyntaxException);
  }
}