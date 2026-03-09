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

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import java.lang.reflect.Field;

public class CSVFormat_20_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreEmptyLines(false)
                .withRecordSeparator("\r\n");
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        commentMarkerField.set(csvFormat, '!');
        
        // When
        boolean result = csvFormat.isCommentMarkerSet();
        
        // Then
        assertTrue(result);
    }
}