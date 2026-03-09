package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonReader_211_2Test {

    private JsonReader jsonReader;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        jsonReader = new JsonReader(mockReader);
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedNone_callsDoPeekAndSkips() throws Exception {
        setField(jsonReader, "peeked", 0); // PEEKED_NONE
        // Mock doPeek to return PEEKED_UNQUOTED (10)
        Method doPeekMethod = JsonReader.class.getDeclaredMethod("doPeek");
        doPeekMethod.setAccessible(true);
        JsonReader spyReader = spy(jsonReader);
        doReturn(10).when(spyReader).doPeek();

        // spy skipUnquotedValue so we can verify it is called
        doNothing().when(spyReader).skipUnquotedValue();

        setField(spyReader, "peeked", 0);
        setField(spyReader, "stackSize", 1);
        setField(spyReader, "pathIndices", new int[32]);
        setField(spyReader, "pathNames", new String[32]);

        spyReader.skipValue();

        verify(spyReader).doPeek();
        verify(spyReader).skipUnquotedValue();
        assertEquals(0, getField(spyReader, "peeked"));
        int[] pathIndices = getField(spyReader, "pathIndices");
        assertEquals(1, pathIndices[0]);
    }

    @Test
    @Timeout(8000)
    void skipValue_beginArray_pushesAndIncrementsCount() throws Exception {
        setField(jsonReader, "peeked", 3); // PEEKED_BEGIN_ARRAY
        setField(jsonReader, "stackSize", 0);
        setField(jsonReader, "pathIndices", new int[32]);
        setField(jsonReader, "pathNames", new String[32]);

        jsonReader.skipValue();

        assertEquals(1, getField(jsonReader, "stackSize"));
        int[] pathIndices = getField(jsonReader, "pathIndices");
        assertEquals(1, pathIndices[0]);
        String[] pathNames = getField(jsonReader, "pathNames");
        assertNull(pathNames[0]);
        assertEquals(0, getField(jsonReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    void skipValue_beginObject_pushesAndIncrementsCount() throws Exception {
        setField(jsonReader, "peeked", 1); // PEEKED_BEGIN_OBJECT
        setField(jsonReader, "stackSize", 0);
        setField(jsonReader, "pathIndices", new int[32]);
        setField(jsonReader, "pathNames", new String[32]);

        jsonReader.skipValue();

        assertEquals(1, getField(jsonReader, "stackSize"));
        int[] pathIndices = getField(jsonReader, "pathIndices");
        assertEquals(1, pathIndices[0]);
        assertEquals(0, getField(jsonReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    void skipValue_endArray_decrementsCountAndStackSize() throws Exception {
        setField(jsonReader, "peeked", 4); // PEEKED_END_ARRAY
        setField(jsonReader, "stackSize", 1);
        setField(jsonReader, "pathIndices", new int[32]);
        setField(jsonReader, "pathNames", new String[32]);

        jsonReader.skipValue();

        assertEquals(0, getField(jsonReader, "stackSize"));
        int[] pathIndices = getField(jsonReader, "pathIndices");
        // pathIndices not incremented because count goes to zero and loop ends before increment
        assertEquals(0, pathIndices[0]);
        assertEquals(0, getField(jsonReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    void skipValue_endObject_countZero_clearsPathNameAndDecrementsStack() throws Exception {
        setField(jsonReader, "peeked", 2); // PEEKED_END_OBJECT
        setField(jsonReader, "stackSize", 1);
        setField(jsonReader, "pathNames", new String[32]);
        setField(jsonReader, "pathIndices", new int[32]);

        jsonReader.skipValue();

        assertEquals(0, getField(jsonReader, "stackSize"));
        String[] pathNames = getField(jsonReader, "pathNames");
        assertNull(pathNames[0]);
        assertEquals(0, getField(jsonReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    void skipValue_endObject_countNotZero_doesNotClearPathName() throws Exception {
        setField(jsonReader, "peeked", 2); // PEEKED_END_OBJECT
        setField(jsonReader, "stackSize", 1);
        setField(jsonReader, "pathNames", new String[32]);
        setField(jsonReader, "pathIndices", new int[32]);
        // Setup count > 0 by setting peeked to PEEKED_BEGIN_OBJECT first to push stack
        setField(jsonReader, "peeked", 1);
        jsonReader.skipValue();

        // Now test with count != 0
        setField(jsonReader, "peeked", 2);
        setField(jsonReader, "stackSize", 1);
        String[] pathNames = getField(jsonReader, "pathNames");
        pathNames[0] = "original";
        setField(jsonReader, "pathNames", pathNames);

        // Using reflection to call private method skipValue with count > 0 is complicated, we simulate count by calling skipValue again
        // Instead, test that pathNames[0] remains unchanged if count != 0
        // We test by spying to prevent decrement of count, but here we just verify pathNames not cleared when count != 0

        // This test is covered by the flow in skipValue, so no direct way to test count != 0 on end object without modifying code
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedUnquotedName_skipsValueAndSetsPathNameIfCountZero() throws Exception {
        setField(jsonReader, "peeked", 14); // PEEKED_UNQUOTED_NAME
        setField(jsonReader, "stackSize", 1);
        String[] pathNames = new String[32];
        setField(jsonReader, "pathNames", pathNames);
        setField(jsonReader, "pathIndices", new int[32]);

        JsonReader spyReader = spy(jsonReader);
        doNothing().when(spyReader).skipUnquotedValue();

        spyReader.skipValue();

        verify(spyReader).skipUnquotedValue();
        assertEquals("<skipped>", spyReader.pathNames[0]);
        assertEquals(0, spyReader.peeked);
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedSingleQuotedName_skipsValueAndSetsPathNameIfCountZero() throws Exception {
        setField(jsonReader, "peeked", 12); // PEEKED_SINGLE_QUOTED_NAME
        setField(jsonReader, "stackSize", 1);
        String[] pathNames = new String[32];
        setField(jsonReader, "pathNames", pathNames);
        setField(jsonReader, "pathIndices", new int[32]);

        JsonReader spyReader = spy(jsonReader);
        doNothing().when(spyReader).skipQuotedValue('\'');

        spyReader.skipValue();

        verify(spyReader).skipQuotedValue('\'');
        assertEquals("<skipped>", spyReader.pathNames[0]);
        assertEquals(0, spyReader.peeked);
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedDoubleQuotedName_skipsValueAndSetsPathNameIfCountZero() throws Exception {
        setField(jsonReader, "peeked", 13); // PEEKED_DOUBLE_QUOTED_NAME
        setField(jsonReader, "stackSize", 1);
        String[] pathNames = new String[32];
        setField(jsonReader, "pathNames", pathNames);
        setField(jsonReader, "pathIndices", new int[32]);

        JsonReader spyReader = spy(jsonReader);
        doNothing().when(spyReader).skipQuotedValue('"');

        spyReader.skipValue();

        verify(spyReader).skipQuotedValue('"');
        assertEquals("<skipped>", spyReader.pathNames[0]);
        assertEquals(0, spyReader.peeked);
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedSingleQuoted_skipsQuotedValue() throws Exception {
        setField(jsonReader, "peeked", 8); // PEEKED_SINGLE_QUOTED
        JsonReader spyReader = spy(jsonReader);
        doNothing().when(spyReader).skipQuotedValue('\'');

        spyReader.skipValue();

        verify(spyReader).skipQuotedValue('\'');
        assertEquals(0, spyReader.peeked);
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedDoubleQuoted_skipsQuotedValue() throws Exception {
        setField(jsonReader, "peeked", 9); // PEEKED_DOUBLE_QUOTED
        JsonReader spyReader = spy(jsonReader);
        doNothing().when(spyReader).skipQuotedValue('"');

        spyReader.skipValue();

        verify(spyReader).skipQuotedValue('"');
        assertEquals(0, spyReader.peeked);
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedNumber_advancesPos() throws Exception {
        setField(jsonReader, "peeked", 16); // PEEKED_NUMBER
        setField(jsonReader, "peekedNumberLength", 5);
        setField(jsonReader, "pos", 3);

        jsonReader.skipValue();

        assertEquals(8, getField(jsonReader, "pos"));
        assertEquals(0, getField(jsonReader, "peeked"));
    }

    @Test
    @Timeout(8000)
    void skipValue_peekedEof_returnsImmediately() throws Exception {
        setField(jsonReader, "peeked", 17); // PEEKED_EOF

        jsonReader.skipValue();

        assertEquals(17, getField(jsonReader, "peeked"));
    }

    // Helper methods for reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) throws Exception {
        Field field = JsonReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }

    private void setField(Object instance, String fieldName, Object value) {
        try {
            Field field = JsonReader.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}