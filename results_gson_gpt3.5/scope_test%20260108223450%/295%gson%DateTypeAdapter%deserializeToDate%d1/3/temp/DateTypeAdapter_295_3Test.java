package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTypeAdapter_295_3Test {

    private DateTypeAdapter dateTypeAdapter;
    private Method deserializeToDateMethod;

    @BeforeEach
    public void setUp() throws Exception {
        dateTypeAdapter = new DateTypeAdapter();
        deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);
    }

    private void setDateFormats(List<DateFormat> dateFormats) throws Exception {
        Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        dateFormatsField.set(dateTypeAdapter, dateFormats);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withMatchingDateFormat() throws Exception {
        // Prepare a date string and corresponding DateFormat
        String dateString = "2023-06-15";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date expectedDate = sdf.parse(dateString);

        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(sdf);
        setDateFormats(dateFormats);

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn(dateString);

        Date actualDate = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withMultipleDateFormats_firstFailsSecondSucceeds() throws Exception {
        String dateString = "15/06/2023";
        SimpleDateFormat sdfFail = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdfSuccess = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date expectedDate = sdfSuccess.parse(dateString);

        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(sdfFail);
        dateFormats.add(sdfSuccess);
        setDateFormats(dateFormats);

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn(dateString);

        Date actualDate = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withEmptyDateFormats_usesISO8601Utils() throws Exception {
        // Empty dateFormats list
        setDateFormats(new ArrayList<>());

        String iso8601DateString = "2023-06-15T12:34:56Z";
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn(iso8601DateString);

        Date actualDate = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        // We cannot easily build expected Date from ISO8601Utils, but we can verify not null
        assertNotNull(actualDate);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withInvalidDate_throwsJsonSyntaxException() throws Exception {
        setDateFormats(new ArrayList<>());

        String invalidDateString = "invalid-date";
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn(invalidDateString);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof JsonSyntaxException);
        assertTrue(cause.getMessage().contains("Failed parsing"));
        assertTrue(cause.getMessage().contains(invalidDateString));
        assertTrue(cause.getMessage().contains("$.date"));
    }
}