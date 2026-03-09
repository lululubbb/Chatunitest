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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_7_3Test {

    @Test
    @Timeout(8000)
    void testGetCommentMarkerWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    void testGetCommentMarkerWhenSetWithChar() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        Character cm = format.getCommentMarker();
        assertNotNull(cm);
        assertEquals(Character.valueOf('#'), cm);
    }

    @Test
    @Timeout(8000)
    void testGetCommentMarkerWhenSetWithCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(Character.valueOf(';'));
        Character cm = format.getCommentMarker();
        assertNotNull(cm);
        assertEquals(Character.valueOf(';'), cm);
    }
}