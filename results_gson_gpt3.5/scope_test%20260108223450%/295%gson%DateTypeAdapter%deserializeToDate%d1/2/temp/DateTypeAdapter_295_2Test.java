package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.internal.bind.util.ISO8601Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateTypeAdapter_295_2Test {

    private DateTypeAdapter dateTypeAdapter;
    private Method deserializeToDateMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        dateTypeAdapter = new DateTypeAdapter();
        deserializeToDateMethod = DateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_ParseWithDateFormatSuccessfully() throws Throwable {
        // Arrange
        JsonReader jsonReader = mock(JsonReader.class);
        String dateString = "2023-06-01";
        when(jsonReader.nextString()).thenReturn(dateString);

        // Add a DateFormat that will parse successfully
        DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        List<DateFormat> dateFormats = getDateFormatsList(dateTypeAdapter);
        dateFormats.clear();
        dateFormats.add(dateFormat);

        // Act
        Date result = invokeDeserializeToDate(jsonReader);

        // Assert
        assertNotNull(result);
        assertEquals(dateString, new java.text.SimpleDateFormat("yyyy-MM-dd").format(result));
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_ParseWithDateFormatFails_ThenISO8601ParsesSuccessfully() throws Throwable {
        // Arrange
        JsonReader jsonReader = mock(JsonReader.class);
        String iso8601DateString = "2023-06-01T12:34:56Z";
        when(jsonReader.nextString()).thenReturn(iso8601DateString);

        // Add a DateFormat that will always fail
        DateFormat failingDateFormat = new DateFormat() {
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition fieldPosition) {
                return null;
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                return null; // Return null to indicate failure to parse
            }
        };
        List<DateFormat> dateFormats = getDateFormatsList(dateTypeAdapter);
        dateFormats.clear();
        dateFormats.add(failingDateFormat);

        // Act
        Date result = invokeDeserializeToDate(jsonReader);

        // Assert
        assertNotNull(result);
        Date expected = ISO8601Utils.parse(iso8601DateString, new ParsePosition(0));
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_ParseWithDateFormatFails_AndISO8601Fails_ThrowsJsonSyntaxException() throws Throwable {
        // Arrange
        JsonReader jsonReader = mock(JsonReader.class);
        String invalidDateString = "invalid-date";
        when(jsonReader.nextString()).thenReturn(invalidDateString);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        // Add a DateFormat that will always fail
        DateFormat failingDateFormat = new DateFormat() {
            @Override
            public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition fieldPosition) {
                return null;
            }

            @Override
            public Date parse(String source, ParsePosition pos) {
                return null; // Return null to indicate failure to parse
            }
        };
        List<DateFormat> dateFormats = getDateFormatsList(dateTypeAdapter);
        dateFormats.clear();
        dateFormats.add(failingDateFormat);

        // Mock ISO8601Utils.parse to throw ParseException by using a spy or wrapper
        // Since ISO8601Utils.parse is static and final, we cannot mock it easily.
        // Instead, rely on invalid input causing it to throw ParseException.

        // Act & Assert
        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            invokeDeserializeToDate(jsonReader);
        });
        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains("$.date"));
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof ParseException);
    }

    private Date invokeDeserializeToDate(JsonReader jsonReader) throws Throwable {
        try {
            return (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    private List<DateFormat> getDateFormatsList(DateTypeAdapter adapter) throws Exception {
        java.lang.reflect.Field field = DateTypeAdapter.class.getDeclaredField("dateFormats");
        field.setAccessible(true);
        return (List<DateFormat>) field.get(adapter);
    }
}