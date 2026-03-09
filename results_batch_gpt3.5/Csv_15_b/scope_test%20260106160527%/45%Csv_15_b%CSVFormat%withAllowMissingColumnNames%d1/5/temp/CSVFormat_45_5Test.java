package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_45_5Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_defaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withAllowMissingColumnNames();
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());
        // Should return a new instance if the flag changes
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_explicitFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
        // Calling withAllowMissingColumnNames() on a false instance should return a new instance with true
        CSVFormat result = format.withAllowMissingColumnNames();
        assertTrue(result.getAllowMissingColumnNames());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_idempotent() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat result = format.withAllowMissingColumnNames(true);
        // Calling again with true should return the same instance because allowMissingColumnNames is already true
        assertSame(format, result);
    }
}