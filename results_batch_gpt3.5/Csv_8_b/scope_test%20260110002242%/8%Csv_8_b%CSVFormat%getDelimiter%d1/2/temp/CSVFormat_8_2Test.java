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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_8_2Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        char delimiter = format.getDelimiter();
        assertEquals(',', delimiter);
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_CustomDelimiter() {
        CSVFormat format = CSVFormat.newFormat(';');
        char delimiter = format.getDelimiter();
        assertEquals(';', delimiter);
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('|');
        char delimiter = format.getDelimiter();
        assertEquals('|', delimiter);
    }
}