package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Locale;

class SqlDateTypeAdapter_404_2Test {

    private SqlDateTypeAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to create instance because constructor is private
        var constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        // Set the private 'format' field to a SimpleDateFormat that matches the test date string with Locale.US
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(adapter, new SimpleDateFormat("MMM d, yyyy", Locale.US));
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        when(reader.peek()).thenReturn(JsonToken.NULL);

        java.sql.Date result = adapter.read(reader);

        InOrder inOrder = inOrder(reader);
        inOrder.verify(reader).peek();
        inOrder.verify(reader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnSqlDate_whenValidDateString() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        String dateString = "Jan 1, 2020";
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(dateString);
        when(reader.getPreviousPath()).thenReturn(null);

        java.sql.Date result = adapter.read(reader);

        assertNotNull(result);
        // Verify the date matches the parsed string
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        java.util.Date expectedDate = sdf.parse(dateString);
        assertEquals(expectedDate.getTime(), result.getTime());
    }

    @Test
    @Timeout(8000)
    void read_shouldThrowJsonSyntaxException_whenParseException() throws IOException {
        JsonReader reader = mock(JsonReader.class);
        String invalidDateString = "invalid-date";
        when(reader.peek()).thenReturn(JsonToken.STRING);
        when(reader.nextString()).thenReturn(invalidDateString);
        when(reader.getPreviousPath()).thenReturn("$.dateField");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));

        String expectedMessage = "Failed parsing '" + invalidDateString + "' as SQL Date; at path $.dateField";
        assertTrue(exception.getMessage().contains(expectedMessage));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof java.text.ParseException);
    }
}