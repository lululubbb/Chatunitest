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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CSVFormat_7_1Test {

    @Test
    @Timeout(8000)
    public void testGetCommentMarker() {
        // Given
        char expectedCommentMarker = '#';
        CSVFormat csvFormat = new CSVFormat(',', '\"', QuoteMode.ALL_NON_NULL, expectedCommentMarker,
                '\\', true, false, "\r\n", null, null, false, true);

        // When
        char actualCommentMarker = csvFormat.getCommentMarker();

        // Then
        assertEquals(expectedCommentMarker, actualCommentMarker);
    }
}