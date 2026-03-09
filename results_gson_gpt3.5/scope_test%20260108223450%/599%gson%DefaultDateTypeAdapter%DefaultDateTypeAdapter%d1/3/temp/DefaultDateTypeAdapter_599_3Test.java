package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.JavaVersion;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_599_3Test {
  private Class<?> clazz;
  private Object adapterInstance;
  private Object mockDateType;

  @BeforeEach
  void setUp() throws Exception {
    clazz = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

    Class<?> dateTypeClass = null;
    for (Class<?> innerClass : clazz.getDeclaredClasses()) {
      if ("DateType".equals(innerClass.getSimpleName())) {
        dateTypeClass = innerClass;
        break;
      }
    }
    assertNotNull(dateTypeClass);

    // Instead of Proxy, create a simple anonymous subclass instance of DateType
    // DateType is a static abstract class inside DefaultDateTypeAdapter, so we can instantiate it via reflection
    Constructor<?> dateTypeCtor = dateTypeClass.getDeclaredConstructor();
    dateTypeCtor.setAccessible(true);
    mockDateType = java.lang.reflect.Proxy.isProxyClass(dateTypeClass) ? null : null; // just to keep compiler happy

    // Create an anonymous subclass of DateType<T> overriding serialize and deserialize methods
    // Since DateType is abstract, we can create a subclass dynamically via Proxy or better via reflection + subclassing
    // But Proxy requires an interface, so we do subclassing via reflection with a custom class loader or use Proxy for interface
    // Here, create a dynamic subclass using an anonymous class via reflection:

    // Use a dynamic subclass via reflection with a new ClassLoader and Unsafe is complicated, so instead create a subclass in code:
    // So, create a subclass of DateType with overridden methods via a dynamic proxy is not possible since DateType is a class.
    // We'll create a subclass manually via reflection using MethodHandles or a helper class.

    // Instead, create a subclass in code:
    mockDateType = new Object() {
      public Object deserialize(Object arg) {
        return arg;
      }
      public Object serialize(Object arg) {
        return arg;
      }
    };

    // But this is not an instance of DateType. So, better approach:

    // Use reflection to create a subclass of DateType<T> overriding methods:
    // We'll use java.lang.reflect.Proxy is not possible, so use a tiny helper subclass:
    mockDateType = new Object() {
    };

    // So instead, create a subclass using reflection and anonymous class:
    // We can do this by using a small helper class inside this test class:

    // So the simplest is to create a small inner class extending DateType inside the test class:

  }

  private Object createDateTypeInstance(Class<?> dateTypeClass) throws Exception {
    return java.lang.reflect.Proxy.isProxyClass(dateTypeClass) ? null : null; // placeholder
  }

  private Object createDateTypeInstance() throws Exception {
    Class<?> dateTypeClass = null;
    for (Class<?> innerClass : clazz.getDeclaredClasses()) {
      if ("DateType".equals(innerClass.getSimpleName())) {
        dateTypeClass = innerClass;
        break;
      }
    }
    assertNotNull(dateTypeClass);

    // Create an anonymous subclass of DateType overriding serialize and deserialize methods
    // via reflection and Unsafe or MethodHandles is complicated,
    // so use a small helper class defined below:

    return new DateTypeImpl();
  }

  private class DateTypeImpl extends DefaultDateTypeAdapter.DateType<Date> {
    public DateTypeImpl() {
      super();
    }

    @Override
    public Date deserialize(Object o) {
      return (Date) o;
    }

    @Override
    public Object serialize(Object o) {
      return o;
    }
  }

  @BeforeEach
  void setUpFixed() throws Exception {
    clazz = Class.forName("com.google.gson.internal.bind.DefaultDateTypeAdapter");

    // find DateType class
    Class<?> dateTypeClass = null;
    for (Class<?> innerClass : clazz.getDeclaredClasses()) {
      if ("DateType".equals(innerClass.getSimpleName())) {
        dateTypeClass = innerClass;
        break;
      }
    }
    assertNotNull(dateTypeClass);

    // Create an anonymous subclass of DateType<Date> overriding serialize and deserialize
    Object dateTypeInstance = new DateTypeDateImpl();

    Constructor<?> ctor = clazz.getDeclaredConstructor(dateTypeClass, int.class, int.class);
    ctor.setAccessible(true);
    adapterInstance = ctor.newInstance(dateTypeInstance, DateFormat.SHORT, DateFormat.SHORT);
  }

  private class DateTypeDateImpl extends DefaultDateTypeAdapter.DateType<Date> {
    public DateTypeDateImpl() {
      super();
    }

    @Override
    public Date deserialize(Object o) {
      return (Date) o;
    }

    @Override
    public Object serialize(Object o) {
      return o;
    }
  }

  @Test
    @Timeout(8000)
  void testConstructor_addsDateFormats() throws Exception {
    setUpFixed();

    Field dateFormatsField = clazz.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapterInstance);

    assertNotNull(dateFormats);
    boolean containsUS = dateFormats.stream()
        .anyMatch(df -> {
          Locale locale = null;
          try {
            if (df instanceof java.text.SimpleDateFormat) {
              Method getDateFormatSymbols = DateFormat.class.getDeclaredMethod("getDateFormatSymbols");
              getDateFormatSymbols.setAccessible(true);
              Object dfs = getDateFormatSymbols.invoke(df);
              Method getLocale = dfs.getClass().getDeclaredMethod("getLocale");
              getLocale.setAccessible(true);
              locale = (Locale) getLocale.invoke(dfs);
            }
          } catch (Exception e) {
          }
          return Locale.US.equals(locale);
        });
    assertTrue(containsUS);

    if (!Locale.getDefault().equals(Locale.US)) {
      assertTrue(dateFormats.size() >= 2);
    }

    if (JavaVersion.isJava9OrLater()) {
      assertTrue(dateFormats.size() >= 3);
    }
  }

  @Test
    @Timeout(8000)
  void testWrite_andRead() throws Exception {
    setUpFixed();

    JsonWriter writer = mock(JsonWriter.class);
    Date now = new Date();

    Method writeMethod = clazz.getMethod("write", JsonWriter.class, Date.class);
    writeMethod.invoke(adapterInstance, writer, now);
    verify(writer).value(anyString());

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);

    Field dateFormatsField = clazz.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapterInstance);
    String formattedDate = dateFormats.get(0).format(now);

    when(reader.nextString()).thenReturn(formattedDate);

    Method readMethod = clazz.getMethod("read", JsonReader.class);
    Object result = readMethod.invoke(adapterInstance, reader);
    assertNotNull(result);
    assertTrue(result instanceof Date);
  }

  @Test
    @Timeout(8000)
  void testRead_withNull() throws Exception {
    setUpFixed();

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Method readMethod = clazz.getMethod("read", JsonReader.class);
    Object result = readMethod.invoke(adapterInstance, reader);
    assertNull(result);
    verify(reader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_validAndInvalid() throws Exception {
    setUpFixed();

    Method deserializeMethod = clazz.getDeclaredMethod("deserializeToDate", JsonReader.class);
    deserializeMethod.setAccessible(true);

    JsonReader reader = mock(JsonReader.class);

    Field dateFormatsField = clazz.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapterInstance);
    Date now = new Date();
    String validDateStr = dateFormats.get(0).format(now);

    when(reader.nextString()).thenReturn(validDateStr);

    Object result = deserializeMethod.invoke(adapterInstance, reader);
    assertNotNull(result);
    assertTrue(result instanceof Date);

    when(reader.nextString()).thenReturn("invalid-date-string");

    Method readMethod = clazz.getMethod("read", JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("invalid-date-string");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      try {
        readMethod.invoke(adapterInstance, reader);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  void testToString_containsSimpleName() throws Exception {
    setUpFixed();

    Method toStringMethod = clazz.getDeclaredMethod("toString");
    toStringMethod.setAccessible(true);
    String str = (String) toStringMethod.invoke(adapterInstance);
    assertTrue(str.contains("DefaultDateTypeAdapter"));
  }
}