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

public class CSVFormat_9_6Test {

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_DefaultIsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = format.getCommentMarker();
        assertNull(commentMarker, "Default commentMarker should be null");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_WithCommentMarkerChar() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        Character commentMarker = format.getCommentMarker();
        assertNotNull(commentMarker, "CommentMarker should not be null after setting");
        assertEquals(Character.valueOf('#'), commentMarker, "CommentMarker should be '#'");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_WithCommentMarkerCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(Character.valueOf(';'));
        Character commentMarker = format.getCommentMarker();
        assertNotNull(commentMarker, "CommentMarker should not be null after setting");
        assertEquals(Character.valueOf(';'), commentMarker, "CommentMarker should be ';'");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_WithNullCommentMarker() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        Character commentMarker = format.getCommentMarker();
        assertNull(commentMarker, "CommentMarker should be null after setting null");
    }
}