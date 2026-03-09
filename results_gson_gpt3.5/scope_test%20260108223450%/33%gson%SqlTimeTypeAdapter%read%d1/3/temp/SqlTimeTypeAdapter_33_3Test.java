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
import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

class SqlTimeTypeAdapter_33_3Test {

    private SqlTimeTypeAdapter adapter;
    private JsonReader jsonReader;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();
        jsonReader = mock(JsonReader.class);

        // Fix the internal DateFormat to use Locale.US to avoid parsing issues
        Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(adapter, new SimpleDateFormat("hh:mm:ss a", java.util.Locale.US));
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        doNothing().when(jsonReader).nextNull();

        Time result = adapter.read(jsonReader);

        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnTime_whenJsonTokenIsStringAndParsable() throws Exception {
        String timeString = "08:30:45 AM";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(timeString);

        Time result = adapter.read(jsonReader);

        assertNotNull(result);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", java.util.Locale.US);
        Date expectedDate = sdf.parse(timeString);
        assertEquals(new Time(expectedDate.getTime()), result);
    }

    @Test
    @Timeout(8000)
    void read_shouldThrowJsonSyntaxException_whenParseExceptionOccurs() throws IOException {
        String invalidTimeString = "invalid-time-format";
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn(invalidTimeString);
        when(jsonReader.getPreviousPath()).thenReturn("$");

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(jsonReader));
        assertTrue(thrown.getMessage().contains("Failed parsing 'invalid-time-format' as SQL Time; at path $"));
        assertNotNull(thrown.getCause());
        assertEquals(java.text.ParseException.class, thrown.getCause().getClass());
    }

    @Test
    @Timeout(8000)
    void read_shouldBeSynchronizedOnThis() throws Exception {
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("08:30:45 AM");

        Runnable task = () -> {
            try {
                Time t = adapter.read(jsonReader);
                assertNotNull(t);
            } catch (IOException e) {
                fail(e);
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }
}