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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_15_4Test {

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("getQuotePolicy");
        Quote quotePolicy = (Quote) method.invoke(format);
        assertNull(quotePolicy, "Default CSVFormat quotePolicy should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_CustomQuotePolicy() throws Exception {
        Quote customQuotePolicy = Quote.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(customQuotePolicy);
        Method method = CSVFormat.class.getMethod("getQuotePolicy");
        Quote quotePolicy = (Quote) method.invoke(format);
        assertSame(customQuotePolicy, quotePolicy, "Quote policy should be the custom one set");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_NullQuotePolicy() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(null);
        Method method = CSVFormat.class.getMethod("getQuotePolicy");
        Quote quotePolicy = (Quote) method.invoke(format);
        assertNull(quotePolicy, "Quote policy should be null when set to null");
    }
}