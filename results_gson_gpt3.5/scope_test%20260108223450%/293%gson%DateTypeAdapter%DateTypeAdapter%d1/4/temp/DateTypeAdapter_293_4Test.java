package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTypeAdapter_293_4Test {

    private DateTypeAdapter adapter;

    @BeforeEach
    public void setUp() {
        adapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    public void testConstructor_dateFormatsInitialized() throws Exception {
        // Access private field dateFormats via reflection
        var field = DateTypeAdapter.class.getDeclaredField("dateFormats");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        var dateFormats = (List<DateFormat>) field.get(adapter);

        // Should contain at least one DateFormat (US)
        assertFalse(dateFormats.isEmpty());
        // The first DateFormat should be US locale
        DateFormat firstFormat = dateFormats.get(0);
        String formatted = firstFormat.format(new Date(0));
        assertNotNull(formatted);
        // If default locale not US, second format should be default locale
        if (!Locale.getDefault().equals(Locale.US)) {
            assertTrue(dateFormats.size() >= 2);
        }
        // If Java 9 or later, there should be a third format
        // We cannot test JavaVersion.isJava9OrLater() directly here, but ensure list size >=1
        assertTrue(dateFormats.size() >= 1);
    }

    @Test
    @Timeout(8000)
    public void testRead_nullValue() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(in).nextNull();

        Date result = adapter.read(in);
        assertNull(result);

        verify(in).peek();
        verify(in).nextNull();
    }

    @Test
    @Timeout(8000)
    public void testRead_validDate() throws Exception {
        Date now = new Date();
        String formattedDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(now);

        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn(formattedDate);

        Date result = adapter.read(in);
        assertNotNull(result);
        // The parsed date should be close to now (same day)
        assertEquals(now.getYear(), result.getYear());
        assertEquals(now.getMonth(), result.getMonth());
        assertEquals(now.getDate(), result.getDate());

        verify(in).peek();
        verify(in).nextString();
    }

    @Test
    @Timeout(8000)
    public void testRead_invalidDate_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("invalid-date-string");

        assertThrows(JsonSyntaxException.class, () -> adapter.read(in));

        verify(in).peek();
        verify(in).nextString();
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);

        adapter.write(out, null);

        verify(out).nullValue();
    }

    @Test
    @Timeout(8000)
    public void testWrite_validDate() throws IOException {
        Date date = new Date(0L);
        JsonWriter out = mock(JsonWriter.class);

        adapter.write(out, date);

        verify(out).value(anyString());
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_successAndFailure() throws Exception {
        JsonReader in = mock(JsonReader.class);

        // Access private method deserializeToDate via reflection
        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        // Setup for valid date string
        Date now = new Date();
        String formattedDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(now);

        when(in.nextString()).thenReturn(formattedDate);

        Date date = (Date) method.invoke(adapter, in);
        assertNotNull(date);

        // Setup for invalid date string to cause ParseException and trigger ISO8601Utils parse fallback
        when(in.nextString()).thenReturn("invalid-date-string");

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(adapter, in));
        assertTrue(thrown.getCause() instanceof JsonSyntaxException);
    }
}