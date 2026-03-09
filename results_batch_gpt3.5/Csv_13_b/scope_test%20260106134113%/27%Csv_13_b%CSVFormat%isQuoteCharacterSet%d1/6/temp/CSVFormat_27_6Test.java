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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_27_6Test {

    private CSVFormat createCSVFormatWithQuoteCharacter(Character quoteCharacter) {
        if (quoteCharacter == null) {
            return CSVFormat.DEFAULT.withQuote((Character) null);
        } else {
            return CSVFormat.DEFAULT.withQuote(quoteCharacter);
        }
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() {
        CSVFormat csvFormat = createCSVFormatWithQuoteCharacter('\"');
        boolean result = csvFormat.isQuoteCharacterSet();
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() {
        CSVFormat csvFormat = createCSVFormatWithQuoteCharacter(null);
        boolean result = csvFormat.isQuoteCharacterSet();
        assertFalse(result);
    }
}