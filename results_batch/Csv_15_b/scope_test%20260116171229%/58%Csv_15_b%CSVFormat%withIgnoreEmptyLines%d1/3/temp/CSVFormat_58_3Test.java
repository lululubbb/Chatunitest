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

class CSVFormat_58_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertTrue(original.getIgnoreEmptyLines());

        CSVFormat result = original.withIgnoreEmptyLines();
        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines());

        // The withIgnoreEmptyLines() should return a new instance or the same if already true
        if (result != original) {
            // If new instance, its ignoreEmptyLines must be true
            assertTrue(result.getIgnoreEmptyLines());
        }

        // Also test that withIgnoreEmptyLines(false) disables ignoring empty lines
        CSVFormat disabled = original.withIgnoreEmptyLines(false);
        assertNotNull(disabled);
        assertFalse(disabled.getIgnoreEmptyLines());
    }
}