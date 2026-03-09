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

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_66_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Test with typical line break char '\n' (LF)
        CSVFormat resultLf = format.withRecordSeparator('\n');
        assertNotNull(resultLf);
        assertEquals("\n", resultLf.getRecordSeparator());

        // Test with typical line break char '\r' (CR)
        CSVFormat resultCr = format.withRecordSeparator('\r');
        assertNotNull(resultCr);
        assertEquals("\r", resultCr.getRecordSeparator());

        // Test with special char '|'
        char specialChar = '|';
        CSVFormat resultSpecial = format.withRecordSeparator(specialChar);
        assertNotNull(resultSpecial);
        assertEquals(String.valueOf(specialChar), resultSpecial.getRecordSeparator());

        // Test with non-printable char (e.g. tab)
        char tab = '\t';
        CSVFormat resultTab = format.withRecordSeparator(tab);
        assertNotNull(resultTab);
        assertEquals(String.valueOf(tab), resultTab.getRecordSeparator());

        // Test that original format is unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), format.getRecordSeparator());
    }
}