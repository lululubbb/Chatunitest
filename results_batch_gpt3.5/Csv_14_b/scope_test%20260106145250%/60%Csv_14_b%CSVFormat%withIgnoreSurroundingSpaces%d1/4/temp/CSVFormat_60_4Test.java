package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_60_4Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreSurroundingSpaces(), "Default CSVFormat should not ignore surrounding spaces");

        CSVFormat modified = original.withIgnoreSurroundingSpaces();
        assertNotNull(modified, "Modified CSVFormat should not be null");
        assertTrue(modified.getIgnoreSurroundingSpaces(), "CSVFormat after withIgnoreSurroundingSpaces() should ignore surrounding spaces");

        // Ensure original is unchanged (immutability)
        assertFalse(original.getIgnoreSurroundingSpaces(), "Original CSVFormat should remain unchanged");

        // Also test that calling withIgnoreSurroundingSpaces(true) returns equivalent result
        CSVFormat modifiedExplicit = original.withIgnoreSurroundingSpaces(true);
        assertEquals(modified, modifiedExplicit, "withIgnoreSurroundingSpaces() and withIgnoreSurroundingSpaces(true) should be equivalent");
    }
}