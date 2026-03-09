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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_45_5Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_defaultTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withAllowMissingColumnNames(true);
        assertNotNull(updated);
        assertTrue(updated.getAllowMissingColumnNames());
        // Ensure original is unchanged (immutability)
        assertFalse(original.getAllowMissingColumnNames());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_explicitFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat updated = original.withAllowMissingColumnNames(false);
        assertNotNull(updated);
        assertFalse(updated.getAllowMissingColumnNames());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_explicitTrue() {
        CSVFormat original = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        CSVFormat updated = original.withAllowMissingColumnNames(true);
        assertNotNull(updated);
        assertTrue(updated.getAllowMissingColumnNames());
        assertNotSame(original, updated);
    }
}