package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DefaultDateTypeAdapter_603_2Test {

  private DefaultDateTypeAdapter<Date> adapter;

  @BeforeEach
  public void setUp() throws Exception {
    Class<?> clazz = DefaultDateTypeAdapter.class;

    Class<?> dateTypeClass = null;
    for (Class<?> c : clazz.getDeclaredClasses()) {
      if ("DateType".equals(c.getSimpleName())) {
        dateTypeClass = c;
        break;
      }
    }

    // Create a Mockito mock of DateType to avoid instantiation issues
    Object dateTypeInstance = Mockito.mock(dateTypeClass);

    Constructor<?> constructor = null;
    for (Constructor<?> c : clazz.getDeclaredConstructors()) {
      Class<?>[] params = c.getParameterTypes();
      if (params.length == 2 && params[1] == String.class) {
        constructor = c;
        break;
      }
    }
    constructor.setAccessible(true);
    adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateTypeInstance, "yyyy-MM-dd");
  }

  @Test
    @Timeout(8000)
  public void testToString_withSimpleDateFormat() throws Exception {
    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
    dateFormats.clear();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    dateFormats.add(sdf);

    String result = adapter.toString();

    assertEquals("DefaultDateTypeAdapter(yyyy-MM-dd)", result);
  }

  @Test
    @Timeout(8000)
  public void testToString_withNonSimpleDateFormat() throws Exception {
    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
    dateFormats.clear();

    DateFormat customFormat = new DateFormat() {
      private static final long serialVersionUID = 1L;

      @Override
      public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition fieldPosition) {
        return null;
      }

      @Override
      public Date parse(String source, ParsePosition pos) {
        return null;
      }
    };
    dateFormats.add(customFormat);

    String result = adapter.toString();

    String expected = "DefaultDateTypeAdapter(" + customFormat.getClass().getSimpleName() + ")";
    assertEquals(expected, result);
  }
}