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

import com.google.gson.internal.sql.SqlDateTypeAdapter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

class SqlDateTypeAdapter_405_4Test {

    private SqlDateTypeAdapter adapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to access private constructor
        Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        adapter = constructor.newInstance();

        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    void write_nullValue_invokesNullValueOnJsonWriter() throws IOException {
        adapter.write(jsonWriter, null);
        // JsonWriter.nullValue() writes "null" to the underlying writer
        String output = stringWriter.toString();
        // The output should be "null"
        assert output.equals("null");
    }

    @Test
    @Timeout(8000)
    void write_nonNullValue_writesFormattedDate() throws IOException {
        // Prepare a specific date
        java.sql.Date date = java.sql.Date.valueOf("2023-04-15");
        // Format expected string according to adapter's format "MMM d, yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        String expectedFormattedDate = sdf.format(date);

        adapter.write(jsonWriter, date);

        String output = stringWriter.toString();
        // The output should be the formatted date surrounded by quotes (JsonWriter.value(String))
        assert output.equals("\"" + expectedFormattedDate + "\"");
    }

    @Test
    @Timeout(8000)
    void write_threadSafety_synchronizedFormatInvocation() throws Exception {
        // Using reflection to invoke private final DateFormat field and check synchronization
        Method writeMethod = SqlDateTypeAdapter.class.getDeclaredMethod("write", JsonWriter.class, java.sql.Date.class);
        writeMethod.setAccessible(true);

        java.sql.Date date = java.sql.Date.valueOf("2023-06-01");

        // We create a spy on the adapter to check the synchronization block indirectly by invoking multiple threads
        SqlDateTypeAdapter spyAdapter = spy(adapter);

        // Run multiple threads calling write concurrently to test synchronization indirectly
        Runnable task = () -> {
            try {
                writeMethod.invoke(spyAdapter, jsonWriter, date);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // The output should contain two formatted dates
        String output = stringWriter.toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        String expectedFormattedDate = sdf.format(date);
        String expectedOutput = "\"" + expectedFormattedDate + "\"" + "\"" + expectedFormattedDate + "\"";
        assert output.equals(expectedOutput);
    }
}