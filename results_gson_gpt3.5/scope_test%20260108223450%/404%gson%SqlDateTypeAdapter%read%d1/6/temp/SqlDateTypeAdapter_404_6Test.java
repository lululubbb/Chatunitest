package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_404_6Test {

    private SqlDateTypeAdapter adapter;
    private JsonReader jsonReader;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();
        jsonReader = mock(JsonReader.class);

        // Set the format field to a SimpleDateFormat with Locale.US to ensure parsing succeeds
        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat format = new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US);
        formatField.set(adapter, format);
    }

    @Test
    @Timeout(8000)
    void read_NullToken_ReturnsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(jsonReader).nextNull();

        java.sql.Date result = adapter.read(jsonReader);

        assertNull(result);
        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).nextNull();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void read_ValidDateString_ReturnsSqlDate() throws Exception {
        String dateString = "Jan 1, 2020";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(dateString);

        java.sql.Date result = adapter.read(jsonReader);

        assertNotNull(result);
        // Check that the returned date matches the parsed date string
        DateFormat format = new SimpleDateFormat("MMM d, yyyy", java.util.Locale.US);
        java.util.Date parsedDate = format.parse(dateString);
        assertEquals(new java.sql.Date(parsedDate.getTime()), result);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).nextString();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void read_InvalidDateString_ThrowsJsonSyntaxException() throws IOException {
        String invalidDate = "invalid-date";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(invalidDate);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
        assertTrue(thrown.getMessage().contains("Failed parsing '" + invalidDate + "' as SQL Date; at path $.date"));
        assertNotNull(thrown.getCause());
        assertEquals(java.text.ParseException.class, thrown.getCause().getClass());

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).nextString();
        inOrder.verify(jsonReader).getPreviousPath();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void read_ConcurrentAccess_SynchronizedFormatParse() throws Exception {
        // This test checks that the format.parse is synchronized by invoking read concurrently.
        // Due to complexity, we test synchronization indirectly by reflection on the format field.

        Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat format = (DateFormat) formatField.get(adapter);

        // Spy on the DateFormat to verify parse is called
        DateFormat spyFormat = spy(format);
        formatField.set(adapter, spyFormat);

        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("Feb 29, 2020"); // valid leap day date

        java.sql.Date result = adapter.read(jsonReader);

        assertNotNull(result);
        verify(spyFormat, times(1)).parse("Feb 29, 2020");
    }
}