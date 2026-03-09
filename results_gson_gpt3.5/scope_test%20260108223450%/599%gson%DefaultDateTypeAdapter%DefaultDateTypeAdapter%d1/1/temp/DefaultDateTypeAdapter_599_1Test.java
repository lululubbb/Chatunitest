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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_599_1Test {

  // Define DateType interface as it is missing from imports
  private interface DateType<T> {
    T deserialize(T date);
    T serialize(T date);
  }

  private DefaultDateTypeAdapter<Date> adapter;
  private DateType<Date> dateTypeMock;

  @BeforeEach
  void setUp() throws Exception {
    dateTypeMock = new DateType<Date>() {
      @Override
      public Date deserialize(Date date) {
        return date;
      }

      @Override
      public Date serialize(Date date) {
        return date;
      }
    };
    // Use reflection to find the private constructor with signature: DateType, int, int
    Constructor<?> ctor = null;
    for (Constructor<?> c : DefaultDateTypeAdapter.class.getDeclaredConstructors()) {
      Class<?>[] params = c.getParameterTypes();
      if (params.length == 3
          && params[0].getSimpleName().equals("DateType")
          && int.class.equals(params[1])
          && int.class.equals(params[2])) {
        ctor = c;
        break;
      }
    }
    if (ctor == null) {
      // fallback: try to find constructor by parameter count and second and third int
      for (Constructor<?> c : DefaultDateTypeAdapter.class.getDeclaredConstructors()) {
        Class<?>[] params = c.getParameterTypes();
        if (params.length == 3
            && int.class.equals(params[1])
            && int.class.equals(params[2])) {
          ctor = c;
          break;
        }
      }
    }
    if (ctor == null) {
      throw new NoSuchMethodException("No matching constructor found");
    }
    ctor.setAccessible(true);

    // Fix: The first argument must be the actual instance of the DateType class declared in DefaultDateTypeAdapter
    // Our dateTypeMock is a different interface instance. We need to create a proxy or use the actual DateType class from DefaultDateTypeAdapter.
    // Since DateType is a package-private interface inside DefaultDateTypeAdapter, we cannot instantiate it directly.
    // Instead, we can use a dynamic proxy to create an instance of that interface.

    Class<?> dateTypeClass = ctor.getParameterTypes()[0];
    Object dateTypeInstance = java.lang.reflect.Proxy.newProxyInstance(
        dateTypeClass.getClassLoader(),
        new Class<?>[]{dateTypeClass},
        (proxy, method, args) -> {
          String name = method.getName();
          if ("deserialize".equals(name) && args.length == 1) {
            return args[0];
          }
          if ("serialize".equals(name) && args.length == 1) {
            return args[0];
          }
          if ("toString".equals(name) && args.length == 0) {
            return "DateTypeProxy";
          }
          return null;
        });

    adapter = (DefaultDateTypeAdapter<Date>) ctor.newInstance(dateTypeInstance, DateFormat.SHORT, DateFormat.SHORT);
  }

  @Test
    @Timeout(8000)
  void testWrite_NullValue_WritesNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    adapter.write(writer, null);
    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_ValidDate_WritesFormattedString() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Date date = new Date(0L); // Epoch

    adapter.write(writer, date);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(writer).value(captor.capture());
    String val = captor.getValue();

    boolean parsed = false;
    try {
      for (DateFormat format : getDateFormats(adapter)) {
        try {
          Date parsedDate = format.parse(val);
          if (parsedDate.equals(date)) {
            parsed = true;
            break;
          }
        } catch (Exception ignored) {
        }
      }
    } catch (Exception e) {
      fail("Exception during date format retrieval or parsing: " + e);
    }
    assertTrue(parsed, "Written string should parse back to original date");
  }

  @Test
    @Timeout(8000)
  void testRead_NullToken_ReturnsNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Date result = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_ValidDateString_ParsesDate() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    Date date = new Date(0L);
    String dateStr;
    try {
      dateStr = getDateFormats(adapter).get(0).format(date);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(dateStr);

    Date result = adapter.read(reader);

    assertNotNull(result);
    assertEquals(date, result);
  }

  @Test
    @Timeout(8000)
  void testRead_InvalidDateString_ThrowsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    String invalidDateStr = "invalid-date";

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(invalidDateStr);

    assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_ValidString() throws Exception {
    Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    JsonReader reader = mock(JsonReader.class);
    Date date = new Date(0L);
    String dateStr = getDateFormats(adapter).get(0).format(date);

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(dateStr);

    Date result = (Date) method.invoke(adapter, reader);

    assertEquals(date, result);
  }

  @Test
    @Timeout(8000)
  void testDeserializeToDate_InvalidString_ThrowsInvocationTargetExceptionCauseJsonSyntaxException() throws Exception {
    Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
    method.setAccessible(true);

    JsonReader reader = mock(JsonReader.class);
    String invalidDateStr = "not-a-date";

    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(invalidDateStr);

    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(adapter, reader));
    assertTrue(exception.getCause() instanceof JsonSyntaxException);
  }

  @Test
    @Timeout(8000)
  void testToString_NotEmpty() {
    String str = adapter.toString();
    assertNotNull(str);
    assertFalse(str.isEmpty());
    assertTrue(str.contains("DefaultDateTypeAdapter"));
  }

  // Helper method to get private dateFormats list via reflection
  @SuppressWarnings("unchecked")
  private static List<DateFormat> getDateFormats(DefaultDateTypeAdapter<?> adapter) throws Exception {
    var field = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
    field.setAccessible(true);
    return (List<DateFormat>) field.get(adapter);
  }
}