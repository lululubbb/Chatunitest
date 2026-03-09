package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapterDeserializeToDateTest {

    private DefaultDateTypeAdapter<Date> adapter;
    private JsonReader jsonReaderMock;

    // A minimal DateType implementation to satisfy constructor
    private static class DateType<T extends Date> {
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Use reflection to invoke private constructor DefaultDateTypeAdapter(DateType<T>, String)
        DateType<Date> dateType = new DateType<>();
        var constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DateType.class, String.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(dateType, "yyyy-MM-dd'T'HH:mm:ss'Z'");

        jsonReaderMock = mock(JsonReader.class);
    }

    private void setDateFormats(List<DateFormat> dateFormats) throws Exception {
        Field field = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        field.setAccessible(true);
        // Clear existing list and addAll to preserve synchronization on the same list instance
        List<DateFormat> existingList = (List<DateFormat>) field.get(adapter);
        existingList.clear();
        existingList.addAll(dateFormats);
    }

    private Date callDeserializeToDate(JsonReader in) throws Exception {
        Method method = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        method.setAccessible(true);
        return (Date) method.invoke(adapter, in);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_parsesWithDateFormat() throws Exception {
        String dateStr = "2023-06-01T12:34:56Z";

        when(jsonReaderMock.nextString()).thenReturn(dateStr);

        // Setup a DateFormat that can parse the date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setLenient(false);
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(sdf);
        setDateFormats(dateFormats);

        Date parsedDate = callDeserializeToDate(jsonReaderMock);

        assertNotNull(parsedDate);
        assertEquals(sdf.parse(dateStr), parsedDate);
        verify(jsonReaderMock).nextString();
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_fallbackToISO8601Utils() throws Exception {
        String dateStr = "2023-06-01T12:34:56.789Z";
        when(jsonReaderMock.nextString()).thenReturn(dateStr);

        // Setup a DateFormat that always throws ParseException
        DateFormat badFormat = mock(DateFormat.class);
        when(badFormat.parse(dateStr)).thenThrow(new ParseException("fail", 0));
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(badFormat);
        setDateFormats(dateFormats);

        Date expectedDate = ISO8601Utils.parse(dateStr, new ParsePosition(0));
        Date actualDate = callDeserializeToDate(jsonReaderMock);

        assertNotNull(actualDate);
        assertEquals(expectedDate, actualDate);
        verify(jsonReaderMock).nextString();
        verify(badFormat).parse(dateStr);
    }

    @Test
    @Timeout(8000)
    public void testDeserializeToDate_throwsJsonSyntaxExceptionOnParseFailure() throws Exception {
        String dateStr = "invalid-date";
        when(jsonReaderMock.nextString()).thenReturn(dateStr);
        when(jsonReaderMock.getPreviousPath()).thenReturn("$.date");

        // Setup a DateFormat that always throws ParseException
        DateFormat badFormat = mock(DateFormat.class);
        when(badFormat.parse(dateStr)).thenThrow(new ParseException("fail", 0));
        List<DateFormat> dateFormats = new ArrayList<>();
        dateFormats.add(badFormat);
        setDateFormats(dateFormats);

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> callDeserializeToDate(jsonReaderMock));
        assertTrue(thrown.getMessage().contains(dateStr));
        assertTrue(thrown.getMessage().contains("$.date"));

        verify(jsonReaderMock).nextString();
        verify(badFormat).parse(dateStr);
        verify(jsonReaderMock).getPreviousPath();
    }
}