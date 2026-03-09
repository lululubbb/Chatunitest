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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_56_1Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader((String[]) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // Original remains unchanged
        assertNull(original.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader(new String[0]);
        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
        // Original remains unchanged
        assertNull(original.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] header = new String[]{"col1", "col2", "col3"};
        CSVFormat result = original.withHeader(header);
        assertNotNull(result);
        assertArrayEquals(header, result.getHeader());
        // Original remains unchanged
        assertNull(original.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] header = new String[]{"a", "b"};
        CSVFormat result = original.withHeader(header);
        // Changing original header array after call should not affect result
        header[0] = "changed";
        assertNotEquals(header[0], result.getHeader()[0]);
    }
}