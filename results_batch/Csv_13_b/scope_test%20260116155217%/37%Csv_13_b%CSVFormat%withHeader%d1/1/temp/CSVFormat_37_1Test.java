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

public class CSVFormat_37_1Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullArray() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((String[]) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // original instance unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyArray() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(new String[0]);
        assertNotNull(result);
        assertArrayEquals(new String[0], result.getHeader());
        // original instance unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyArray() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] headers = {"col1", "col2", "col3"};
        CSVFormat result = format.withHeader(headers);
        assertNotNull(result);
        assertArrayEquals(headers, result.getHeader());
        // original instance unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat result = format.withHeader("c", "d");
        assertNotSame(format, result);
        assertArrayEquals(new String[]{"a", "b"}, format.getHeader());
        assertArrayEquals(new String[]{"c", "d"}, result.getHeader());
    }
}