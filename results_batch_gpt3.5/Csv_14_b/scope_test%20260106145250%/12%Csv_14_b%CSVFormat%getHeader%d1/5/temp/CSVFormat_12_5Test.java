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

import java.lang.reflect.Constructor;

class CSVFormat_12_5Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create CSVFormat instance with header = null using reflection
        CSVFormat formatWithNullHeader = createCSVFormatWithHeader(null);
        assertNull(formatWithNullHeader.getHeader());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNotNull() throws Exception {
        String[] header = new String[]{"col1", "col2", "col3"};
        CSVFormat formatWithHeader = createCSVFormatWithHeader(header);
        String[] returnedHeader = formatWithHeader.getHeader();
        assertNotNull(returnedHeader);
        assertArrayEquals(header, returnedHeader);
        // Ensure clone: modifying returned array does not affect original
        returnedHeader[0] = "modified";
        String[] returnedHeader2 = formatWithHeader.getHeader();
        assertEquals("col1", returnedHeader2[0]);
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
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
                boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(
                ',',
                '"',
                null,
                null,
                null,
                false,
                true,
                "\r\n",
                null,
                (Object) new Object[0],
                header,
                false,
                false,
                false,
                false,
                false);
    }
}