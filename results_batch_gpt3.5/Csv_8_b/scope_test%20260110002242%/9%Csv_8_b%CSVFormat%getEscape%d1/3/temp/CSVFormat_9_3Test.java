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

class CSVFormat_9_3Test {

    @Test
    @Timeout(8000)
    void testGetEscapeWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWhenChar() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertEquals(Character.valueOf('\\'), format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeOriginalDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWithCharacterObject() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape(Character.valueOf('\\'));
        assertEquals(Character.valueOf('\\'), format.getEscape());
    }
}