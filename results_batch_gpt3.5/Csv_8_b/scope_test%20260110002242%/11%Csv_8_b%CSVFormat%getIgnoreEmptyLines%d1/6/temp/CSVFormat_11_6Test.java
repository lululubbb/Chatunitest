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

public class CSVFormat_11_6Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_DefaultInstance() {
        // DEFAULT has ignoreEmptyLines = true
        assertTrue(CSVFormat.DEFAULT.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_RFC4180Instance() {
        // RFC4180 has ignoreEmptyLines = false
        assertFalse(CSVFormat.RFC4180.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_EXCELInstance() {
        // EXCEL has ignoreEmptyLines = false
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_TDFInstance() {
        // TDF has ignoreEmptyLines = true (inherited from DEFAULT)
        assertTrue(CSVFormat.TDF.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_MYSQLInstance() {
        // MYSQL has ignoreEmptyLines = false
        assertFalse(CSVFormat.MYSQL.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_CustomInstanceTrue() {
        CSVFormat custom = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertTrue(custom.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_CustomInstanceFalse() {
        CSVFormat custom = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(custom.getIgnoreEmptyLines());
    }
}