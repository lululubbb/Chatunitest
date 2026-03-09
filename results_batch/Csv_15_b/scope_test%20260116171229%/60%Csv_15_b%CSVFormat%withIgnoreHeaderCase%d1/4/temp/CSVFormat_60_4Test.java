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

class CSVFormat_60_4Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreHeaderCase());

        CSVFormat modified = original.withIgnoreHeaderCase();
        assertNotNull(modified);
        assertTrue(modified.getIgnoreHeaderCase());

        // Also test withIgnoreHeaderCase(boolean) for completeness
        CSVFormat modifiedFalse = original.withIgnoreHeaderCase(false);
        assertNotNull(modifiedFalse);
        assertFalse(modifiedFalse.getIgnoreHeaderCase());

        // Ensure original instance is not mutated
        assertFalse(original.getIgnoreHeaderCase());

        // The returned instance is different from original
        assertNotSame(original, modified);
        assertNotSame(original, modifiedFalse);
    }
}