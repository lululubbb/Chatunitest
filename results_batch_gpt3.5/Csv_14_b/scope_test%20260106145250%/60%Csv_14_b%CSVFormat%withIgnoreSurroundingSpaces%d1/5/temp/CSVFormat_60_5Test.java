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

public class CSVFormat_60_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_DefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withIgnoreSurroundingSpaces();
        assertNotNull(result);
        assertTrue(result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_FalseThenTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format.getIgnoreSurroundingSpaces());

        CSVFormat result = format.withIgnoreSurroundingSpaces();
        assertTrue(result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_AlreadyTrueReturnsSameInstance() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        CSVFormat result = format.withIgnoreSurroundingSpaces(true);
        // withIgnoreSurroundingSpaces(true) returns this if already true
        assertSame(format, result);
    }
}