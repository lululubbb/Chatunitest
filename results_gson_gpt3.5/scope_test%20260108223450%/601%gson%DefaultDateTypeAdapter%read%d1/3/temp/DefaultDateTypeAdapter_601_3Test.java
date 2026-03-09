package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.JavaVersion;
import com.google.gson.internal.PreJava9DateFormatProvider;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class DefaultDateTypeAdapter_601_3Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private DefaultDateTypeAdapter.DateType<Date> dateType;

    @BeforeEach
    void setUp() throws Exception {
        dateType = mock(DefaultDateTypeAdapter.DateType.class);

        // Use reflection to get constructor with (DateType<T>, int) since String constructor doesn't exist
        Constructor<DefaultDateTypeAdapter> constructor = DefaultDateTypeAdapter.class.getDeclaredConstructor(DefaultDateTypeAdapter.DateType.class, int.class);
        constructor.setAccessible(true);
        adapter = (DefaultDateTypeAdapter<Date>) constructor.newInstance(dateType, 0);

        // Initialize dateFormats list with a SimpleDateFormat to avoid NPE in deserializeToDate
        Field dateFormatsField = DefaultDateTypeAdapter.class.getDeclaredField("dateFormats");
        dateFormatsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<DateFormat> dateFormats = (List<DateFormat>) dateFormatsField.get(adapter);
        dateFormats.clear();
        dateFormats.add(new SimpleDateFormat("MMM d, yyyy", Locale.US));
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Date result = adapter.read(in);

        InOrder inOrder = inOrder(in);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_shouldReturnDeserializedDate_whenJsonTokenIsNotNull() throws Exception {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.STRING);

        String dateString = "Jan 1, 2020";
        when(in.nextString()).thenReturn(dateString);

        Date parsedDate = new SimpleDateFormat("MMM d, yyyy", Locale.US).parse(dateString);
        when(dateType.deserialize(parsedDate)).thenReturn(parsedDate);

        Date result = adapter.read(in);

        InOrder inOrder = inOrder(in, dateType);
        inOrder.verify(in).peek();
        inOrder.verify(in).nextString();
        inOrder.verify(dateType).deserialize(parsedDate);

        assertEquals(parsedDate, result);
    }
}