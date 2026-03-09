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
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.SimpleDateFormat;

class SqlTimeTypeAdapter_34_2Test {

    private SqlTimeTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        stringWriter = new StringWriter();
        jsonWriter = spy(new JsonWriter(stringWriter));
    }

    @Test
    @Timeout(8000)
    void write_shouldWriteNullValue_whenTimeIsNull() throws IOException {
        adapter.write(jsonWriter, null);

        InOrder inOrder = inOrder(jsonWriter);
        inOrder.verify(jsonWriter).nullValue();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void write_shouldWriteFormattedTime_whenTimeIsNotNull() throws IOException {
        Time time = Time.valueOf("13:45:30");
        adapter.write(jsonWriter, time);

        String expectedFormattedTime;
        synchronized (adapter) {
            expectedFormattedTime = new SimpleDateFormat("hh:mm:ss a").format(time);
        }

        verify(jsonWriter).value(expectedFormattedTime);
        verify(jsonWriter, never()).nullValue();
    }
}