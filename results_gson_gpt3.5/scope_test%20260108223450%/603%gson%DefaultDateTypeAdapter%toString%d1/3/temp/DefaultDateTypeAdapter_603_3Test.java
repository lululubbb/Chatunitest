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

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultDateTypeAdapter_603_3Test {

  private DefaultDateTypeAdapter<Date> adapter;

  @BeforeEach
  public void setUp() throws Exception {
    adapter = createInstance();

    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    List<DateFormat> dateFormats = new ArrayList<>();
    dateFormatsField.set(adapter, dateFormats);
  }

  @Test
    @Timeout(8000)
  public void toString_withSimpleDateFormat_returnsPattern() throws Exception {
    List<DateFormat> dateFormats = getDateFormats(adapter);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    dateFormats.add(sdf);

    String expected = "DefaultDateTypeAdapter(" + sdf.toPattern() + ")";
    assertEquals(expected, adapter.toString());
  }

  @Test
    @Timeout(8000)
  public void toString_withNonSimpleDateFormat_returnsClassName() throws Exception {
    List<DateFormat> dateFormats = getDateFormats(adapter);
    DateFormat anonymousFormat = new DateFormat() {
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
    dateFormats.add(anonymousFormat);

    String expected = "DefaultDateTypeAdapter(" + anonymousFormat.getClass().getSimpleName() + ")";
    assertEquals(expected, adapter.toString());
  }

  @SuppressWarnings("unchecked")
  private DefaultDateTypeAdapter<Date> createInstance() throws Exception {
    Class<?> clazz = DefaultDateTypeAdapter.class;
    Class<?> dateTypeClass = null;
    for (Class<?> innerClass : clazz.getDeclaredClasses()) {
      if ("DateType".equals(innerClass.getSimpleName())) {
        dateTypeClass = innerClass;
        break;
      }
    }
    if (dateTypeClass == null) {
      throw new IllegalStateException("DateType class not found");
    }

    // Use existing enum constants if available
    Object[] enumConstants = dateTypeClass.getEnumConstants();
    if (enumConstants == null || enumConstants.length == 0) {
      throw new IllegalStateException("DateType enum has no constants");
    }
    Object dateType = enumConstants[0]; // use first enum constant

    Constructor<?> constructor = clazz.getDeclaredConstructor(dateTypeClass, String.class);
    constructor.setAccessible(true);
    return (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, "yyyy-MM-dd");
  }

  @SuppressWarnings("unchecked")
  private List<DateFormat> getDateFormats(DefaultDateTypeAdapter<Date> adapter) throws Exception {
    Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    return (List<DateFormat>) dateFormatsField.get(adapter);
  }
}