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
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.mockito.Mockito.*;

import com.google.gson.internal.sql.SqlTimeTypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.sql.Time;
import java.util.Calendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimeTypeAdapter_34_1Test {

  private SqlTimeTypeAdapter adapter;
  private JsonWriter jsonWriter;
  private StringWriter stringWriter;

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to access the private constructor
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    stringWriter = new StringWriter();
    jsonWriter = spy(new JsonWriter(stringWriter));
  }

  @Test
    @Timeout(8000)
  void write_nullValue_callsNullValue() throws IOException {
    adapter.write(jsonWriter, null);
    verify(jsonWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  void write_nonNullValue_writesFormattedTime() throws Exception {
    // Prepare a Time instance using Calendar to avoid parse issues
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR, 10);
    calendar.set(Calendar.MINUTE, 15);
    calendar.set(Calendar.SECOND, 30);
    calendar.set(Calendar.AM_PM, Calendar.AM);
    calendar.set(Calendar.MILLISECOND, 0);
    Time time = new Time(calendar.getTimeInMillis());

    // Invoke the write method
    adapter.write(jsonWriter, time);

    // Format expected string using the same pattern as in SqlTimeTypeAdapter
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm:ss a");
    String expected = sdf.format(time);

    // Verify that jsonWriter.value() was called with the correct formatted string
    verify(jsonWriter).value(expected);
  }
}