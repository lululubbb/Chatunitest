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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
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
    void withHeader_nullResultSet_returnsFormatWithNullMetaData() throws SQLException, NoSuchFieldException, IllegalAccessException {
        CSVFormat result = csvFormatDefault.withHeader((ResultSet) null);
        // It should call withHeader with null metaData and return a CSVFormat instance
        assertNotNull(result);

        // Use reflection to check the private header field is null
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(result);
        assertNull(header);
    }

    @Test
    @Timeout(8000)
    void withHeader_nonNullResultSet_callsWithHeaderWithMetaData() throws SQLException, NoSuchFieldException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);

        CSVFormat formatSpy = spy(csvFormatDefault);
        // Stub the withHeader(ResultSetMetaData) method to avoid actual execution
        doReturn(csvFormatDefault).when(formatSpy).withHeader(any(ResultSetMetaData.class));

        CSVFormat result = formatSpy.withHeader(resultSet);

        verify(resultSet).getMetaData();
        verify(formatSpy).withHeader(metaData);
        assertEquals(csvFormatDefault, result);
    }

    @Test
    @Timeout(8000)
    void withHeader_resultSetGetMetaDataThrowsSQLException_propagatesException() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenThrow(new SQLException("metaData error"));

        SQLException thrown = assertThrows(SQLException.class, () -> csvFormatDefault.withHeader(resultSet));
        assertEquals("metaData error", thrown.getMessage());
    }
}