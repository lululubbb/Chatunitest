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

public class CSVFormat_37_4Test {

    @Test
    @Timeout(8000)
    public void testWithHeader_NullHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader((String[]) null);
        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withHeader(new String[0]);
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[0], result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NonEmptyHeader() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat result = original.withHeader(header);
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(header, result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_OriginalUnchanged() {
        CSVFormat original = CSVFormat.DEFAULT;
        String[] header = new String[] {"a", "b"};
        CSVFormat result = original.withHeader(header);
        // Original header should remain null
        assertNull(original.getHeader());
        // Result header should be set
        assertArrayEquals(header, result.getHeader());
    }
}