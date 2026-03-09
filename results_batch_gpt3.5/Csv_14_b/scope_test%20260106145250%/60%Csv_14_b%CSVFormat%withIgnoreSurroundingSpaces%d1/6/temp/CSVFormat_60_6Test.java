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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_60_6Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreSurroundingSpaces());

        CSVFormat modified = original.withIgnoreSurroundingSpaces(true);
        assertNotNull(modified);
        assertTrue(modified.getIgnoreSurroundingSpaces());

        // Calling withIgnoreSurroundingSpaces(true) again on the modified instance returns the same instance (idempotent)
        CSVFormat modifiedAgain = modified.withIgnoreSurroundingSpaces(true);
        assertSame(modified, modifiedAgain);

        // Check that original instance is unchanged
        assertFalse(original.getIgnoreSurroundingSpaces());
    }
}