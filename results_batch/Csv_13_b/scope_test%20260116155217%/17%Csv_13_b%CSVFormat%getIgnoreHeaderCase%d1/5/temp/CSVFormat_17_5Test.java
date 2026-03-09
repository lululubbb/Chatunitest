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

public class CSVFormat_17_5Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreHeaderCase(), "DEFAULT format should have ignoreHeaderCase false");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        assertTrue(format.getIgnoreHeaderCase(), "Format withIgnoreHeaderCase(true) should return true");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        assertFalse(format.getIgnoreHeaderCase(), "Format withIgnoreHeaderCase(false) should return false");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_WithIgnoreHeaderCase_Chained() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase().withIgnoreHeaderCase(false);
        assertFalse(format.getIgnoreHeaderCase(), "Chained withIgnoreHeaderCase(false) should override true");
    }
}