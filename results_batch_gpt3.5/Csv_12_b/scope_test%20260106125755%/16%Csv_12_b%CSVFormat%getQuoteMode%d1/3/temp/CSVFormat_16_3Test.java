package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_16_3Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_NonNull() throws Exception {
        QuoteMode mode = QuoteMode.ALL;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class,
                boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',',
                Character.valueOf('"'),
                mode,
                null,
                null,
                false,
                false,
                "\r\n",
                null,
                (Object) null,
                false,
                false);
        assertEquals(mode, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Null() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                QuoteMode.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class,
                boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',',
                Character.valueOf('"'),
                null,
                null,
                null,
                false,
                false,
                "\r\n",
                null,
                (Object) null,
                false,
                false);
        assertNull(format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_DefaultConstant() {
        assertNotNull(CSVFormat.DEFAULT.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeViaWithQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }
}