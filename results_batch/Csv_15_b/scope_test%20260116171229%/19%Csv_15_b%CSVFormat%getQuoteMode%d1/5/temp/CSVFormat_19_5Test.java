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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;

public class CSVFormat_19_5Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Default() {
        QuoteMode quoteMode = csvFormatDefault.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_WithDifferentQuoteModes() throws Exception {
        QuoteMode[] modes = QuoteMode.values();

        for (QuoteMode mode : modes) {
            CSVFormat instance = createCSVFormatWithQuoteMode(mode);
            assertEquals(mode, instance.getQuoteMode(),
                    "QuoteMode should be " + mode + " but was " + instance.getQuoteMode());
        }
    }

    private CSVFormat createCSVFormatWithQuoteMode(QuoteMode quoteMode) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;

        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        return constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar (Character)
                quoteMode, // quoteMode
                null, // commentStart (Character)
                null, // escape (Character)
                false, // ignoreSurroundingSpaces
                true, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // nullString
                (Object) null, // headerComments (Object[])
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