package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonWriter;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;

class DefaultDateTypeAdapter_601_4Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private DefaultDateTypeAdapter.DateType<Date> dateTypeMock;

    @BeforeEach
    void setUp() throws Exception {
        dateTypeMock = mock(DefaultDateTypeAdapter.DateType.class);
        // Use reflection to invoke the constructor with (DateType<T>, int, int)
        Constructor<DefaultDateTypeAdapter> constructor =
                DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, int.class, int.class);
        constructor.setAccessible(true);
        adapter = constructor.newInstance(dateTypeMock, DateFormat.SHORT, DateFormat.SHORT);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        // nextNull returns void, so just do nothing on call
        doNothing().when(jsonReader).nextNull();

        Date result = adapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldDeserializeDateAndReturnDeserializedValue() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("01/01/1970"); // Provide a valid date string matching SHORT format

        // Create a spy of adapter
        DefaultDateTypeAdapter<Date> spyAdapter = Mockito.spy(adapter);

        // Use reflection to get the private method deserializeToDate
        Method deserializeToDateMethod = DefaultDateTypeAdapter.class.getDeclaredMethod("deserializeToDate", JsonReader.class);
        deserializeToDateMethod.setAccessible(true);

        // Invoke the private method deserializeToDate directly on spyAdapter to get the Date to return
        Date deserializedDateFromPrivateMethod = (Date) deserializeToDateMethod.invoke(spyAdapter, jsonReader);

        // Now mock dateTypeMock.deserialize to return a modified date
        Date deserializedDate = new Date(deserializedDateFromPrivateMethod.getTime() + 1000);
        when(dateTypeMock.deserialize(deserializedDateFromPrivateMethod)).thenReturn(deserializedDate);

        // Now call read on spyAdapter
        Date result = spyAdapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextString();
        verify(dateTypeMock).deserialize(deserializedDateFromPrivateMethod);
        assertEquals(deserializedDate, result);
    }
}