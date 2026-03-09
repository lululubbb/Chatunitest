package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_19_2Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_NullQuoteMode() throws Exception {
        // Create a CSVFormat instance with quoteCharacter = null and quoteMode = MINIMAL
        CSVFormat defaultFormat = CSVFormat.DEFAULT;
        // Use reflection to get the private constructor
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");

        quoteCharField.setAccessible(true);
        quoteModeField.setAccessible(true);

        // Create a new CSVFormat instance with quoteCharacter null and quoteMode MINIMAL
        // Since CSVFormat is final and no public constructor, use withQuote(null) but it returns a new instance
        CSVFormat format = defaultFormat.withQuote((Character) null);

        // Access quoteMode field via reflection
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(format);

        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }
}