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
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateTypeAdapter_295_4Test {

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
    public void testDeserializeToDate_withValidDateFormat() throws Exception {
        // Arrange
        JsonReader jsonReader = mock(JsonReader.class);
        String testDateString = "2023-06-01";
        when(jsonReader.nextString()).thenReturn(testDateString);

        // Add a DateFormat that can parse the testDateString
        DateFormat mockDateFormat = mock(DateFormat.class);
        Date expectedDate = new Date();
        when(mockDateFormat.parse(testDateString)).thenReturn(expectedDate);

        // Inject mockDateFormat into the dateFormats list using reflection
        List<DateFormat> dateFormats = getDateFormatsList();
        dateFormats.clear();
        dateFormats.add(mockDateFormat);

        // Act
        Date result = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);

        // Assert
        assertEquals(expectedDate, result);
        verify(jsonReader).nextString();
        verify(mockDateFormat).parse(testDateString);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withDateFormatThrowsParseException_andIso8601Parses() throws Exception {
        // Arrange
        JsonReader jsonReader = mock(JsonReader.class);
        String testDateString = "2023-06-01T12:34:56Z";
        when(jsonReader.nextString()).thenReturn(testDateString);

        // Add a DateFormat that throws ParseException
        DateFormat mockDateFormat = mock(DateFormat.class);
        when(mockDateFormat.parse(testDateString)).thenThrow(ParseException.class);

        List<DateFormat> dateFormats = getDateFormatsList();
        dateFormats.clear();
        dateFormats.add(mockDateFormat);

        // Spy on ISO8601Utils to verify parse is called
        Date expectedDate = new Date();
        // Since ISO8601Utils.parse is static final, we cannot mock it easily.
        // Instead, we rely on actual parsing, so testDateString must be a valid ISO8601 date.
        // The string "2023-06-01T12:34:56Z" is valid ISO8601.

        // Act
        Date result = (Date) deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDate.getClass(), result.getClass());
        verify(jsonReader).nextString();
        verify(mockDateFormat).parse(testDateString);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_withAllParseFailures_throwsJsonSyntaxException() throws Exception {
        // Arrange
        JsonReader jsonReader = mock(JsonReader.class);
        String testDateString = "invalid-date";
        when(jsonReader.nextString()).thenReturn(testDateString);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        // Add a DateFormat that throws ParseException
        DateFormat mockDateFormat = mock(DateFormat.class);
        when(mockDateFormat.parse(testDateString)).thenThrow(ParseException.class);

        List<DateFormat> dateFormats = getDateFormatsList();
        dateFormats.clear();
        dateFormats.add(mockDateFormat);

        // Act & Assert
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            deserializeToDateMethod.invoke(dateTypeAdapter, jsonReader);
        });
        Throwable cause = thrown.getCause();
        assertTrue(cause instanceof JsonSyntaxException);
        assertTrue(cause.getMessage().contains("Failed parsing 'invalid-date' as Date; at path $.date"));
        verify(jsonReader).nextString();
        verify(mockDateFormat).parse(testDateString);
        verify(jsonReader).getPreviousPath();
    }

    private List<DateFormat> getDateFormatsList() throws Exception {
        // Use reflection to access the private final List<DateFormat> dateFormats field
        java.lang.reflect.Field dateFormatsField = DateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        //noinspection unchecked
        return (List<DateFormat>) dateFormatsField.get(dateTypeAdapter);
    }
}