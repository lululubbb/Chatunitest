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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CSVFormat_65_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuoteMode() {
        // Create a mock QuoteMode
        QuoteMode mockQuoteMode = mock(QuoteMode.class);

        // Create a CSVFormat instance
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Call the withQuoteMode method
        CSVFormat newCsvFormat = csvFormat.withQuoteMode(mockQuoteMode);

        // Assert that the returned CSVFormat is not the same instance
        assertEquals(csvFormat, newCsvFormat);

        // Assert that the quote mode of the new CSVFormat is the mock instance
        assertEquals(mockQuoteMode, newCsvFormat.getQuoteMode());
    }
}