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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_12_4Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_True() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_False() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_TDFConstant() {
        try {
            java.lang.reflect.Field tdfField = CSVFormat.class.getDeclaredField("TDF");
            tdfField.setAccessible(true);
            CSVFormat tdfFormat = (CSVFormat) tdfField.get(null);
            assertTrue(tdfFormat.getIgnoreSurroundingSpaces());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("TDF constant not found or inaccessible");
        }
    }
}