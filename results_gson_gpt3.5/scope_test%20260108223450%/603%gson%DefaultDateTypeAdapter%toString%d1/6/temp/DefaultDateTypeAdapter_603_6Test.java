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
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultDateTypeAdapter_603_6Test {

  private DefaultDateTypeAdapter<Date> adapter;
  private DateType<Date> dateTypeMock;

  @BeforeEach
  @SuppressWarnings("unchecked")
  public void setUp() throws Exception {
    // Mock DateType instance using Mockito
    dateTypeMock = mock(DateType.class);

    // Use the constructor DefaultDateTypeAdapter(DateType<T> dateType, String datePattern)
    var constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DateType.class, String.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(dateTypeMock, "yyyy-MM-dd");

    // Clear and set dateFormats list to control toString output
    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
    dateFormats.clear();
  }

  @Test
    @Timeout(8000)
  public void testToString_withSimpleDateFormat() throws Exception {
    // Add a SimpleDateFormat instance to dateFormats list
    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    dateFormats.add(sdf);

    String expected = "DefaultDateTypeAdapter(" + sdf.toPattern() + ")";
    assertEquals(expected, adapter.toString());
  }

  @Test
    @Timeout(8000)
  public void testToString_withNonSimpleDateFormat() throws Exception {
    // Add a DateFormat subclass that is not SimpleDateFormat to dateFormats list
    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

    DateFormat anonDateFormat = new DateFormat() {
      @Override
      public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition fieldPosition) {
        return null;
      }

      @Override
      public Date parse(String source, ParsePosition pos) {
        return null;
      }
    };
    dateFormats.add(anonDateFormat);

    String expected = "DefaultDateTypeAdapter(" + anonDateFormat.getClass().getSimpleName() + ")";
    assertEquals(expected, adapter.toString());
  }
}