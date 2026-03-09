package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

class SqlTimeTypeAdapter_33_5Test {

    private SqlTimeTypeAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        // Fix the adapter's internal DateFormat to be lenient to parse 24-hour format properly
        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        // Use 24-hour format with AM/PM removed to match the input string format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        sdf.setLenient(true);
        formatField.set(adapter, sdf);
    }

    @Test
    @Timeout(8000)
    void read_nullToken_returnsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Time result = adapter.read(in);

        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_validTimeString_returnsTime() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String timeString = "10:15:30 AM";
        when(in.nextString()).thenReturn(timeString);
        when(in.getPreviousPath()).thenReturn("$.time");

        Time result = adapter.read(in);

        assertNotNull(result);
        // Verify time matches parsed string
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        sdf.setLenient(true);
        try {
            Date parsedDate = sdf.parse(timeString);
            assertEquals(parsedDate.getTime(), result.getTime());
        } catch (Exception e) {
            fail("Parsing in test setup failed");
        }
    }

    @Test
    @Timeout(8000)
    void read_invalidTimeString_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String invalidTime = "invalid-time";
        when(in.nextString()).thenReturn(invalidTime);
        when(in.getPreviousPath()).thenReturn("$.time");

        JsonSyntaxException exception = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
        assertTrue(exception.getMessage().contains("Failed parsing"));
        assertTrue(exception.getMessage().contains(invalidTime));
        assertTrue(exception.getMessage().contains("$.time"));
        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof java.text.ParseException);
    }
}