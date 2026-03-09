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

public class CSVFormat_10_2Test {

    private CSVFormat createCSVFormatWithHeader(String[] header) {
        // Use the withHeader method to create a new CSVFormat instance with the desired header
        return CSVFormat.DEFAULT.withHeader(header);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsCloneOfHeader() {
        // Prepare header array
        String[] header = new String[]{"col1", "col2", "col3"};

        // Create CSVFormat instance with header set via withHeader method
        CSVFormat csvFormat = createCSVFormatWithHeader(header);

        // Invoke getHeader and verify it returns a clone, not the same array
        String[] returnedHeader = csvFormat.getHeader();

        assertNotNull(returnedHeader, "Returned header should not be null");
        assertArrayEquals(header, returnedHeader, "Returned header should equal the original header");
        assertNotSame(header, returnedHeader, "Returned header should be a clone, not the same instance");

        // Modify returnedHeader and verify original header is not affected
        returnedHeader[0] = "modified";

        // Since CSVFormat is immutable, getHeader() again should return original header
        String[] headerAfterModification = csvFormat.getHeader();
        assertEquals("col1", headerAfterModification[0], "Original header should not be modified");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderReturnsNullWhenHeaderIsNull() {
        // Create CSVFormat instance with header set to null via withHeader method
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader((String[]) null);

        // Invoke getHeader and verify it returns null
        String[] returnedHeader = csvFormat.getHeader();
        assertNull(returnedHeader, "Returned header should be null when header field is null");
    }
}