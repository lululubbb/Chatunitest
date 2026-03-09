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

public class CSVFormat_41_5Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        CSVFormat format = CSVFormat.DEFAULT;
        // The method withAllowMissingColumnNames() calls withAllowMissingColumnNames(true)
        CSVFormat newFormat = format.withAllowMissingColumnNames();

        // The returned CSVFormat should be different instance if allowMissingColumnNames was false before
        if (format.getAllowMissingColumnNames()) {
            // If already true, the same instance is returned
            assertSame(format, newFormat);
        } else {
            // Otherwise, a new instance is returned
            assertNotSame(format, newFormat);
            assertTrue(newFormat.getAllowMissingColumnNames());
        }
    }
}