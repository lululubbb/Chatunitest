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
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_8_6Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter() throws Exception {
        // Create a CSVFormat object with delimiter as COMMA for testing
        CSVFormat csvFormat = createCSVFormat();

        // Use reflection to access the private field 'delimiter'
        char expectedDelimiter = Constants.COMMA;
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char actualDelimiter = (char) delimiterField.get(csvFormat);

        // Verify that the getDelimiter method returns the correct delimiter
        assertEquals(expectedDelimiter, csvFormat.getDelimiter());
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    private CSVFormat createCSVFormat() {
        try {
            return CSVFormat.newFormat(Constants.COMMA);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}