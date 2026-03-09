package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultDateTypeAdapter_598_6Test {

  private DefaultDateTypeAdapter<Date> adapter;
  private DateFormat usDateFormat;
  private DateFormat defaultDateFormat;

  interface DateType<T extends Date> {
    T deserialize(Date date);
    Date serialize(T value);
  }

  @BeforeEach
  void setUp() throws Exception {
    Class<DefaultDateTypeAdapter> clazz = DefaultDateTypeAdapter.class;

    // The nested DateType class is a static class, not an interface, so we cannot use Proxy.
    // Instead, create an anonymous subclass of DateType with the expected methods.

    // Find the nested DateType class
    Class<?> dateTypeClass = null;
    for (Class<?> innerClass : clazz.getDeclaredClasses()) {
      if ("DateType".equals(innerClass.getSimpleName())) {
        dateTypeClass = innerClass;
        break;
      }
    }
    assertNotNull(dateTypeClass, "DateType class not found");

    // Create a subclass instance of DateType with the required methods implemented
    Object dateTypeInstance = new Object() {
      public Date deserialize(Date date) {
        return date;
      }

      public Date serialize(Date value) {
        return value;
      }
    };

    // We need to create a proxy or subclass of dateTypeClass that implements the methods.
    // Because dateTypeClass is a class, not interface, use a dynamic subclass via Proxy is not possible.
    // Use a dynamic proxy via bytecode or reflection is complicated here.
    // Instead, use a dynamic subclass via reflection and anonymous class.

    // Use a helper class extending DateType<Date>
    Object dateType = java.lang.reflect.Proxy.isProxyClass(dateTypeClass) ? null : null;

    // Alternative: create a subclass by reflection
    // dateTypeClass has a public constructor? Try to find a no-arg constructor
    Constructor<?> dateTypeConstructor = null;
    try {
      dateTypeConstructor = dateTypeClass.getDeclaredConstructor();
      dateTypeConstructor.setAccessible(true);
    } catch (NoSuchMethodException e) {
      // no no-arg constructor
    }

    Object dateTypeObj;
    if (dateTypeConstructor != null) {
      // instantiate and override methods via proxy? But it's a class, not interface
      // We can use a dynamic subclass using java.lang.reflect.Proxy only if it's interface, so no
      // Instead, create a subclass via anonymous class extending dateTypeClass
      dateTypeObj = new Object();
      // But we cannot instantiate anonymous subclass via reflection easily
      // So use a dynamic subclass with a helper class extending dateTypeClass

      // Create a subclass in code:
      // We can define a helper class inside this test:

      class DateTypeImpl extends DefaultDateTypeAdapter.DateType<Date> {
        @Override
        public Date deserialize(Date date) {
          return date;
        }

        @Override
        public Date serialize(Date value) {
          return value;
        }
      }

      dateTypeObj = new DateTypeImpl();
    } else {
      // no no-arg constructor, try to find constructor with parameters
      // fallback: use the nested interface declared in this test (our own DateType interface)
      // but constructor expects DefaultDateTypeAdapter.DateType, not our interface
      // so try to create a dynamic proxy for the class's interfaces

      // Get the interfaces implemented by dateTypeClass
      Class<?>[] interfaces = dateTypeClass.getInterfaces();
      if (interfaces.length == 0) {
        // no interfaces to proxy, fail
        throw new IllegalStateException("DateType class is not interface and has no interfaces");
      }

      Object proxy = java.lang.reflect.Proxy.newProxyInstance(
          dateTypeClass.getClassLoader(),
          interfaces,
          (proxy1, method, args) -> {
            String name = method.getName();
            if ("deserialize".equals(name)) {
              return args[0];
            } else if ("serialize".equals(name)) {
              return args[0];
            }
            return null;
          });
      dateTypeObj = proxy;
    }

    // Actually, the above is complicated. The simplest way is to create a subclass of DateType<Date> inside this test.
    // So let's redefine the setUp method to do that:

  }

  @BeforeEach
  void setUp() throws Exception {
    Class<DefaultDateTypeAdapter> clazz = DefaultDateTypeAdapter.class;

    // Find the nested DateType class
    Class<?> dateTypeClass = null;
    for (Class<?> innerClass : clazz.getDeclaredClasses()) {
      if ("DateType".equals(innerClass.getSimpleName())) {
        dateTypeClass = innerClass;
        break;
      }
    }
    assertNotNull(dateTypeClass, "DateType class not found");

    // Create a subclass of DateType<Date> with implemented methods
    // Use a dynamic subclass via reflection and anonymous class

    Object dateTypeInstance = new Object() {
      @SuppressWarnings("unused")
      public Date deserialize(Date date) {
        return date;
      }

      @SuppressWarnings("unused")
      public Date serialize(Date value) {
        return value;
      }
    };

    // But the constructor expects an instance of DateType<T>, so create an instance of the nested DateType class.
    // The nested DateType class is abstract, so we can create an anonymous subclass via reflection:

    Object dateType = java.lang.reflect.Proxy.newProxyInstance(
        dateTypeClass.getClassLoader(),
        new Class<?>[] {dateTypeClass},
        (proxy, method, args) -> {
          String name = method.getName();
          if ("deserialize".equals(name)) {
            return args[0];
          } else if ("serialize".equals(name)) {
            return args[0];
          }
          return null;
        });

    // This fails because dateTypeClass is not interface, so Proxy cannot be used.

    // So, instead, create a subclass dynamically:

    // Use a helper class inside the test:
    class DateTypeImpl extends DefaultDateTypeAdapter.DateType<Date> {
      @Override
      public Date deserialize(Date date) {
        return date;
      }

      @Override
      public Date serialize(Date value) {
        return value;
      }
    }

    // Create instance of DateTypeImpl
    Object dateTypeObj = new DateTypeImpl();

    // Get the constructor of DefaultDateTypeAdapter that takes DateType and int
    Constructor<DefaultDateTypeAdapter> constructor = clazz.getDeclaredConstructor(dateTypeClass, int.class);
    constructor.setAccessible(true);

    adapter = constructor.newInstance(dateTypeObj, DateFormat.SHORT);

    var dateFormatsField = clazz.getDeclaredField("dateFormats");
    dateFormatsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

    usDateFormat = dateFormats.get(0);
    if (dateFormats.size() > 1) {
      defaultDateFormat = dateFormats.get(1);
    }
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue_writesNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    adapter.write(writer, null);
    verify(writer).nullValue();
    verifyNoMoreInteractions(writer);
  }

  @Test
    @Timeout(8000)
  void testWrite_validDate_writesFormattedString() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Date date = new Date(0L);
    adapter.write(writer, date);
    verify(writer).value(anyString());
  }

  @Test
    @Timeout(8000)
  void testRead_nullJson_returnsNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    adapter.read(reader);
    verify(reader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testRead_validFormattedDate_returnsDate() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    String formattedDate = usDateFormat.format(new Date(0L));
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(formattedDate);

    Date result = adapter.read(reader);
    assertNotNull(result);
    assertEquals(usDateFormat.parse(formattedDate), result);
  }

  @Test
    @Timeout(8000)
  void testRead_invalidFormattedDate_throwsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("invalid-date");

    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_validDateString() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    String validDate = usDateFormat.format(new Date(0L));

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(validDate);

    Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    Date date = (Date) method.invoke(adapter, reader);
    assertEquals(usDateFormat.parse(validDate), date);
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_nullValue() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    Date date = (Date) method.invoke(adapter, reader);
    assertNull(date);
    verify(reader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testToString_containsClassName() {
    String str = adapter.toString();
    assertTrue(str.contains("DefaultDateTypeAdapter"));
  }
}