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

class CSVFormat_45_6Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_withChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat result = original.withCommentMarker(commentChar);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_withChar_nullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char commentChar = '\0';

        CSVFormat result = original.withCommentMarker(commentChar);

        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testWithCommentMarker_withChar_sameAsOriginal() {
        Character existingComment = CSVFormat.DEFAULT.getCommentMarker();
        char commentChar = existingComment != null ? existingComment : '#';

        CSVFormat original = CSVFormat.DEFAULT.withCommentMarker(Character.valueOf(commentChar));

        CSVFormat result = original.withCommentMarker(commentChar);

        assertNotNull(result);
        assertSame(original, result);
        assertEquals(Character.valueOf(commentChar), result.getCommentMarker());
    }
}