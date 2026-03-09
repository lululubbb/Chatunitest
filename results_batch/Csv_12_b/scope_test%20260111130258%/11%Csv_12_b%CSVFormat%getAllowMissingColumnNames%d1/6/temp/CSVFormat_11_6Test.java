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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CSVFormat_11_6Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames() throws Exception {
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote(null)
                .withQuoteMode(null)
                .withCommentMarker(null)
                .withEscape(null)
                .withIgnoreSurroundingSpaces(false)
                .withAllowMissingColumnNames(true)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n")
                .withNullString(null)
                .withHeader((String[]) null)
                .withSkipHeaderRecord(false);

        boolean result = csvFormat.getAllowMissingColumnNames();

        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_DefaultValue() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        boolean result = csvFormat.getAllowMissingColumnNames();

        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_RFC4180() throws Exception {
        CSVFormat csvFormat = CSVFormat.RFC4180;

        boolean result = csvFormat.getAllowMissingColumnNames();

        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Excel() throws Exception {
        CSVFormat csvFormat = CSVFormat.EXCEL;

        boolean result = csvFormat.getAllowMissingColumnNames();

        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_TDF() throws Exception {
        CSVFormat csvFormat = CSVFormat.TDF;

        boolean result = csvFormat.getAllowMissingColumnNames();

        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_MYSQL() throws Exception {
        CSVFormat csvFormat = CSVFormat.MYSQL;

        boolean result = csvFormat.getAllowMissingColumnNames();

        assertEquals(false, result);
    }
}