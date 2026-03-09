package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.ReflectionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import java.lang.reflect.InvocationTargetException;

public class ReflectiveTypeAdapterFactoryCheckAccessibleTest {

    private Field staticField;
    private Field instanceField;
    private Method staticMethod;
    private Method instanceMethod;
    private Constructor<?> constructor;

    private Object instanceObject;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, NoSuchMethodException {
        // Prepare test class and members
        instanceObject = new TestClass();

        staticField = TestClass.class.getDeclaredField("staticField");
        instanceField = TestClass.class.getDeclaredField("instanceField");

        staticMethod = TestClass.class.getDeclaredMethod("staticMethod");
        instanceMethod = TestClass.class.getDeclaredMethod("instanceMethod");

        constructor = TestClass.class.getDeclaredConstructor();
    }

    @Test
    @Timeout(8000)
    public void checkAccessible_allowsAccessibleStaticMember() throws Exception {
        try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = mockStatic(ReflectionAccessFilterHelper.class)) {
            // Static member, object is null
            mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticField, null)).thenReturn(true);

            invokeCheckAccessible(null, staticField);

            mockedHelper.verify(() -> ReflectionAccessFilterHelper.canAccess(staticField, null));
        }
    }

    @Test
    @Timeout(8000)
    public void checkAccessible_allowsAccessibleInstanceMember() throws Exception {
        try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = mockStatic(ReflectionAccessFilterHelper.class)) {
            // Instance member, object is instanceObject
            mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceField, instanceObject)).thenReturn(true);

            invokeCheckAccessible(instanceObject, instanceField);

            mockedHelper.verify(() -> ReflectionAccessFilterHelper.canAccess(instanceField, instanceObject));
        }
    }

    @Test
    @Timeout(8000)
    public void checkAccessible_throwsOnInaccessibleStaticMember() throws Exception {
        try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = mockStatic(ReflectionAccessFilterHelper.class);
             MockedStatic<ReflectionHelper> mockedHelper2 = mockStatic(ReflectionHelper.class)) {
            mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticMethod, null)).thenReturn(false);
            mockedHelper2.when(() -> ReflectionHelper.getAccessibleObjectDescription(staticMethod, true))
                    .thenReturn("staticMethod description");

            JsonIOException ex = assertThrows(JsonIOException.class, () -> {
                invokeCheckAccessible(null, staticMethod);
            });

            assertTrue(ex.getMessage().contains("staticMethod description"));
            assertTrue(ex.getMessage().contains("is not accessible"));
            mockedHelper.verify(() -> ReflectionAccessFilterHelper.canAccess(staticMethod, null));
            mockedHelper2.verify(() -> ReflectionHelper.getAccessibleObjectDescription(staticMethod, true));
        }
    }

    @Test
    @Timeout(8000)
    public void checkAccessible_throwsOnInaccessibleInstanceMember() throws Exception {
        try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = mockStatic(ReflectionAccessFilterHelper.class);
             MockedStatic<ReflectionHelper> mockedHelper2 = mockStatic(ReflectionHelper.class)) {
            mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(instanceMethod, instanceObject)).thenReturn(false);
            mockedHelper2.when(() -> ReflectionHelper.getAccessibleObjectDescription(instanceMethod, true))
                    .thenReturn("instanceMethod description");

            JsonIOException ex = assertThrows(JsonIOException.class, () -> {
                invokeCheckAccessible(instanceObject, instanceMethod);
            });

            assertTrue(ex.getMessage().contains("instanceMethod description"));
            assertTrue(ex.getMessage().contains("is not accessible"));
            mockedHelper.verify(() -> ReflectionAccessFilterHelper.canAccess(instanceMethod, instanceObject));
            mockedHelper2.verify(() -> ReflectionHelper.getAccessibleObjectDescription(instanceMethod, true));
        }
    }

    @Test
    @Timeout(8000)
    public void checkAccessible_staticMemberWithStaticModifier() throws Exception {
        try (MockedStatic<ReflectionAccessFilterHelper> mockedHelper = mockStatic(ReflectionAccessFilterHelper.class)) {
            // Static member with static modifier, object parameter ignored (set null)
            mockedHelper.when(() -> ReflectionAccessFilterHelper.canAccess(staticMethod, null)).thenReturn(true);

            invokeCheckAccessible(instanceObject, staticMethod);

            mockedHelper.verify(() -> ReflectionAccessFilterHelper.canAccess(staticMethod, null));
        }
    }

    private <M extends AccessibleObject & Member> void invokeCheckAccessible(Object object, M member) throws Exception {
        var method = ReflectiveTypeAdapterFactory.class.getDeclaredMethod("checkAccessible", Object.class, AccessibleObject.class);
        method.setAccessible(true);
        method.invoke(null, object, member);
    }

    static class TestClass {
        public static String staticField = "static";
        public String instanceField = "instance";

        public static void staticMethod() {
        }

        public void instanceMethod() {
        }
    }
}