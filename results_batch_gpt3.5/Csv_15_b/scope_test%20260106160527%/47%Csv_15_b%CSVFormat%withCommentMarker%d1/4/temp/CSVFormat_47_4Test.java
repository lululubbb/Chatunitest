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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_47_4Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat resultFormat = baseFormat.withCommentMarker(commentChar);

        assertNotNull(resultFormat);
        assertEquals(Character.valueOf(commentChar), resultFormat.getCommentMarker());
        // The original should remain unchanged (immutability)
        assertNull(baseFormat.getCommentMarker());
        assertNotSame(baseFormat, resultFormat);
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char_withNullChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentChar = '\0';

        CSVFormat resultFormat = baseFormat.withCommentMarker(commentChar);

        assertNotNull(resultFormat);
        // When commentChar is '\0', getCommentMarker() returns null per library behavior
        assertNull(resultFormat.getCommentMarker());
        assertNull(baseFormat.getCommentMarker());
        assertNotSame(baseFormat, resultFormat);
    }
}