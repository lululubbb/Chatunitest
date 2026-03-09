package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateTypeAdapter_293_2Test {

  private DateTypeAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new DateTypeAdapter();
  }

  @SuppressWarnings("unchecked")
  @Test
    @Timeout(8000)
  void testConstructor_initializesDateFormats() throws Exception {
    // Reflectively check dateFormats list size and contents
    var dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    var dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

    assertNotNull(dateFormats);
    assertTrue(dateFormats.size() >= 1);
    DateFormat usFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    boolean found = dateFormats.stream().anyMatch(df -> df.toString().equals(usFormat.toString()));
    assertTrue(found);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withDate() {
    TypeToken<Date> dateTypeToken = TypeToken.get(Date.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);

    TypeAdapter<Date> dateAdapter = (TypeAdapter<Date>) DateTypeAdapter.FACTORY.create(new Gson(), dateTypeToken);
    assertNotNull(dateAdapter);
    assertTrue(dateAdapter instanceof DateTypeAdapter);

    TypeAdapter<String> stringAdapter = (TypeAdapter<String>) DateTypeAdapter.FACTORY.create(new Gson(), stringTypeToken);
    assertNull(stringAdapter);
  }

  @Test
    @Timeout(8000)
  void testWrite_and_Read_withValidDate() throws IOException {
    Date date = new Date(0); // Epoch

    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    adapter.write(jsonWriter, date);
    jsonWriter.close();

    String json = stringWriter.toString();
    assertNotNull(json);
    assertFalse(json.isEmpty());

    JsonReader jsonReader = new JsonReader(new StringReader(json));
    Date readDate = adapter.read(jsonReader);
    assertNotNull(readDate);
    assertEquals(date.getTime(), readDate.getTime());
  }

  @Test
    @Timeout(8000)
  void testRead_nullJson_returnsNull() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(jsonReader).nextNull();

    Date result = adapter.read(jsonReader);
    assertNull(result);

    verify(jsonReader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testRead_invalidFormat_throwsJsonSyntaxException() throws IOException {
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("invalid-date-format");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
    // Fix: check for "Failed parsing" instead of "Failed parsing date"
    assertTrue(ex.getMessage().contains("Failed parsing"));
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_withValidAndInvalidInputs() throws Exception {
    Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    // Valid date string
    JsonReader jsonReader = mock(JsonReader.class);
    when(jsonReader.nextString()).thenReturn("Jan 1, 1970 12:00:00 AM");
    Date date = (Date) method.invoke(adapter, jsonReader);
    assertNotNull(date);

    // Invalid date string triggers JsonSyntaxException wrapped in InvocationTargetException
    when(jsonReader.nextString()).thenReturn("not-a-date");
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(adapter, jsonReader));
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof JsonSyntaxException);
    assertTrue(cause.getMessage().contains("Failed parsing"));
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue_writesNull() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    adapter.write(jsonWriter, null);
    jsonWriter.close();

    assertEquals("null", stringWriter.toString());
  }
}