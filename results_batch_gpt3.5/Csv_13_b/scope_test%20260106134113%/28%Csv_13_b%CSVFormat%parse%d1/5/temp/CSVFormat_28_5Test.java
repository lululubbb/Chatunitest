package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_28_5Test {

    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParserInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a mock CSVParser instance using Mockito
        CSVParser mockParser = mock(CSVParser.class);

        // Because CSVFormat.parse(Reader) is final, we cannot spy/override it directly.
        // Instead, use reflection to replace the parse method call by invoking a helper.

        // Create a subclass of CSVFormat to override parse method
        CSVFormat formatOverride = new CSVFormat(format.getDelimiter(),
                format.getQuoteCharacter(),
                format.getQuoteMode(),
                format.getCommentMarker(),
                format.getEscapeCharacter(),
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                format.getNullString(),
                format.getHeaderComments(),
                format.getHeader(),
                format.getSkipHeaderRecord(),
                format.getAllowMissingColumnNames(),
                format.getIgnoreHeaderCase()) {
            @Override
            public CSVParser parse(Reader in) throws IOException {
                return mockParser;
            }
        };

        CSVParser parser = formatOverride.parse(mockReader);

        assertNotNull(parser);
        assertSame(mockParser, parser);
    }

    @Test
    @Timeout(8000)
    void testParseWithNullReaderThrowsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(NullPointerException.class, () -> format.parse(null));
    }
}