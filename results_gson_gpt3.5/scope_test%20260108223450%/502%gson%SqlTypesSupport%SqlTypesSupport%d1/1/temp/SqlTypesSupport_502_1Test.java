package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.TypeAdapterFactory;
import java.util.Date;

public class SqlTypesSupport_502_1Test {

  @Test
    @Timeout(8000)
  public void testStaticFieldsInitialization() throws Exception {
    Class<SqlTypesSupport> clazz = SqlTypesSupport.class;

    // SUPPORTS_SQL_TYPES
    Field supportsSqlTypesField = clazz.getField("SUPPORTS_SQL_TYPES");
    assertTrue(Modifier.isStatic(supportsSqlTypesField.getModifiers()));
    assertTrue(Modifier.isFinal(supportsSqlTypesField.getModifiers()));
    Object supportsSqlTypesValue = supportsSqlTypesField.get(null);
    assertNotNull(supportsSqlTypesValue);
    assertTrue(supportsSqlTypesValue instanceof Boolean);

    // DATE_DATE_TYPE
    Field dateDateTypeField = clazz.getField("DATE_DATE_TYPE");
    assertTrue(Modifier.isStatic(dateDateTypeField.getModifiers()));
    assertTrue(Modifier.isFinal(dateDateTypeField.getModifiers()));
    Object dateDateTypeValue = dateDateTypeField.get(null);
    assertNotNull(dateDateTypeValue);
    assertTrue(dateDateTypeValue instanceof DateType);
    
    // TIMESTAMP_DATE_TYPE
    Field timestampDateTypeField = clazz.getField("TIMESTAMP_DATE_TYPE");
    assertTrue(Modifier.isStatic(timestampDateTypeField.getModifiers()));
    assertTrue(Modifier.isFinal(timestampDateTypeField.getModifiers()));
    Object timestampDateTypeValue = timestampDateTypeField.get(null);
    assertNotNull(timestampDateTypeValue);
    assertTrue(timestampDateTypeValue instanceof DateType);

    // DATE_FACTORY
    Field dateFactoryField = clazz.getField("DATE_FACTORY");
    assertTrue(Modifier.isStatic(dateFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(dateFactoryField.getModifiers()));
    Object dateFactoryValue = dateFactoryField.get(null);
    assertNotNull(dateFactoryValue);
    assertTrue(dateFactoryValue instanceof TypeAdapterFactory);

    // TIME_FACTORY
    Field timeFactoryField = clazz.getField("TIME_FACTORY");
    assertTrue(Modifier.isStatic(timeFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(timeFactoryField.getModifiers()));
    Object timeFactoryValue = timeFactoryField.get(null);
    assertNotNull(timeFactoryValue);
    assertTrue(timeFactoryValue instanceof TypeAdapterFactory);

    // TIMESTAMP_FACTORY
    Field timestampFactoryField = clazz.getField("TIMESTAMP_FACTORY");
    assertTrue(Modifier.isStatic(timestampFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(timestampFactoryField.getModifiers()));
    Object timestampFactoryValue = timestampFactoryField.get(null);
    assertNotNull(timestampFactoryValue);
    assertTrue(timestampFactoryValue instanceof TypeAdapterFactory);
  }
}