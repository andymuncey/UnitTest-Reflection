package uk.ac.chester.testing.validators;

import org.junit.jupiter.api.Test;
import uk.ac.chester.testing.*;
import uk.ac.chester.testing.handlers.ClassTestEventHandlerEN;
import uk.ac.chester.testing.handlers.ConstructorsTestEventHandlerEN;
import uk.ac.chester.testing.handlers.FieldsTestEventHandlerEN;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;
import uk.ac.chester.testing.reflection.PackageHelper;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TopDownValidator {

    @Test
    public void test(){

        //Looking for a class named TestClass, package unknown
        String className = "TestClass";

        Set<Class> classes = PackageHelper.findClasses(className);

        if (classes.isEmpty()) {
            fail("No class named " + className + " found. Class names are case sensitive");
        }

        if (classes.size() > 1) {
            fail("Multiple classes found, only one class named " + className + "should be declared");
        }

        //get only item
        Class aClass = classes.iterator().next();

        //generic class member tests
        @SuppressWarnings("unchecked")
        ClassTester classTester = new ClassTester(aClass,new ClassTestEventHandlerEN());
        classTester.checkFields();
        classTester.checkMethods();
        classTester.checkConstructorParameterNames();

        //individual constructor tests
        //construct it using no arg constructor
        ConstructorsTester.EventHandler constructorHandler = new ConstructorsTestEventHandlerEN();
        @SuppressWarnings("unchecked")
        ConstructorsTester constructorsTester = new ConstructorsTester<>(aClass,constructorHandler);
        Object instance = constructorsTester.test(AccessModifier.PUBLIC);
        assertNotNull(instance);

        //construct it with arguments
        Object instanceFromParams = constructorsTester.test(AccessModifier.PRIVATE,'a',"bc",'d');
        assertNotNull(instanceFromParams);

        //testExistence specific fields


        FieldsTester.EventHandler fieldHandler = new FieldsTestEventHandlerEN();
        @SuppressWarnings("unchecked")
        FieldsTester fieldsTester = new FieldsTester(aClass,fieldHandler);
        fieldsTester.test(AccessModifier.PRIVATE, int.class, "regularIvar", false);
        fieldsTester.test(AccessModifier.PUBLIC, int.class, "REGULAR_CONSTANT", false);

        //testExistence specific methods
        MethodsTester.EventHandler methodHandler = new MethodTestEventHandlerEN();
        @SuppressWarnings("unchecked")
        MethodsTester methodsTester = new MethodsTester(aClass, methodHandler);
        boolean returnsPrimitiveIntExists = methodsTester.testExistenceForValues( int.class,"returnsPrimitiveInt");
        assertTrue(returnsPrimitiveIntExists);

        @SuppressWarnings("unchecked")
        MethodsTester<Object> methodsTester2 = new MethodsTester(aClass, methodHandler);
        boolean anotherMethodExists = methodsTester2.testExistenceForValues(void.class, "intParamStringParam",1,"testExistence");
        assertTrue(anotherMethodExists);
    }
}
