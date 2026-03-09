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
import java.util.List;

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
import java.util.Locale;

class DateTypeAdapter_293_6Test {

    private DateTypeAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    void testConstructor_initializesDateFormats() {
        // The constructor adds at least one DateFormat for Locale.US
        // If default locale is not US, adds another
        // If Java9 or later, adds a third
        // Test that dateFormats list is not empty and contains DateFormat instances
        try {
            var field = DateTypeAdapter.class.getDeclaredField("dateFormats");
            field.setAccessible(true);
            var dateFormats = (java.util.List<DateFormat>) field.get(adapter);
            assertNotNull(dateFormats);
            assertTrue(dateFormats.size() >= 1);
            for (DateFormat df : dateFormats) {
                assertNotNull(df);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e);
        }
    }

    @Test
    @Timeout(8000)
    void testRead_nullJson() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(in).nextNull();

        Date result = adapter.read(in);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testRead_validDateString() throws Exception {
        String dateString = "Jan 1, 2020, 12:00:00 AM";
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn(dateString);

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        Date expectedDate = df.parse(dateString);

        Date result = adapter.read(in);

        assertNotNull(result);
        assertEquals(expectedDate, result);
    }

    @Test
    @Timeout(8000)
    void testRead_invalidDateString_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("invalid date");

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
        assertTrue(ex.getMessage().contains("Failed parsing"));
    }

    @Test
    @Timeout(8000)
    void testRead_invalidToken_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
        // Mock nextString to throw IllegalStateException to simulate unexpected call
        when(in.nextString()).thenThrow(new IllegalStateException("Expected string but was array"));

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
        assertTrue(ex.getMessage().contains("Expected a string but was"));
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_validString() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("Jan 1, 2020, 12:00:00 AM");

        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date result = (Date) method.invoke(adapter, in);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_invalidString_throwsJsonSyntaxException() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("not a date");

        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(adapter, in));
        assertTrue(thrown.getCause() instanceof com.google.gson.JsonSyntaxException);
    }

    @Test
    @Timeout(8000)
    void testWrite_nullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        adapter.write(out, null);
        verify(out).nullValue();
    }

    @Test
    @Timeout(8000)
    void testWrite_nonNullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);

        Date date = new Date(0L);

        adapter.write(out, date);

        verify(out).value(anyString());
    }

    @Test
    @Timeout(8000)
    void testFactory_create_returnsAdapterForDate() {
        var factory = DateTypeAdapter.FACTORY;
        var gson = mock(com.google.gson.Gson.class);
        var typeTokenDate = com.google.gson.reflect.TypeToken.get(Date.class);
        var typeTokenString = com.google.gson.reflect.TypeToken.get(String.class);

        var adapterForDate = factory.create(gson, typeTokenDate);
        assertNotNull(adapterForDate);
        assertTrue(adapterForDate instanceof DateTypeAdapter);

        var adapterForString = factory.create(gson, typeTokenString);
        assertNull(adapterForString);
    }

}