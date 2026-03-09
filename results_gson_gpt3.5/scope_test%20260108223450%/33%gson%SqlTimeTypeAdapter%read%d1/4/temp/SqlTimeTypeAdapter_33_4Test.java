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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class SqlTimeTypeAdapter_33_4Test {

    private SqlTimeTypeAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to instantiate private constructor
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();
    }

    @Test
    @Timeout(8000)
    void read_nullToken_returnsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        // nextNull() should be called
        doNothing().when(in).nextNull();

        Time result = adapter.read(in);

        verify(in).peek();
        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_validTimeString_returnsSqlTime() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        // Use time string matching the adapter's SimpleDateFormat "hh:mm:ss a"
        // Use Locale.US explicitly to avoid locale issues
        when(in.nextString()).thenReturn("10:15:30 AM");

        // Stub getPreviousPath to avoid null in error messages if needed
        when(in.getPreviousPath()).thenReturn("path");

        // Set the default locale to US for the test to match the adapter's SimpleDateFormat locale
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            Time result = adapter.read(in);

            verify(in).peek();
            verify(in).nextString();

            // Expected time parsed from string "10:15:30 AM"
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
            try {
                Date parsedDate = sdf.parse("10:15:30 AM");
                Time expectedTime = new Time(parsedDate.getTime());
                assertEquals(expectedTime, result);
            } catch (Exception e) {
                fail("Parsing time failed in test setup");
            }
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    @Timeout(8000)
    void read_invalidTimeString_throwsJsonSyntaxException() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);
        String invalidTime = "invalid-time";
        when(in.nextString()).thenReturn(invalidTime);
        when(in.getPreviousPath()).thenReturn("path");

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            adapter.read(in);
        });

        verify(in).peek();
        verify(in).nextString();
        verify(in).getPreviousPath();

        assertTrue(thrown.getMessage().contains("Failed parsing"));
        assertTrue(thrown.getMessage().contains(invalidTime));
        assertTrue(thrown.getMessage().contains("path"));
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof java.text.ParseException);
    }
}