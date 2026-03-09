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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

public class CSVFormat_37_5Test {

    private CSVFormat baseFormat;

    @BeforeEach
    public void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullHeader() throws Exception {
        CSVFormat formatWithNullHeader = baseFormat.withHeader((String[]) null);
        assertNotNull(formatWithNullHeader);
        assertNull(formatWithNullHeader.getHeader());
        // Ensure original format header is unchanged
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() throws Exception {
        CSVFormat formatWithEmptyHeader = baseFormat.withHeader(new String[0]);
        assertNotNull(formatWithEmptyHeader);
        assertNotNull(formatWithEmptyHeader.getHeader());
        assertEquals(0, formatWithEmptyHeader.getHeader().length);
        // Original format header remains null
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NonEmptyHeader() throws Exception {
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat formatWithHeader = baseFormat.withHeader(header);
        assertNotNull(formatWithHeader);
        assertArrayEquals(header, formatWithHeader.getHeader());
        // Original format header remains null
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_Immutability() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format1 = baseFormat.withHeader(header);
        CSVFormat format2 = format1.withHeader("x", "y", "z");
        assertArrayEquals(new String[] {"a", "b"}, format1.getHeader());
        assertArrayEquals(new String[] {"x", "y", "z"}, format2.getHeader());
        // baseFormat header still null
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_ReflectionInvocation() throws Exception {
        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", String[].class);
        String[] header = new String[] {"h1","h2"};
        CSVFormat format = (CSVFormat) withHeaderMethod.invoke(baseFormat, (Object) header);
        assertNotNull(format);
        assertArrayEquals(header, format.getHeader());
    }
}