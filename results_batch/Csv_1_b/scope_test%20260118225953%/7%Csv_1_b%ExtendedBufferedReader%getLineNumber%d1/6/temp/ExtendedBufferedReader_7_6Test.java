package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendedBufferedReader_7_6Test {

    private ExtendedBufferedReader extendedBufferedReader;

    @BeforeEach
    void setUp() {
        Reader dummyReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return -1;
            }

            @Override
            public void close() throws IOException {
            }
        };
        extendedBufferedReader = new ExtendedBufferedReader(dummyReader);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_initialValue() throws Exception {
        int lineNumber = extendedBufferedReader.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    @Timeout(8000)
    void testGetLineNumber_afterSettingLineCounter() throws Exception {
        Field lineCounterField = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        lineCounterField.setAccessible(true);
        lineCounterField.setInt(extendedBufferedReader, 5);

        int lineNumber = extendedBufferedReader.getLineNumber();
        assertEquals(5, lineNumber);
    }
}