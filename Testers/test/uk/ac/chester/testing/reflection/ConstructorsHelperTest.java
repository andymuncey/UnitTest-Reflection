package uk.ac.chester.testing.reflection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ConstructorsHelperTest {

    private static class ConstructorTestClass {
        public ConstructorTestClass() {}
        public ConstructorTestClass(int intParam) {}
        public ConstructorTestClass(int anInt, String aString) {}
        private ConstructorTestClass(char myChar, String myString, char myChar2) {}
    }


    ConstructorsHelper<ConstructorTestClass> helper;

    @BeforeEach
    void setUp() {
        helper = new ConstructorsHelper<>(ConstructorTestClass.class);
    }

    @AfterEach
    void tearDown() {
        helper = null;
    }

    @Test
    void constructorForParamTypes() {

        Optional<Constructor<ConstructorTestClass>> privateConstructor = helper.constructorForParamTypes(true,false,true,char.class, String.class, char.class);
        assertTrue(privateConstructor.isPresent());

        Optional<Constructor<ConstructorTestClass>> boxedAllowed = helper.constructorForParamTypes(false,true,true,Integer.class, String.class);
        assertTrue(boxedAllowed.isPresent());

        Optional<Constructor<ConstructorTestClass>> boxedNotAllowed = helper.constructorForParamTypes(false,false,true,Integer.class, String.class);
        assertTrue(boxedNotAllowed.isEmpty());
    }

    @Test
    void availableConstructors() {

        Set<Constructor<ConstructorTestClass>> allConstructors = helper.availableConstructors(true);
        assertEquals(4, allConstructors.size());

        Set<Constructor<ConstructorTestClass>> publicConstructors = helper.availableConstructors(false);
        assertEquals(3, publicConstructors.size());
    }

    @Test
    void constructorForArgTypes() {

        Optional<Constructor<ConstructorTestClass>> constructor = helper.constructorForArgTypes(false,false,"Hello", 42);
        assertTrue(constructor.isPresent());

        Optional<Constructor<ConstructorTestClass>> paramsNotInOrderResult = helper.constructorForArgTypes(false,true,"Hello", 42);
        assertTrue(paramsNotInOrderResult.isEmpty());

    }
}