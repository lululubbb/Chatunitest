package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class SqlTimeTypeAdapter_33_1Test {

    private SqlTimeTypeAdapter adapter;
    private JsonReader jsonReader;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();
        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        Time result = adapter.read(jsonReader);

        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnTime_whenJsonStringIsValid() throws IOException, NoSuchFieldException, IllegalAccessException, ParseException {
        String timeString = "11:22:33";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(timeString);

        // Access private field 'format' to parse expected time
        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        DateFormat format = (DateFormat) formatField.get(adapter);

        Date parsedDate;
        synchronized (adapter) {
            parsedDate = format.parse(timeString);
        }
        Time expectedTime = new Time(parsedDate.getTime());

        Time result = adapter.read(jsonReader);

        assertEquals(expectedTime, result);
    }

    @Test
    @Timeout(8000)
    void read_shouldThrowJsonSyntaxException_whenParseExceptionOccurs() throws IOException {
        String invalidTimeString = "invalid-time";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(invalidTimeString);
        when(jsonReader.getPreviousPath()).thenReturn("$.time");

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));

        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains(invalidTimeString));
        assertTrue(thrown.getMessage().contains("$.time"));
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof ParseException);
    }
}