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

public class CSVFormat_20_3Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenNotSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isCommentMarkerSet(), "Comment marker should not be set by default");
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenSetWithChar() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        assertTrue(format.isCommentMarkerSet(), "Comment marker should be set when specified with char");
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenSetWithCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(Character.valueOf(';'));
        assertTrue(format.isCommentMarkerSet(), "Comment marker should be set when specified with Character");
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenSetToNull() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        assertFalse(format.isCommentMarkerSet(), "Comment marker should not be set when specified as null");
    }
}