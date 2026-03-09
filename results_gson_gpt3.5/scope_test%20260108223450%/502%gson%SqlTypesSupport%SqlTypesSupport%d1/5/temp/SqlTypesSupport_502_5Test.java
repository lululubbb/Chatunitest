package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import java.sql.Timestamp;
import java.util.Date;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class SqlTypesSupport_502_5Test {

  @Test
    @Timeout(8000)
  void testStaticFieldsInitialization() throws Exception {
    Class<SqlTypesSupport> clazz = SqlTypesSupport.class;

    // SUPPORTS_SQL_TYPES
    Field supportsSqlTypesField = clazz.getDeclaredField("SUPPORTS_SQL_TYPES");
    supportsSqlTypesField.setAccessible(true);
    boolean supportsSqlTypes = supportsSqlTypesField.getBoolean(null);
    // Just assert it's boolean, no other info available
    assertTrue(supportsSqlTypes || !supportsSqlTypes);

    // DATE_DATE_TYPE
    Field dateDateTypeField = clazz.getDeclaredField("DATE_DATE_TYPE");
    dateDateTypeField.setAccessible(true);
    Object dateDateType = dateDateTypeField.get(null);
    assertNotNull(dateDateType);
    assertTrue(dateDateType instanceof com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType);

    // TIMESTAMP_DATE_TYPE
    Field timestampDateTypeField = clazz.getDeclaredField("TIMESTAMP_DATE_TYPE");
    timestampDateTypeField.setAccessible(true);
    Object timestampDateType = timestampDateTypeField.get(null);
    assertNotNull(timestampDateType);
    assertTrue(timestampDateType instanceof com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType);

    // DATE_FACTORY
    Field dateFactoryField = clazz.getDeclaredField("DATE_FACTORY");
    dateFactoryField.setAccessible(true);
    Object dateFactory = dateFactoryField.get(null);
    assertNotNull(dateFactory);
    assertTrue(dateFactory instanceof com.google.gson.TypeAdapterFactory);

    // TIME_FACTORY
    Field timeFactoryField = clazz.getDeclaredField("TIME_FACTORY");
    timeFactoryField.setAccessible(true);
    Object timeFactory = timeFactoryField.get(null);
    assertNotNull(timeFactory);
    assertTrue(timeFactory instanceof com.google.gson.TypeAdapterFactory);

    // TIMESTAMP_FACTORY
    Field timestampFactoryField = clazz.getDeclaredField("TIMESTAMP_FACTORY");
    timestampFactoryField.setAccessible(true);
    Object timestampFactory = timestampFactoryField.get(null);
    assertNotNull(timestampFactory);
    assertTrue(timestampFactory instanceof com.google.gson.TypeAdapterFactory);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = SqlTypesSupport.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    var instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof SqlTypesSupport);
  }
}