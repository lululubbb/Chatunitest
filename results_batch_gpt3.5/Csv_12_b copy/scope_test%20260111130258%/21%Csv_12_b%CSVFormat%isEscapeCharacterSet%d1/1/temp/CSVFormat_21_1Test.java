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

import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVFormat_21_1Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet() throws Exception {
        // Given
        CSVFormat csvFormat = Mockito.spy(CSVFormat.DEFAULT);

        // When
        boolean isEscapeCharacterSet = csvFormat.isEscapeCharacterSet();

        // Then
        assertFalse(isEscapeCharacterSet);
    }
}