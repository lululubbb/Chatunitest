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
import java.util.Date;

class DefaultDateTypeAdapter_601_6Test {

    private DefaultDateTypeAdapter<Date> adapter;
    private DefaultDateTypeAdapter.DateType<Date> dateTypeMock;

    @BeforeEach
    void setUp() throws Exception {
        dateTypeMock = mock(DefaultDateTypeAdapter.DateType.class);

        // Use reflection to find and invoke the private constructor with (DateType<T>, int)
        Constructor<?>[] constructors = DefaultDateTypeAdapter.class.getDeclaredConstructors();
        Constructor<?> targetConstructor = null;
        for (Constructor<?> c : constructors) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == 2) {
                if (DefaultDateTypeAdapter.DateType.class.isAssignableFrom(params[0]) &&
                    params[1] == int.class) {
                    targetConstructor = c;
                    break;
                }
            }
        }
        if (targetConstructor == null) {
            throw new IllegalStateException("Suitable constructor not found");
        }
        targetConstructor.setAccessible(true);
        // Use style = 0 (e.g. DateFormat.SHORT) for int parameter
        adapter = (DefaultDateTypeAdapter<Date>) targetConstructor.newInstance(dateTypeMock, 0);
    }

    @Test
    @Timeout(8000)
    void read_NullToken_ReturnsNull() throws IOException {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        doNothing().when(jsonReader).nextNull();

        Date result = adapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void read_ValidDate_DeserializeCalled() throws Exception {
        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(jsonReader.nextString()).thenReturn("2023-01-01T00:00:00Z");

        Date expectedDeserialized = new Date();

        // Mock dateTypeMock.deserialize to return expectedDeserialized
        when(dateTypeMock.deserialize(any(Date.class))).thenReturn(expectedDeserialized);

        Date result = adapter.read(jsonReader);

        verify(jsonReader).peek();
        verify(jsonReader).nextString();
        verify(dateTypeMock).deserialize(any(Date.class));
        assertEquals(expectedDeserialized, result);
    }
}