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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_43_1Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withIgnoreEmptyLines(true);
        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines());
        // Original format should remain unchanged (immutable)
        assertFalse(format.getIgnoreEmptyLines());
        // The returned instance should be different if the flag changes
        CSVFormat formatFalse = format.withIgnoreEmptyLines(false);
        CSVFormat result2 = formatFalse.withIgnoreEmptyLines(true);
        assertTrue(result2.getIgnoreEmptyLines());
        assertNotSame(formatFalse, result2);
    }
}