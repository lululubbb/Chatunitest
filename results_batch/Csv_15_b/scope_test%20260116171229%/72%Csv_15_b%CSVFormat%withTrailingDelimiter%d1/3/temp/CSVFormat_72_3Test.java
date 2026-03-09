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

class CSVFormat_72_3Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getTrailingDelimiter(), "Default format should not have trailing delimiter");

        CSVFormat newFormat = format.withTrailingDelimiter();
        assertNotNull(newFormat, "withTrailingDelimiter should not return null");
        assertTrue(newFormat.getTrailingDelimiter(), "New format should have trailing delimiter");
        // Original format should remain unchanged (immutability)
        assertFalse(format.getTrailingDelimiter(), "Original format should remain unchanged");

        // Also test withTrailingDelimiter(boolean) directly for false
        CSVFormat revertedFormat = newFormat.withTrailingDelimiter(false);
        assertNotNull(revertedFormat);
        assertFalse(revertedFormat.getTrailingDelimiter(), "Trailing delimiter should be false after reverting");

        // Test chaining multiple times
        CSVFormat chainedFormat = format.withTrailingDelimiter().withTrailingDelimiter(false).withTrailingDelimiter();
        assertTrue(chainedFormat.getTrailingDelimiter(), "Trailing delimiter should be true after chaining");
    }
}