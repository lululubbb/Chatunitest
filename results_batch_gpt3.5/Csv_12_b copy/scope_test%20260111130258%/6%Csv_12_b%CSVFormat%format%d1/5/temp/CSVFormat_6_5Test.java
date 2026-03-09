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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_6_5Test {

    @Test
    @Timeout(8000)
    public void testFormat() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        StringWriter out = new StringWriter();
        CSVPrinter csvPrinter = Mockito.mock(CSVPrinter.class);
        Mockito.when(csvPrinter.printRecord(Mockito.any(Object[].class))).thenReturn(csvPrinter);

        String result = csvFormat.format("value1", "value2");

        assertEquals("value1,value2", result.trim());
    }
}