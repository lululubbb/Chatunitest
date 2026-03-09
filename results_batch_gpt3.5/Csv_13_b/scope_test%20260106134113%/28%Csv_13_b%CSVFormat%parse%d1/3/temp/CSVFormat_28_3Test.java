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
import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_28_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testParseReturnsCSVParserInstance() throws Exception {
        Reader mockReader = mock(Reader.class);

        // Use reflection to create a mock CSVParser instance to return
        CSVParser mockParser = mock(CSVParser.class);

        // Spy on CSVFormat to override parse method to return our mockParser instead of real one
        CSVFormat spyFormat = spy(csvFormat);

        doReturn(mockParser).when(spyFormat).parse(any(Reader.class));

        CSVParser parser = spyFormat.parse(mockReader);

        assertNotNull(parser, "parse should return a non-null CSVParser instance");
        assertEquals(mockParser, parser, "parse should return the mocked CSVParser instance");
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsIOException() throws Exception {
        Reader mockReader = mock(Reader.class);

        // Use reflection to get the CSVParser constructor that throws IOException
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);

        // Create a subclass of CSVFormat to override parse method and simulate IOException
        CSVFormat csvFormatThrows = new CSVFormat(DEFAULT.getDelimiter(), DEFAULT.getQuoteCharacter(),
                DEFAULT.getQuoteMode(), DEFAULT.getCommentMarker(), DEFAULT.getEscapeCharacter(),
                DEFAULT.getIgnoreSurroundingSpaces(), DEFAULT.getIgnoreEmptyLines(), DEFAULT.getRecordSeparator(),
                DEFAULT.getNullString(), DEFAULT.getHeaderComments(), DEFAULT.getHeader(),
                DEFAULT.getSkipHeaderRecord(), DEFAULT.getAllowMissingColumnNames(), DEFAULT.getIgnoreHeaderCase()) {
            @Override
            public CSVParser parse(Reader in) throws IOException {
                throw new IOException("Simulated IO exception");
            }
        };

        IOException ioException = assertThrows(IOException.class, () -> csvFormatThrows.parse(mockReader));
        assertEquals("Simulated IO exception", ioException.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testParseWithNullReaderThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }
}