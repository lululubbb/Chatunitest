package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_599_2Test {

  private DefaultDateTypeAdapter<Date> adapter;
  private DefaultDateTypeAdapter.DateType<Date> dateType;

  @BeforeEach
  void setUp() throws Exception {
    // Create a DateType with required Class<Date> constructor and implement abstract methods
    Class<Date> clazz = Date.class;
    dateType = new DefaultDateTypeAdapter.DateType<Date>(clazz) {
      public Date cast(Date d) {
        return d;
      }

      public Date deserialize(Date d) {
        return d;
      }
    };

    Constructor<DefaultDateTypeAdapter> constructor =
        DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, int.class, int.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(dateType, DateFormat.SHORT, DateFormat.SHORT);
  }

  @Test
    @Timeout(8000)
  void testWrite_andRead() throws Exception {
    JsonWriter writer = mock(JsonWriter.class);
    Date now = new Date();

    // Test write with a valid date
    adapter.write(writer, now);
    verify(writer).value(anyString());

    // Test write with null
    adapter.write(writer, null);
    verify(writer).nullValue();

    // Prepare JsonReader for read: simulate a string date that matches one of the dateFormats
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    DateFormat format = (DateFormat) getField(adapter, "dateFormats", List.class).get(0);
    String formattedDate = format.format(now);
    when(reader.nextString()).thenReturn(formattedDate);

    Date parsedDate = adapter.read(reader);
    assertNotNull(parsedDate);
    assertEquals(format.parse(formattedDate), parsedDate);

    // Test read with null token
    when(reader.peek()).thenReturn(JsonToken.NULL);
    adapter.read(reader);
    verify(reader, atLeastOnce()).nextNull();
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_validAndInvalid() throws Exception {
    JsonReader reader = mock(JsonReader.class);

    Method deserializeToDate = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    deserializeToDate.setAccessible(true);

    // Valid date string
    when(reader.nextString()).thenReturn(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    Date result = (Date) deserializeToDate.invoke(adapter, reader);
    assertNotNull(result);

    // Invalid date string triggers JsonSyntaxException wrapped in InvocationTargetException
    when(reader.nextString()).thenReturn("invalid-date");

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> deserializeToDate.invoke(adapter, reader));
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof JsonSyntaxException, "Expected cause to be JsonSyntaxException");
  }

  @Test
    @Timeout(8000)
  void testToString_containsClassName() {
    String str = adapter.toString();
    assertTrue(str.contains("DefaultDateTypeAdapter"));
  }

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName, Class<T> clazz) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }
}