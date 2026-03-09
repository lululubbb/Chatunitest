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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_37_6Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((String[]) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // Original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(new String[0]);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
        // Original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = {"col1", "col2", "col3"};
        CSVFormat result = format.withHeader(header);
        assertNotNull(result);
        assertArrayEquals(header, result.getHeader());
        // Original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = {"a", "b"};
        CSVFormat result = format.withHeader(header);
        // Changing original array after withHeader call should not affect the result header
        header[0] = "changed";
        assertNotEquals(header[0], result.getHeader()[0]);
    }
}