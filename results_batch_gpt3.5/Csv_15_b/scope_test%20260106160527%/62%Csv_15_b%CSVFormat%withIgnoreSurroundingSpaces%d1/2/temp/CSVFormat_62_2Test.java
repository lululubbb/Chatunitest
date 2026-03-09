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

public class CSVFormat_62_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() {
        CSVFormat originalFormat = CSVFormat.DEFAULT;
        CSVFormat resultFormat = originalFormat.withIgnoreSurroundingSpaces(true);

        assertNotNull(resultFormat, "Resulting CSVFormat should not be null");
        assertTrue(resultFormat.getIgnoreSurroundingSpaces(), "IgnoreSurroundingSpaces should be true");

        // Original instance should remain unchanged (immutable pattern)
        assertFalse(originalFormat.getIgnoreSurroundingSpaces(), "Original CSVFormat should have ignoreSurroundingSpaces as false");

        // Calling withIgnoreSurroundingSpaces again on the result should still return a CSVFormat with ignoreSurroundingSpaces true
        CSVFormat secondResult = resultFormat.withIgnoreSurroundingSpaces(true);
        assertTrue(secondResult.getIgnoreSurroundingSpaces(), "Second result should also have ignoreSurroundingSpaces true");

        // The returned instance should be a new instance (immutability)
        assertNotSame(originalFormat, resultFormat, "withIgnoreSurroundingSpaces should return a new instance");
    }
}