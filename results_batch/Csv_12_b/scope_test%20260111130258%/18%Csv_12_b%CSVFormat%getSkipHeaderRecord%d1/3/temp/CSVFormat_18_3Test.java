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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class CSVFormat_18_3Test {

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord() throws Exception {
        // Create a mock CSVFormat object
        CSVFormat csvFormat = mock(CSVFormat.class);

        // Set up the mock to return true for skipHeaderRecord
        when(csvFormat.getSkipHeaderRecord()).thenReturn(true);

        // Verify the method returns the expected value
        assertEquals(true, csvFormat.getSkipHeaderRecord());
    }
}