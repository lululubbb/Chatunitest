package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import java.sql.Timestamp;
import java.util.Date;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class SqlTypesSupport_502_3Test {

  @Test
    @Timeout(8000)
  void testStaticFieldsInitialization() throws Exception {
    Class<SqlTypesSupport> clazz = SqlTypesSupport.class;

    // SUPPORTS_SQL_TYPES should be a boolean and initialized
    Field supportsSqlTypesField = clazz.getField("SUPPORTS_SQL_TYPES");
    assertTrue(Modifier.isStatic(supportsSqlTypesField.getModifiers()));
    assertTrue(Modifier.isFinal(supportsSqlTypesField.getModifiers()));
    Object supportsSqlTypesValue = supportsSqlTypesField.get(null);
    assertNotNull(supportsSqlTypesValue);
    assertTrue(supportsSqlTypesValue instanceof Boolean);

    // DATE_DATE_TYPE should be a DateType<? extends Date> and not null
    Field dateDateTypeField = clazz.getField("DATE_DATE_TYPE");
    assertTrue(Modifier.isStatic(dateDateTypeField.getModifiers()));
    assertTrue(Modifier.isFinal(dateDateTypeField.getModifiers()));
    Object dateDateTypeValue = dateDateTypeField.get(null);
    assertNotNull(dateDateTypeValue);
    assertTrue(dateDateTypeValue instanceof com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType);

    // TIMESTAMP_DATE_TYPE should be a DateType<? extends Date> and not null
    Field timestampDateTypeField = clazz.getField("TIMESTAMP_DATE_TYPE");
    assertTrue(Modifier.isStatic(timestampDateTypeField.getModifiers()));
    assertTrue(Modifier.isFinal(timestampDateTypeField.getModifiers()));
    Object timestampDateTypeValue = timestampDateTypeField.get(null);
    assertNotNull(timestampDateTypeValue);
    assertTrue(timestampDateTypeValue instanceof com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType);

    // DATE_FACTORY should be a TypeAdapterFactory and not null
    Field dateFactoryField = clazz.getField("DATE_FACTORY");
    assertTrue(Modifier.isStatic(dateFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(dateFactoryField.getModifiers()));
    Object dateFactoryValue = dateFactoryField.get(null);
    assertNotNull(dateFactoryValue);
    assertTrue(dateFactoryValue instanceof com.google.gson.TypeAdapterFactory);

    // TIME_FACTORY should be a TypeAdapterFactory and not null
    Field timeFactoryField = clazz.getField("TIME_FACTORY");
    assertTrue(Modifier.isStatic(timeFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(timeFactoryField.getModifiers()));
    Object timeFactoryValue = timeFactoryField.get(null);
    assertNotNull(timeFactoryValue);
    assertTrue(timeFactoryValue instanceof com.google.gson.TypeAdapterFactory);

    // TIMESTAMP_FACTORY should be a TypeAdapterFactory and not null
    Field timestampFactoryField = clazz.getField("TIMESTAMP_FACTORY");
    assertTrue(Modifier.isStatic(timestampFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(timestampFactoryField.getModifiers()));
    Object timestampFactoryValue = timestampFactoryField.get(null);
    assertNotNull(timestampFactoryValue);
    assertTrue(timestampFactoryValue instanceof com.google.gson.TypeAdapterFactory);
  }
}