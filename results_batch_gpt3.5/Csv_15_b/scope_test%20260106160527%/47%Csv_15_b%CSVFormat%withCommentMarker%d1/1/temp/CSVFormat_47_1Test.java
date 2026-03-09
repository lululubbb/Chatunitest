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

class CSVFormat_47_1Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentChar = '#';

        CSVFormat newFormat = baseFormat.withCommentMarker(commentChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(commentChar), newFormat.getCommentMarker());

        // Original format should remain unchanged
        assertNull(baseFormat.getCommentMarker());

        // Test with a different comment char
        char anotherCommentChar = '!';
        CSVFormat anotherFormat = newFormat.withCommentMarker(anotherCommentChar);
        assertEquals(Character.valueOf(anotherCommentChar), anotherFormat.getCommentMarker());

        // Test with comment char as null (no comment marker)
        CSVFormat nullCharFormat = baseFormat.withCommentMarker((Character) null);
        // The API treats null as "no comment marker", so getCommentMarker() returns null
        assertNull(nullCharFormat.getCommentMarker());
    }
}