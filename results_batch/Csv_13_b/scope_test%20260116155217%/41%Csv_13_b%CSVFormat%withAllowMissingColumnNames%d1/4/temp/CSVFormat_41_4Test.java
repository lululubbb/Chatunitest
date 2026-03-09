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

public class CSVFormat_41_4Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_DefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withAllowMissingColumnNames();
        assertNotNull(newFormat);
        assertTrue(newFormat.getAllowMissingColumnNames());
        // Original instance should remain unchanged (immutable)
        assertTrue(format.getAllowMissingColumnNames());  // DEFAULT has allowMissingColumnNames = true
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_FalseThenTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());

        CSVFormat newFormat = format.withAllowMissingColumnNames(true);
        assertTrue(newFormat.getAllowMissingColumnNames());
        assertNotSame(format, newFormat);
    }
}