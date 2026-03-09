package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultDateTypeAdapter_597_2Test {

  private DefaultDateTypeAdapter<Date> adapter;

  private static final String PATTERN = "yyyy-MM-dd";

  private static class DateTypeImpl implements DefaultDateTypeAdapter.DateType<Date> {
    @Override
    public Date deserialize(Date date) {
      return date;
    }
  }

  @BeforeEach
  void setUp() throws Exception {
    Class<?> clazz = DefaultDateTypeAdapter.class;
    Class<?> dateTypeClass = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter$DateType");

    Object dateType = new DateTypeImpl();

    Constructor<?> constructor = clazz.getDeclaredConstructor(dateTypeClass, String.class);
    constructor.setAccessible(true);
    adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, PATTERN);
  }

  @Test
    @Timeout(8000)
  void testWriteAndRead() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Date now = new Date();

    adapter.write(writer, now);
    verify(writer).value(anyString());

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();
    assertNull(adapter.read(reader));

    when(reader.peek()).thenReturn(JsonToken.STRING);
    SimpleDateFormat sdf = new SimpleDateFormat(PATTERN, Locale.US);
    String dateStr = sdf.format(now);
    when(reader.nextString()).thenReturn(dateStr);
    Date parsed = adapter.read(reader);
    assertNotNull(parsed);
    assertEquals(sdf.format(now), sdf.format(parsed));
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_validAndInvalid() throws Exception {
    Method deserializeToDate = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    deserializeToDate.setAccessible(true);

    JsonReader reader = mock(JsonReader.class);

    when(reader.nextString()).thenReturn("2020-01-01");
    Object result = deserializeToDate.invoke(adapter, reader);
    assertTrue(result instanceof Date);

    when(reader.nextString()).thenReturn("invalid-date");
    try {
      deserializeToDate.invoke(adapter, reader);
      fail("Expected InvocationTargetException with cause JsonSyntaxException");
    } catch (InvocationTargetException e) {
      assertTrue(e.getCause() instanceof JsonSyntaxException);
    }
  }

  @Test
    @Timeout(8000)
  void testToString_containsClassName() {
    String s = adapter.toString();
    assertTrue(s.contains("DefaultDateTypeAdapter"));
  }
}