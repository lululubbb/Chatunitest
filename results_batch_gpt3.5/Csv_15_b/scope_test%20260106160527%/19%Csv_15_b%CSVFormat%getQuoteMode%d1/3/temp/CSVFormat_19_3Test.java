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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class CSVFormat_19_3Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteMode returns the correct QuoteMode instance")
    void testGetQuoteMode() {
        // Using predefined CSVFormat constants with different quoteModes
        assertEquals(QuoteMode.MINIMAL, CSVFormat.DEFAULT.getQuoteMode(), "DEFAULT should have QuoteMode.MINIMAL");
        assertEquals(QuoteMode.MINIMAL, CSVFormat.EXCEL.getQuoteMode(), "EXCEL should have QuoteMode.MINIMAL");
        assertEquals(QuoteMode.MINIMAL, CSVFormat.INFORMIX_UNLOAD.getQuoteMode(), "INFORMIX_UNLOAD should have QuoteMode.MINIMAL");
        assertEquals(QuoteMode.MINIMAL, CSVFormat.INFORMIX_UNLOAD_CSV.getQuoteMode(), "INFORMIX_UNLOAD_CSV should have QuoteMode.MINIMAL");
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.MYSQL.getQuoteMode(), "MYSQL should have QuoteMode.ALL_NON_NULL");
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_CSV.getQuoteMode(), "POSTGRESQL_CSV should have QuoteMode.ALL_NON_NULL");
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_TEXT.getQuoteMode(), "POSTGRESQL_TEXT should have QuoteMode.ALL_NON_NULL");
        assertEquals(QuoteMode.MINIMAL, CSVFormat.RFC4180.getQuoteMode(), "RFC4180 should have QuoteMode.MINIMAL");
        assertEquals(QuoteMode.MINIMAL, CSVFormat.TDF.getQuoteMode(), "TDF should have QuoteMode.MINIMAL");

        // Create CSVFormat instances with explicit QuoteMode via withQuoteMode()
        CSVFormat formatAll = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        assertEquals(QuoteMode.ALL, formatAll.getQuoteMode(), "Custom format should have QuoteMode.ALL");

        CSVFormat formatNone = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NONE);
        assertEquals(QuoteMode.NONE, formatNone.getQuoteMode(), "Custom format should have QuoteMode.NONE");

        CSVFormat formatNonNumeric = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NON_NUMERIC);
        assertEquals(QuoteMode.NON_NUMERIC, formatNonNumeric.getQuoteMode(), "Custom format should have QuoteMode.NON_NUMERIC");

        CSVFormat formatAllNonNull = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        assertEquals(QuoteMode.ALL_NON_NULL, formatAllNonNull.getQuoteMode(), "Custom format should have QuoteMode.ALL_NON_NULL");
    }
}