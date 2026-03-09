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

class CSVFormat_21_4Test {

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_WithRecordSeparatorChar() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator('\n');
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_WithRecordSeparatorString() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\r");
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomFormat() {
        CSVFormat format = CSVFormat.newFormat(';').withRecordSeparator("||");
        String recordSeparator = format.getRecordSeparator();
        assertEquals("||", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_MySQLFormat() {
        CSVFormat format = CSVFormat.MYSQL;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\n", recordSeparator);
    }
}