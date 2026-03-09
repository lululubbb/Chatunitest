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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_37_2Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader((String[]) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getHeader());
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader(new String[0]);
        assertNotNull(newFormat);
        assertArrayEquals(new String[0], newFormat.getHeader());
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat newFormat = format.withHeader(header);
        assertNotNull(newFormat);
        assertArrayEquals(header, newFormat.getHeader());
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[] {"a", "b"};
        CSVFormat newFormat = format.withHeader(header);
        // Changing original header array after withHeader call should not affect CSVFormat
        header[0] = "changed";
        assertNotEquals(header[0], newFormat.getHeader()[0]);
    }

}