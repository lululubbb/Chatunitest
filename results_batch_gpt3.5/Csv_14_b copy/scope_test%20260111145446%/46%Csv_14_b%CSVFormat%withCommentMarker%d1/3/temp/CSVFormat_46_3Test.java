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
import org.mockito.Mockito;

public class CSVFormat_46_3Test {

    @Test
    @Timeout(8000)
    public void testWithCommentMarker() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Character commentMarker = '#';

        // When
        CSVFormat newCsvFormat = csvFormat.withCommentMarker(commentMarker);

        // Then
        assertEquals(commentMarker, newCsvFormat.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_LineBreak() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Character lineBreak = '\n';

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withCommentMarker(lineBreak));

        // Then
        assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_Null() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Character nullMarker = null;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withCommentMarker(nullMarker));

        // Then
        assertEquals("The comment start marker character cannot be null", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithCommentMarker_LineBreakCharacter() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Character lineBreak = '\n';

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withCommentMarker(lineBreak));

        // Then
        assertEquals("The comment start marker character cannot be a line break", exception.getMessage());
    }

    // Additional tests for other methods can be added here
}