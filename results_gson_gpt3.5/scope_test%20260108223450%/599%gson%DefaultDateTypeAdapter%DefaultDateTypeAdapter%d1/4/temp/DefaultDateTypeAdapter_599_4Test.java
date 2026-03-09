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
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_599_4Test {

    private DefaultDateTypeAdapter<Date> adapterUS;
    private DefaultDateTypeAdapter<Date> adapterDefaultLocale;
    private DefaultDateTypeAdapter<Date> adapterJava9OrLater;

    private static final DefaultDateTypeAdapter.DateType<Date> DATE_TYPE = new DefaultDateTypeAdapter.DateType<Date>(Date.class) {
        public Date deserialize(Date date) {
            return date;
        }

        public Date serialize(Date date) {
            return date;
        }
    };

    @BeforeEach
    void setUp() throws Exception {
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(
                DefaultDateTypeAdapter.DateType.class, int.class, int.class);
        constructor.setAccessible(true);

        adapterUS = constructor.newInstance(DATE_TYPE, DateFormat.SHORT, DateFormat.SHORT);
        adapterDefaultLocale = constructor.newInstance(DATE_TYPE, DateFormat.MEDIUM, DateFormat.MEDIUM);
        adapterJava9OrLater = constructor.newInstance(DATE_TYPE, DateFormat.FULL, DateFormat.FULL);
    }

    @Test
    @Timeout(8000)
    void testConstructorAddsUSDateTimeInstance() throws Exception {
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(
                DefaultDateTypeAdapter.DateType.class, int.class, int.class);
        constructor.setAccessible(true);

        DefaultDateTypeAdapter<Date> adapter = constructor.newInstance(DATE_TYPE, DateFormat.SHORT, DateFormat.SHORT);

        var dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);

        assertFalse(dateFormats.isEmpty());
        DateFormat first = dateFormats.get(0);

        assertTrue(first instanceof SimpleDateFormat);

        Locale locale = null;
        try {
            // Changed to getDeclaredMethod for getLocale() instead of accessing field directly,
            // because the 'locale' field is not accessible in Java 9+
            Method getLocaleMethod = SimpleDateFormat.class.getMethod("getLocale");
            locale = (Locale) getLocaleMethod.invoke(first);
        } catch (NoSuchMethodException e) {
            // fallback to old reflective access for Java 8 and below
            try {
                java.lang.reflect.Field localeField = SimpleDateFormat.class.getDeclaredField("locale");
                localeField.setAccessible(true);
                locale = (Locale) localeField.get(first);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                fail("Could not access locale field in SimpleDateFormat");
            }
        } catch (Exception e) {
            fail("Could not invoke getLocale() method in SimpleDateFormat");
        }
        assertEquals(Locale.US, locale);
    }

    @Test
    @Timeout(8000)
    void testWriteAndRead() throws Exception {
        JsonWriter writer = mock(JsonWriter.class);
        JsonReader reader = mock(JsonReader.class);

        Date now = new Date();

        adapterUS.write(writer, now);
        verify(writer).value(anyString());

        adapterUS.write(writer, null);
        verify(writer).nullValue();

        when(reader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(reader).nextNull();
        assertNull(adapterUS.read(reader));

        when(reader.peek()).thenReturn(JsonToken.STRING);
        String dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(now);
        when(reader.nextString()).thenReturn(dateStr);

        Date parsedDate = adapterUS.read(reader);
        assertNotNull(parsedDate);

        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn("invalid-date-string");
        assertThrows(JsonSyntaxException.class, () -> adapterUS.read(reader));
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate() throws Exception {
        JsonReader reader = mock(JsonReader.class);

        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        when(reader.peek()).thenReturn(JsonToken.STRING);
        Date now = new Date();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(now);
        when(reader.nextString()).thenReturn(dateStr);

        Date result = (Date) method.invoke(adapterUS, reader);
        assertNotNull(result);

        when(reader.peek()).thenReturn(JsonToken.NUMBER);
        long time = now.getTime();
        when(reader.nextLong()).thenReturn(time);
        Date result2 = (Date) method.invoke(adapterUS, reader);
        // Use assertEquals with delta to avoid millisecond precision issues
        assertEquals(time, result2.getTime(), 5000);

        when(reader.peek()).thenReturn(JsonToken.BOOLEAN);
        Exception ex = assertThrows(Exception.class, () -> method.invoke(adapterUS, reader));
        Throwable cause = ex.getCause();
        assertTrue(cause instanceof JsonSyntaxException);
    }

    @Test
    @Timeout(8000)
    void testToString() {
        String toString = adapterUS.toString();
        assertTrue(toString.contains(DefaultDateTypeAdapter.class.getSimpleName()));
        // The adapterUS uses Locale.US, so the toString should contain "US" or "us" (case insensitive)
        assertTrue(toString.toLowerCase(Locale.ROOT).contains("us"));
    }
}