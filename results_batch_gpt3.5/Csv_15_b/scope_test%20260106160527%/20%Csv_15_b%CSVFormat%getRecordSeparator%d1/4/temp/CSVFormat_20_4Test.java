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

class CSVFormat_20_4Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        csvFormatCustom = constructor.newInstance(',',
                '"',
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                new Object[0],   // Pass empty Object[] instead of null
                new String[0],   // Pass empty String[] instead of null
                false,
                false,
                false,
                false,
                false,
                false);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Default() {
        assertEquals("\r\n", csvFormatDefault.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomSeparator() {
        CSVFormat custom = csvFormatDefault.withRecordSeparator("\n");
        assertEquals("\n", custom.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_NullSeparator() {
        CSVFormat custom = csvFormatDefault.withRecordSeparator((String) null);
        assertNull(custom.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_EmptySeparator() {
        CSVFormat custom = csvFormatDefault.withRecordSeparator("");
        assertEquals("", custom.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_WithChar() {
        CSVFormat custom = csvFormatDefault.withRecordSeparator('\n');
        assertEquals("\n", custom.getRecordSeparator());
    }
}