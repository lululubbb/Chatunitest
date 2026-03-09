package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_19_3Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_CustomInstanceWithQuoteModeSet() throws Exception {
        // Use reflection to create a CSVFormat instance with a specific quoteMode
        CSVFormat format = createCSVFormatWithQuoteMode(QuoteMode.ALL_NON_NULL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_CustomInstanceWithQuoteModeNull() throws Exception {
        CSVFormat format = createCSVFormatWithQuoteMode(null);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode);
    }

    private CSVFormat createCSVFormatWithQuoteMode(QuoteMode quoteMode) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar
                quoteMode, // quoteMode
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                true, // ignoreEmptyLines (match DEFAULT)
                "\r\n", // recordSeparator
                null, // nullString
                null, // headerComments (Object[])
                null, // header (String[])
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false, // trailingDelimiter
                false  // autoFlush
        );
    }
}