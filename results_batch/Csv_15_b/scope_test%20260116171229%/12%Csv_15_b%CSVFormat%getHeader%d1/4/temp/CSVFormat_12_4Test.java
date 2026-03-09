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

class CSVFormat_12_4Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null using withHeader()
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] result = format.getHeader();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNotNull() throws Exception {
        String[] header = new String[] {"col1", "col2"};
        // Create a new CSVFormat instance with header set using withHeader()
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header);

        String[] result = format.getHeader();
        assertNotNull(result);
        assertArrayEquals(header, result);

        // Verify that returned array is a clone, not the same reference
        assertNotSame(header, result);

        // Modifying returned array should not affect original header
        result[0] = "modified";
        String[] originalHeader = format.getHeader();
        assertEquals("col1", originalHeader[0]);
    }
}