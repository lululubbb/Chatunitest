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

class CSVFormat_8_6Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_DefaultConstructor() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_NewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withDelimiter('|');
        assertEquals('|', modified.getDelimiter());
        // Original should remain unchanged
        assertEquals(',', original.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_MultipleWithDelimiterCalls() {
        CSVFormat format = CSVFormat.newFormat(':').withDelimiter('\t').withDelimiter('^');
        assertEquals('^', format.getDelimiter());
    }
}