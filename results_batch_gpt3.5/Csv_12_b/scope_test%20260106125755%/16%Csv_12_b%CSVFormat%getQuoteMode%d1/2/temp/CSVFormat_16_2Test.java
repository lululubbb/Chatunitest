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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_16_2Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        // The default CSVFormat.DEFAULT has QuoteMode.ALL, not null
        assertEquals(QuoteMode.ALL, quoteMode, "Default CSVFormat should have QuoteMode.ALL");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomNonNull() {
        QuoteMode customMode = QuoteMode.MINIMAL;
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(customMode);
        QuoteMode quoteMode = format.getQuoteMode();
        assertEquals(customMode, quoteMode, "QuoteMode should be the custom one set");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomNull() throws Exception {
        // Since withQuoteMode(null) returns DEFAULT (quoteMode field non-null),
        // create a CSVFormat instance with quoteMode set to null via reflection.
        CSVFormat format = CSVFormat.DEFAULT;
        java.lang.reflect.Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);

        // Create a new CSVFormat instance with quoteMode null by copying DEFAULT and setting quoteMode to null
        CSVFormat formatWithNullQuoteMode;
        {
            java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class, String[].class,
                    boolean.class, boolean.class);
            ctor.setAccessible(true);
            formatWithNullQuoteMode = ctor.newInstance(
                    format.getDelimiter(),
                    format.getQuoteCharacter(),
                    null, // quoteMode null here
                    format.getCommentMarker(),
                    format.getEscapeCharacter(),
                    format.getIgnoreSurroundingSpaces(),
                    format.getIgnoreEmptyLines(),
                    format.getRecordSeparator(),
                    format.getNullString(),
                    format.getHeader(),
                    format.getSkipHeaderRecord(),
                    format.getAllowMissingColumnNames()
            );
        }

        assertNull(formatWithNullQuoteMode.getQuoteMode(), "QuoteMode should be null when explicitly set to null");
    }
}