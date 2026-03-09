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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_18_3Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter() throws NoSuchFieldException, IllegalAccessException {
        // Create a mock CSVFormat object
        CSVFormat csvFormat = mock(CSVFormat.class);

        // Set the quoteCharacter field to a specific value using reflection
        char expectedQuoteCharacter = '"';
        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        quoteCharacterField.set(csvFormat, expectedQuoteCharacter);

        // Mock the behavior of getQuoteCharacter method
        when(csvFormat.getQuoteCharacter()).thenCallRealMethod();

        // Test the getQuoteCharacter method
        assertEquals(expectedQuoteCharacter, csvFormat.getQuoteCharacter().charValue());
    }
}