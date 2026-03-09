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

public class CSVFormat_22_4Test {

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT trailingDelimiter is false
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_WithTrailingDelimiterTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_WithTrailingDelimiterFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_ExcelFormat() {
        CSVFormat format = CSVFormat.EXCEL;
        // EXCEL inherits from DEFAULT withTrailingDelimiter false
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_CustomFormat() throws Exception {
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
                Object[].class,
                String[].class,
                boolean.class,
                boolean.class,
                boolean.class,
                boolean.class,
                boolean.class,
                boolean.class
        );
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',',
                '"',
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                null,
                null,
                false,
                false,
                false,
                false,
                true,  // trailingDelimiter true
                false
        );
        assertTrue(format.getTrailingDelimiter());
    }
}