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

class CSVFormatValueOfTest {

    @Test
    @Timeout(8000)
    void testValueOf_WithValidFormatName_ReturnsExpectedFormat() throws Exception {
        CSVFormat expectedFormat = CSVFormat.DEFAULT;
        CSVFormat actualFormat = CSVFormat.valueOf("DEFAULT");
        assertEquals(expectedFormat.getDelimiter(), actualFormat.getDelimiter());
        assertEquals(expectedFormat.getQuoteCharacter(), actualFormat.getQuoteCharacter());
        assertEquals(expectedFormat.getRecordSeparator(), actualFormat.getRecordSeparator());
        // Add more property comparisons if needed
    }

    @Test
    @Timeout(8000)
    void testValueOf_WithAnotherValidFormatName_ReturnsExpectedFormat() throws Exception {
        CSVFormat expectedFormat = CSVFormat.EXCEL;
        CSVFormat actualFormat = CSVFormat.valueOf("EXCEL");
        assertEquals(expectedFormat.getDelimiter(), actualFormat.getDelimiter());
        assertEquals(expectedFormat.getQuoteCharacter(), actualFormat.getQuoteCharacter());
        assertEquals(expectedFormat.getRecordSeparator(), actualFormat.getRecordSeparator());
        // Add more property comparisons if needed
    }

    @Test
    @Timeout(8000)
    void testValueOf_WithNull_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    void testValueOf_WithInvalidFormatName_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("INVALID_FORMAT"));
    }
}