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

class CSVFormat_56_1Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertTrue(original.getIgnoreEmptyLines(), "Default CSVFormat should ignore empty lines");

        CSVFormat result = original.withIgnoreEmptyLines(true);
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getIgnoreEmptyLines(), "Result should ignore empty lines");

        CSVFormat modified = original.withIgnoreEmptyLines(false);
        assertFalse(modified.getIgnoreEmptyLines(), "Modified CSVFormat should not ignore empty lines");

        CSVFormat result2 = modified.withIgnoreEmptyLines(true);
        assertTrue(result2.getIgnoreEmptyLines(), "Result2 should ignore empty lines");

        assertNotSame(original, result, "withIgnoreEmptyLines should return new instance");
        assertNotSame(modified, result2, "withIgnoreEmptyLines should return new instance");
    }
}