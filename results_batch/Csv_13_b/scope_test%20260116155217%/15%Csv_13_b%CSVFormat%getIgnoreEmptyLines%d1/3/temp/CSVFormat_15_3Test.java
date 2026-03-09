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

public class CSVFormat_15_3Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_DefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_ConstantRFC4180() {
        assertFalse(CSVFormat.RFC4180.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_ConstantEXCEL() {
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_ConstantTDF() {
        assertTrue(CSVFormat.TDF.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_ConstantMYSQL() {
        assertFalse(CSVFormat.MYSQL.getIgnoreEmptyLines());
    }

}