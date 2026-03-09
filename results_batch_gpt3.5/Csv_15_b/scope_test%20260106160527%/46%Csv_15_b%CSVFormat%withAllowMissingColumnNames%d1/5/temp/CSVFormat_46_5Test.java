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

class CSVFormat_46_5Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesTrue() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat modified = original.withAllowMissingColumnNames(true);

        assertNotSame(original, modified, "withAllowMissingColumnNames should return a new CSVFormat instance");
        assertTrue(modified.getAllowMissingColumnNames(), "allowMissingColumnNames should be true");
        // Original should remain unchanged
        assertFalse(original.getAllowMissingColumnNames(), "original allowMissingColumnNames should remain false");
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesFalse() {
        CSVFormat original = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat modified = original.withAllowMissingColumnNames(false);

        assertNotSame(original, modified, "withAllowMissingColumnNames should return a new CSVFormat instance");
        assertFalse(modified.getAllowMissingColumnNames(), "allowMissingColumnNames should be false");
        // Original should remain unchanged
        assertTrue(original.getAllowMissingColumnNames(), "original allowMissingColumnNames should remain true");
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNamesDoesNotAffectOtherFields() {
        CSVFormat original = CSVFormat.DEFAULT.withDelimiter(';').withIgnoreEmptyLines(false);
        CSVFormat modified = original.withAllowMissingColumnNames(true);

        assertEquals(original.getDelimiter(), modified.getDelimiter(), "Delimiter should remain unchanged");
        assertEquals(original.getIgnoreEmptyLines(), modified.getIgnoreEmptyLines(), "IgnoreEmptyLines should remain unchanged");
        assertTrue(modified.getAllowMissingColumnNames(), "allowMissingColumnNames should be true");
    }
}