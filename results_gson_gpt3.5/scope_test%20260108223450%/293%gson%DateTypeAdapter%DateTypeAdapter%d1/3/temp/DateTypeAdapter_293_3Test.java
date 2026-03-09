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

class DateTypeAdapter_293_3Test {

    private DateTypeAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new DateTypeAdapter();
    }

    @Test
    @Timeout(8000)
    void testConstructor_initializesDateFormats() throws Exception {
        // Reflectively access the private dateFormats field
        var field = DateTypeAdapter.class.getDeclaredField("dateFormats");
        field.setAccessible(true);
        var dateFormats = (java.util.List<DateFormat>) field.get(adapter);

        assertNotNull(dateFormats);
        assertTrue(dateFormats.size() >= 1);
        // First date format is US locale default datetime instance
        DateFormat first = dateFormats.get(0);
        assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(new Date()), first.format(new Date()));

        // If default locale is not US, second date format should be default locale
        if (!Locale.getDefault().equals(Locale.US)) {
            assertTrue(dateFormats.size() >= 2);
            DateFormat second = dateFormats.get(1);
            assertEquals(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT).format(new Date()), second.format(new Date()));
        }
    }

    @Test
    @Timeout(8000)
    void testRead_nullValue() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(in).nextNull();

        Date result = adapter.read(in);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testRead_validDate() throws IOException {
        Date now = new Date();
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(now));

        Date result = adapter.read(in);

        assertNotNull(result);
        // The parsed date should be close to now (within a second)
        assertTrue(Math.abs(result.getTime() - now.getTime()) < 1000);
    }

    @Test
    @Timeout(8000)
    void testRead_invalidDate_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        when(in.nextString()).thenReturn("invalid-date-string");

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
        assertTrue(ex.getMessage().contains("Failed parsing"));
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
    void testWrite_validDate() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        Date date = new Date();

        adapter.write(out, date);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(out).value(captor.capture());

        String formatted = captor.getValue();
        assertNotNull(formatted);
        // The formatted string should parse back to a date close to the original
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
        try {
            Date parsed = df.parse(formatted);
            assertTrue(Math.abs(parsed.getTime() - date.getTime()) < 2000);
        } catch (ParseException e) {
            fail("Formatted date string could not be parsed back");
        }
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_validString() throws Throwable {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US).format(new Date()));

        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        Date result = (Date) method.invoke(adapter, in);

        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testDeserializeToDate_invalidString_throwsJsonSyntaxException() throws Throwable {
        JsonReader in = mock(JsonReader.class);
        when(in.nextString()).thenReturn("bad-date");

        Method method = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> method.invoke(adapter, in));
        assertTrue(thrown.getCause() instanceof com.google.gson.JsonSyntaxException);
    }

}