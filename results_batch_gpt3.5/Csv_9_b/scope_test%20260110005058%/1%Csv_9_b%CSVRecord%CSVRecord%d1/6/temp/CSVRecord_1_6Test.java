package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_1_6Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[]{"a", "b", "c"};
        mapping = new HashMap<>();
        mapping.put("first", 0);
        mapping.put("second", 1);
        mapping.put("third", 2);
        comment = "comment";
        recordNumber = 42L;
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithNonNullValues() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(3, record.size());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get("second"));
        assertEquals("c", record.get(Enum.valueOf(TestEnum.class, "THIRD")));
        assertTrue(record.isMapped("first"));
        assertTrue(record.isSet("first"));
        assertTrue(record.isConsistent());
        Iterator<String> it = record.iterator();
        List<String> iterated = new ArrayList<>();
        while (it.hasNext()) {
            iterated.add(it.next());
        }
        assertEquals(Arrays.asList(values), iterated);
        Map<String, String> map = record.toMap();
        assertEquals("a", map.get("first"));
        assertEquals("b", map.get("second"));
        assertEquals("c", map.get("third"));
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(values.length, record.size());
        // Use reflection to call private toList method instead of direct call
        Method toList = CSVRecord.class.getDeclaredMethod("toList");
        toList.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> toListResult = (List<String>) toList.invoke(record);
        assertEquals(values.length, toListResult.size());
        assertEquals(values.length, record.toMap().size());
        assertTrue(record.toString().contains("a"));
    }

    @Test
    @Timeout(8000)
    public void testConstructorWithNullValuesUsesEmptyArray() throws Exception {
        CSVRecord record = new CSVRecord(null, mapping, comment, recordNumber);
        assertEquals(0, record.size());
        assertFalse(record.isConsistent());
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetWithInvalidIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    @Timeout(8000)
    public void testGetWithInvalidName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertThrows(IllegalArgumentException.class, () -> record.get("invalid"));
    }

    @Test
    @Timeout(8000)
    public void testGetWithNullName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertThrows(IllegalArgumentException.class, () -> record.get((String) null));
    }

    @Test
    @Timeout(8000)
    public void testGetWithNullEnum() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertThrows(NullPointerException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testIsMappedReturnsFalseForNullOrMissing() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isMapped(null));
        assertFalse(record.isMapped("missing"));
    }

    @Test
    @Timeout(8000)
    public void testIsSetReturnsFalseForNullOrMissing() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertFalse(record.isSet(null));
        assertFalse(record.isSet("missing"));
    }

    @Test
    @Timeout(8000)
    public void testPutInCopiesEntries() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> target = new HashMap<>();
        Map<String, String> result = record.putIn(target);
        assertSame(target, result);
        assertEquals("a", target.get("first"));
        assertEquals("b", target.get("second"));
        assertEquals("c", target.get("third"));
    }

    @Test
    @Timeout(8000)
    public void testPrivateToListMethod() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Method toList = CSVRecord.class.getDeclaredMethod("toList");
        toList.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toList.invoke(record);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testValuesMethodReturnsArray() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, vals);
    }

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }
}