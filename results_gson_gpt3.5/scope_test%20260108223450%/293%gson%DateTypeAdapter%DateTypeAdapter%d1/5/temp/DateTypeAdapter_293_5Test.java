package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

class DateTypeAdapter_293_5Test {

    private DateTypeAdapter dateTypeAdapter;

    @BeforeEach
    void setUp() {
        dateTypeAdapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    void testConstructor_initializesDateFormats() throws Exception {
        // Access private field dateFormats via reflection
        var field = DateTypeAdapter.class.getDeclaredField("dateFormats");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        var dateFormats = (java.util.List<DateFormat>) field.get(dateTypeAdapter);

        assertFalse(dateFormats.isEmpty());
        // First format should be US locale default DateTime instance
        DateFormat usFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        assertEquals(usFormat.getClass(), dateFormats.get(0).getClass());

        if (!Locale.getDefault().equals(Locale.US)) {
            assertTrue(dateFormats.size() >= 2);
        }
    }

    @Test
    @Timeout(8000)
    void testRead_nullJson_returnsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(in).nextNull();

        Date result = dateTypeAdapter.read(in);

        assertNull(result);
        verify(in).nextNull();
    }

    @Test
    @Timeout(8000)
    void testRead_validDateString() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("Jan 1, 2020, 12:00:00 AM");

        Date result = dateTypeAdapter.read(in);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testRead_invalidDateString_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("invalid-date-string");

        assertThrows(com.google.gson.JsonSyntaxException.class, () -> dateTypeAdapter.read(in));
    }

    @Test
    @Timeout(8000)
    void testWrite_nullDate_writesNull() throws IOException {
        JsonWriter out = mock(JsonWriter.class);

        dateTypeAdapter.write(out, null);

        verify(out).nullValue();
    }

    @Test
    @Timeout(8000)
    void testWrite_validDate_writesString() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        Date date = new Date(0L);

        dateTypeAdapter.write(out, date);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(out).value(captor.capture());
        String written = captor.getValue();
        assertNotNull(written);
        assertFalse(written.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_validDateString() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("Jan 1, 2020, 12:00:00 AM");

        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date date = (Date) method.invoke(dateTypeAdapter, in);

        assertNotNull(date);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_invalidDateString_throwsJsonSyntaxException() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("not-a-date");

        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        assertThrows(com.google.gson.JsonSyntaxException.class, () -> {
            try {
                method.invoke(dateTypeAdapter, in);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }
}