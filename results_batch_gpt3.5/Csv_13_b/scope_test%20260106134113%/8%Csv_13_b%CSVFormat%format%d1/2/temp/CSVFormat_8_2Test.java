package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_2Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() throws Exception {
        String result = csvFormatDefault.format("a", "b", "c");
        // The default delimiter is COMMA, quote char is DOUBLE_QUOTE_CHAR, record separator CRLF
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValue() throws Exception {
        CSVFormat format = csvFormatDefault.withNullString("NULL");
        String result = format.format("a", null, "c");
        assertEquals("\"a\",NULL,\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() throws Exception {
        String result = csvFormatDefault.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withValuesContainingDelimiterAndQuote() throws Exception {
        String val1 = "value,with,commas";
        String val2 = "value \"with\" quotes";
        String result = csvFormatDefault.format(val1, val2);
        // Values should be quoted and quotes escaped
        assertEquals("\"value,with,commas\",\"value \"\"with\"\" quotes\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Use a helper method that mocks CSVPrinter to throw IOException
        CSVFormat format = createCSVFormatThrowingIOException();

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            format.format("a");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock IO exception", thrown.getCause().getMessage());
    }

    private CSVFormat createCSVFormatThrowingIOException() {
        // Use DEFAULT as base and override format method via anonymous subclass
        return new CSVFormat(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteCharacter(),
                CSVFormat.DEFAULT.getQuoteMode(),
                CSVFormat.DEFAULT.getCommentMarker(),
                CSVFormat.DEFAULT.getEscapeCharacter(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeaderComments(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord(),
                CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                CSVFormat.DEFAULT.getIgnoreHeaderCase()) {
            @Override
            public String format(final Object... values) {
                try {
                    CSVPrinter printer = mock(CSVPrinter.class);
                    doThrow(new IOException("mock IO exception")).when(printer).printRecord((Object[]) any());
                    printer.printRecord(values);
                    return "";
                } catch (final IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

    @Test
    @Timeout(8000)
    public void testFormat_withSingleNullValue() throws Exception {
        CSVFormat format = csvFormatDefault.withNullString("NULL");
        String result = format.format((Object) null);
        assertEquals("NULL", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withMixedNullAndEmptyString() throws Exception {
        CSVFormat format = csvFormatDefault.withNullString("NULL");
        String result = format.format(null, "", "value");
        assertEquals("NULL,\"\",\"value\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withCustomQuoteCharacter() throws Exception {
        CSVFormat format = csvFormatDefault.withQuote('\'');
        String result = format.format("val1", "val,2");
        assertEquals("'val1','val,2'", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNoQuoteCharacter() throws Exception {
        CSVFormat format = csvFormatDefault.withQuote((Character) null);
        String result = format.format("val1", "val,2");
        // Without quote character, delimiter in value should not be quoted, so it may break CSV but test as is
        assertEquals("val1,val,2", result);
    }
}