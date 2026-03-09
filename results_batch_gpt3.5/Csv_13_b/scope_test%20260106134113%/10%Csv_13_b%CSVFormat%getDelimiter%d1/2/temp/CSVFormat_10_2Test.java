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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_2Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_CustomDelimiter() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat modifiedFormat = baseFormat.withDelimiter('|');
        assertEquals('|', modifiedFormat.getDelimiter());
    }
}