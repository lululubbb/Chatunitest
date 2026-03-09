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

public class CSVFormat_58_6Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        CSVFormat format = CSVFormat.DEFAULT;
        // Call the focal method
        CSVFormat newFormat = format.withIgnoreEmptyLines();

        // It should return the same instance because DEFAULT already has ignoreEmptyLines = true
        assertSame(format, newFormat);

        // The instance should have ignoreEmptyLines set to true
        assertTrue(newFormat.getIgnoreEmptyLines());

        // The original instance should remain unchanged (DEFAULT has ignoreEmptyLines = true)
        assertTrue(format.getIgnoreEmptyLines());

        // Also test that withIgnoreEmptyLines(true) returns the same instance with ignoreEmptyLines true
        CSVFormat formatTrue = format.withIgnoreEmptyLines(true);
        assertSame(format, formatTrue);
        assertTrue(formatTrue.getIgnoreEmptyLines());

        // Test that withIgnoreEmptyLines(false) returns a different instance with ignoreEmptyLines false
        CSVFormat formatFalse = format.withIgnoreEmptyLines(false);
        assertNotSame(format, formatFalse);
        assertFalse(formatFalse.getIgnoreEmptyLines());
    }
}