package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

class CSVFormat_24_6Test {

    @Test
    @Timeout(8000)
    void testParse() throws IOException {
        // Given
        Reader reader = mock(Reader.class);
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVParser csvParser = csvFormat.parse(reader);

        // Then
        assertNotNull(csvParser);
    }
}