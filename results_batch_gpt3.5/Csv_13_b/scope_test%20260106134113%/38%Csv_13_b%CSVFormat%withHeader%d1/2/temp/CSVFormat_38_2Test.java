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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatWithHeaderTest {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withNullResultSet_returnsWithHeaderNull() throws SQLException {
        CSVFormat result = csvFormatDefault.withHeader((ResultSet) null);
        // The call should delegate to withHeader(ResultSetMetaData) with null argument
        CSVFormat expected = csvFormatDefault.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_withResultSet_callsGetMetaDataAndReturnsWithHeader() throws SQLException {
        ResultSetMetaData metaDataMock = mock(ResultSetMetaData.class);
        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getMetaData()).thenReturn(metaDataMock);

        CSVFormat result = csvFormatDefault.withHeader(resultSetMock);

        verify(resultSetMock, times(1)).getMetaData();
        CSVFormat expected = csvFormatDefault.withHeader(metaDataMock);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_privateMethodInvocation_withNull() throws Exception {
        Method withHeaderMetaData = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMetaData.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMetaData.invoke(csvFormatDefault, new Object[]{null});
        CSVFormat expected = csvFormatDefault.withHeader((ResultSetMetaData) null);
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_privateMethodInvocation_withMockedMetaData() throws Exception {
        // We test that withHeader(ResultSetMetaData) returns a CSVFormat instance (cannot assert more without source)
        ResultSetMetaData metaDataMock = mock(ResultSetMetaData.class);

        Method withHeaderMetaData = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMetaData.setAccessible(true);

        CSVFormat result = (CSVFormat) withHeaderMetaData.invoke(csvFormatDefault, metaDataMock);

        assertNotNull(result);
        assertTrue(result instanceof CSVFormat);
    }
}