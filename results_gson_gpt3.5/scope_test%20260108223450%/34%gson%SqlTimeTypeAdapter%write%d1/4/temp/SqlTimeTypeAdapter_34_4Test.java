package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.mockito.Mockito.*;

import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.SimpleDateFormat;

class SqlTimeTypeAdapter_34_4Test {

    private SqlTimeTypeAdapter adapter;
    private JsonWriter jsonWriter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to instantiate the private constructor
        var constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        jsonWriter = mock(JsonWriter.class);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_invokesNullValue() throws IOException {
        adapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        verifyNoMoreInteractions(jsonWriter);
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_writesFormattedTime() throws Exception {
        // Prepare a Time instance with a fixed time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long timeMillis = sdf.parse("15:30:45").getTime();
        Time time = new Time(timeMillis);

        // Because format is private final field, get it via reflection to produce expected string
        SimpleDateFormat format = (SimpleDateFormat) getPrivateField(adapter, "format");
        String expectedFormatted = format.format(time);

        adapter.write(jsonWriter, time);

        InOrder inOrder = inOrder(jsonWriter);
        inOrder.verify(jsonWriter).value(expectedFormatted);
        verifyNoMoreInteractions(jsonWriter);
    }

    // Utility method to get private field value by reflection
    private Object getPrivateField(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}