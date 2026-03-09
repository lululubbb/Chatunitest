package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_9_1Test {

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_DefaultNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character commentMarker = format.getCommentMarker();
        assertNull(commentMarker, "Default CSVFormat should have null commentMarker");
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_WithCommentMarkerChar() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        Character commentMarker = format.getCommentMarker();
        assertNotNull(commentMarker, "Comment marker should not be null");
        assertEquals(Character.valueOf('#'), commentMarker);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarker_WithCommentMarkerCharacterNull() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        Character commentMarker = format.getCommentMarker();
        assertNull(commentMarker, "Comment marker explicitly set to null should return null");
    }

}