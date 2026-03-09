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

class CSVFormat_37_3Test {

    @Test
    @Timeout(8000)
    void testWithHeader_nullHeader() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeader((String[]) null);
        assertNotNull(result);
        assertNotSame(base, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_emptyHeader() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat result = base.withHeader(new String[0]);
        assertNotNull(result);
        assertNotSame(base, result);
        assertArrayEquals(new String[0], result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_singleHeader() {
        CSVFormat base = CSVFormat.DEFAULT;
        String[] header = {"col1"};
        CSVFormat result = base.withHeader(header);
        assertNotNull(result);
        assertNotSame(base, result);
        assertArrayEquals(header, result.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_multipleHeaders() {
        CSVFormat base = CSVFormat.DEFAULT;
        String[] header = {"col1", "col2", "col3"};
        CSVFormat result = base.withHeader(header);
        assertNotNull(result);
        assertNotSame(base, result);
        assertArrayEquals(header, result.getHeader());
    }
}