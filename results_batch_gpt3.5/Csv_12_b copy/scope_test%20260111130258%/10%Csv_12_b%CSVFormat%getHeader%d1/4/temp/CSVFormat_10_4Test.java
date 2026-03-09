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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_10_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetHeader() {
        // Given
        String[] expectedHeader = {"Header1", "Header2"};
        csvFormat = csvFormat.withHeader(expectedHeader);

        // When
        String[] actualHeader = csvFormat.getHeader();

        // Then
        assertArrayEquals(expectedHeader, actualHeader);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenNull() {
        // Given
        csvFormat = csvFormat.withHeader((String[]) null);

        // When
        String[] actualHeader = csvFormat.getHeader();

        // Then
        assertNull(actualHeader);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderClone() {
        // Given
        String[] header = {"Header1", "Header2"};
        csvFormat = csvFormat.withHeader(header);

        // When
        String[] actualHeader = csvFormat.getHeader().clone();
        actualHeader[0] = "ModifiedHeader";

        // Then
        assertNotEquals("ModifiedHeader", header[0]);
    }
}