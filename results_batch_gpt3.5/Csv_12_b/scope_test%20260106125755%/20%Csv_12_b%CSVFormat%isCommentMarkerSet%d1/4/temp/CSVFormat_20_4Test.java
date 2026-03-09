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

public class CSVFormat_20_4Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenSet() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        assertTrue(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSetWhenSetNullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        assertFalse(format.isCommentMarkerSet());
    }
}