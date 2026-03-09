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

class CSVFormat_45_2Test {

    @Test
    @Timeout(8000)
    void testWithCommentMarker_char() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char commentChar = '#';

        // Invoke the focal method
        CSVFormat resultFormat = baseFormat.withCommentMarker(commentChar);

        assertNotNull(resultFormat, "Resulting CSVFormat should not be null");
        assertEquals(Character.valueOf(commentChar), resultFormat.getCommentMarker(),
                "Comment marker should be set to the given character");

        // Verify immutability: original format's commentMarker should remain unchanged
        assertNull(baseFormat.getCommentMarker(), "Original CSVFormat commentMarker should remain null");

        // Also test that the returned format is a different instance
        assertNotSame(baseFormat, resultFormat, "Returned CSVFormat should be a different instance");
    }
}