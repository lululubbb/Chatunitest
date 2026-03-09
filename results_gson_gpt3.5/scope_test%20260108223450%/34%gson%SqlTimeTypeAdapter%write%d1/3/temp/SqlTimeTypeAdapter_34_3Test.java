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
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Time;
import java.text.SimpleDateFormat;

class SqlTimeTypeAdapter_34_3Test {

    private SqlTimeTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        stringWriter = new StringWriter();
        jsonWriter = spy(new JsonWriter(stringWriter));
    }

    @Test
    @Timeout(8000)
    void write_nullValue_callsNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        verify(jsonWriter, never()).value(anyString());
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_formatsAndWritesTime() throws Exception {
        // Prepare time instance
        Time time = Time.valueOf("13:45:30"); // 1:45:30 PM

        // Use reflection to access the private 'format' field to get expected string
        SimpleDateFormat format = (SimpleDateFormat) getPrivateField(adapter, "format");
        String expectedFormattedTime;
        synchronized (adapter) {
            expectedFormattedTime = format.format(time);
        }

        adapter.write(jsonWriter, time);

        InOrder inOrder = inOrder(jsonWriter);
        inOrder.verify(jsonWriter).value(expectedFormattedTime);
        inOrder.verifyNoMoreInteractions();
    }

    // Utility method to get private field via reflection
    private Object getPrivateField(Object instance, String fieldName) throws Exception {
        var field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

}