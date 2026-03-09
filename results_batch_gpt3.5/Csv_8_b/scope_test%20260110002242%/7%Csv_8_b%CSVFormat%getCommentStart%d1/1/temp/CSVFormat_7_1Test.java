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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_7_1Test {

    @Test
    @Timeout(8000)
    void testGetCommentStart_whenNull() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart((Character) null);
        assertNull(format.getCommentStart());
    }

    @Test
    @Timeout(8000)
    void testGetCommentStart_whenChar() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        assertEquals(Character.valueOf('#'), format.getCommentStart());
    }
}