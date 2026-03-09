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

import org.junit.jupiter.api.Test;

class CSVFormat_18_5Test {

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter() throws NoSuchFieldException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withDelimiter(',')
                .withEscape(null)
                .withQuoteMode(null)
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n")
                .withNullString(null)
                .withHeader((String[]) null) // Fixing the withHeader ambiguity by specifying the type
                .withHeaderComments((Object[]) null) // Fixing the varargs call warning
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(false)
                .withIgnoreHeaderCase(false)
                .withTrim(false)
                .withTrailingDelimiter(false);

        // Using reflection to access and modify private field
        java.lang.reflect.Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        quoteCharacterField.set(csvFormat, '\'');

        // When
        Character actualQuoteCharacter = csvFormat.getQuoteCharacter();

        // Then
        assertEquals('\'', actualQuoteCharacter);
    }
}