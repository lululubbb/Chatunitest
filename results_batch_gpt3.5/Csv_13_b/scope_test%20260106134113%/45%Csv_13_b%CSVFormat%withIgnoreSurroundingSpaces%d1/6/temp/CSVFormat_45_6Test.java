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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class CSVFormat_45_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withIgnoreSurroundingSpaces();
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreSurroundingSpaces());
        // Original format should remain unchanged (immutable)
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpacesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke public withIgnoreSurroundingSpaces(boolean)
        Method method = CSVFormat.class.getMethod("withIgnoreSurroundingSpaces", boolean.class);

        CSVFormat formatFalse = (CSVFormat) method.invoke(format, Boolean.FALSE);
        assertNotNull(formatFalse);
        assertFalse(formatFalse.getIgnoreSurroundingSpaces());

        CSVFormat formatTrue = (CSVFormat) method.invoke(format, Boolean.TRUE);
        assertNotNull(formatTrue);
        assertTrue(formatTrue.getIgnoreSurroundingSpaces());

        // Confirm original format unchanged
        assertFalse(format.getIgnoreSurroundingSpaces());
    }
}