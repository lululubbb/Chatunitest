package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

public class UtcDateTypeAdapter_160_6Test {

    private UtcDateTypeAdapter adapter;
    private JsonReader jsonReader;

    @BeforeEach
    public void setup() {
        adapter = new UtcDateTypeAdapter();
        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void testRead_NullToken_ReturnsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        // nextNull() is void, just verify it is called
        doNothing().when(jsonReader).nextNull();

        Date result = adapter.read(jsonReader);

        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_ValidDateString_ReturnsParsedDate() throws Exception {
        String dateStr = "2023-06-01T12:34:56Z";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(dateStr);

        // Use reflection to invoke private static parse method to get expected Date
        Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
        parseMethod.setAccessible(true);
        Date expectedDate = (Date) parseMethod.invoke(null, dateStr, new ParsePosition(0));

        Date actualDate = adapter.read(jsonReader);

        assertNotNull(actualDate);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    @Timeout(8000)
    public void testRead_ParseException_ThrowsJsonParseException() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("invalid-date-string");

        // Because parse is private static, we cannot mock it directly.
        // Instead, we test that read throws JsonParseException for invalid input.

        try {
            adapter.read(jsonReader);
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            assertTrue(e.getCause() instanceof ParseException);
        }
    }
}