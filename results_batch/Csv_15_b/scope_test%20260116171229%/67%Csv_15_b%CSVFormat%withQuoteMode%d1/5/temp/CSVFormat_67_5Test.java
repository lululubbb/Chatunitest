package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_67_5Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;
        CSVFormat modified = original.withQuoteMode(newQuoteMode);

        assertNotNull(modified);
        assertNotSame(original, modified);
        assertEquals(newQuoteMode, modified.getQuoteMode());

        // Original should remain unchanged
        assertNull(original.getQuoteMode());

        // Test with null QuoteMode
        CSVFormat nullQuoteModeFormat = original.withQuoteMode(null);
        assertNotNull(nullQuoteModeFormat);
        assertNotSame(original, nullQuoteModeFormat);
        assertNull(nullQuoteModeFormat.getQuoteMode());
    }
}