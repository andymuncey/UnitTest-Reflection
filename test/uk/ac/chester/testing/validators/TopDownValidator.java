package uk.ac.chester.testing.validators;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.*;
import uk.ac.chester.testing.handlers.*;
import java.util.Set;

public class TopDownValidator {

    @Test
    public void test(){

        //Looking for a class named TestClass, package unknown
        String className = "TestClass";

        Set<Class> classes = PackageHelper.findClasses(className);

        if (classes.isEmpty()) {
            Assert.fail("No class named " + className + " found");
        }

        if (classes.size() > 1) {
            Assert.fail("Multiple classes found, only one class named " + className + "should be declared");
        }

        //get only item
        Class aClass = (Class)classes.toArray()[0];

        //generic class member tests
        ClassTester classTester = new ClassTester(aClass,new ClassTestEventHandlerEN());
        classTester.checkFields();
        classTester.checkMethods();

        //construct it using no arg constructor
        ConstructorTester constructorTester = new ConstructorTester<>(aClass,new ConstructorTestEventHandlerEN());
        Object instance = constructorTester.testConstructor(AccessModifier.PUBLIC);
        Assert.assertNotNull(instance);

        //construct it with arguments
        Object instanceFromParams = constructorTester.testConstructor(AccessModifier.PRIVATE,'a',"bc",'d');
        Assert.assertNotNull(instanceFromParams);

        //test specific fields
        FieldTester fieldTester = new FieldTester(aClass,new FieldTestEventHandlerEN());
        fieldTester.testField(AccessModifier.PRIVATE,"regularIvar",int.class,false);
        fieldTester.testField(AccessModifier.PUBLIC, "REGULAR_CONSTANT", int.class, false);

        //test specific methods
        MethodTester<Object> methodTester = new MethodTester(aClass, int.class,"returnsPrimitiveInt",new MethodTestEventHandlerEN());
        Object result = methodTester.test();
        Assert.assertEquals(1,result);

        MethodTester<Object> methodTester2 = new MethodTester(aClass, void.class, "intParamStringParam", new MethodTestEventHandlerEN());
        Object result2 = methodTester2.test(1,"test");
        Assert.assertNull(result2);
    }
}
