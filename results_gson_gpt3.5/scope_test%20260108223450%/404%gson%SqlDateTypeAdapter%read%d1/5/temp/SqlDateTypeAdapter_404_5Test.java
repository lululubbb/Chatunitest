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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

class SqlDateTypeAdapter_404_5Test {

    private SqlDateTypeAdapter adapter;
    private JsonReader jsonReader;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Use reflection to access the private constructor
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void read_nullToken_returnsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        java.sql.Date result = adapter.read(jsonReader);

        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_validDateString_returnsSqlDate() throws Exception {
        String dateString = "Jan 1, 2020";  // Single space between Jan and 1
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(dateString);

        java.sql.Date result = adapter.read(jsonReader);

        assertNotNull(result);
        // Check the date value corresponds to the parsed string
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        long expectedTime = sdf.parse(dateString).getTime();
        assertEquals(expectedTime, result.getTime());
    }

    @Test
    @Timeout(8000)
    void read_invalidDateString_throwsJsonSyntaxException() throws IOException {
        String invalidDate = "invalid-date";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(invalidDate);
        when(jsonReader.getPreviousPath()).thenReturn("$.date");

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains(invalidDate));
        assertTrue(thrown.getMessage().contains("$.date"));
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof java.text.ParseException);
    }
}