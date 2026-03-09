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

class CSVFormat_45_3Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_withChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(commentChar), updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_withChar_nullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '\0';

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_effectOnOtherFields() {
        CSVFormat original = CSVFormat.DEFAULT.withIgnoreEmptyLines(false).withAllowMissingColumnNames();
        char commentChar = '!';

        CSVFormat updated = original.withCommentMarker(commentChar);

        assertEquals(Character.valueOf(commentChar), updated.getCommentMarker());
        assertEquals(original.getIgnoreEmptyLines(), updated.getIgnoreEmptyLines());
        assertEquals(original.getAllowMissingColumnNames(), updated.getAllowMissingColumnNames());
        assertEquals(original.getDelimiter(), updated.getDelimiter());
        assertEquals(original.getQuoteCharacter(), updated.getQuoteCharacter());
        assertEquals(original.getRecordSeparator(), updated.getRecordSeparator());
    }
}