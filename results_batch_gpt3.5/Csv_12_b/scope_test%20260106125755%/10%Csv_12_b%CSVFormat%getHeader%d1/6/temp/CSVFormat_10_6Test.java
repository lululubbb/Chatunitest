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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_10_6Test {

    @Test
    @Timeout(8000)
    public void testGetHeader_whenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header set to null using withHeader()
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] result = format.getHeader();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testGetHeader_whenHeaderIsNotNull() throws Exception {
        String[] header = new String[] {"col1", "col2", "col3"};
        // Create a new CSVFormat instance with header set to header array using withHeader()
        CSVFormat format = CSVFormat.DEFAULT.withHeader(header);

        String[] result = format.getHeader();
        assertNotNull(result);
        assertArrayEquals(header, result);
        // Verify it returns a clone, not the same reference
        assertNotSame(header, result);
    }
}