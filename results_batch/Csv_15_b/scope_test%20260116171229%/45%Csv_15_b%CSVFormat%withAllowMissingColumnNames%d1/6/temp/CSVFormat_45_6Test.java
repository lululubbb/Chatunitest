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

class CSVFormat_45_6Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getAllowMissingColumnNames(), "Default should not allow missing column names");

        CSVFormat modified = original.withAllowMissingColumnNames(true);
        assertNotNull(modified, "Result should not be null");
        assertTrue(modified.getAllowMissingColumnNames(), "Should allow missing column names after withAllowMissingColumnNames");

        // Calling again with same value should return the same instance
        CSVFormat modifiedAgain = modified.withAllowMissingColumnNames(true);
        assertNotNull(modifiedAgain);
        assertTrue(modifiedAgain.getAllowMissingColumnNames());
        assertSame(modified, modifiedAgain, "Subsequent calls with same value return same instance");

        // Calling with different value should return a new instance
        CSVFormat modifiedFalse = modified.withAllowMissingColumnNames(false);
        assertNotNull(modifiedFalse);
        assertFalse(modifiedFalse.getAllowMissingColumnNames());
        assertNotSame(modified, modifiedFalse, "withAllowMissingColumnNames with different value returns new instance");

        // Verify original remains unchanged
        assertFalse(original.getAllowMissingColumnNames(), "Original instance remains unchanged");
    }
}