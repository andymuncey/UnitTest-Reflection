package uk.ac.chester.Testing;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * The tests in the class should FAIL - it's designed to verify expected
 */
public class MethodTesterValidator {

    private TestClass tasks;

    @Before
    public void setUp() throws Exception {
        tasks = new TestClass();
    }

    @After
    public void tearDown() throws Exception {
        tasks = null;
    }


    @Test
    public void nonExistentMethod() {
        MethodTester<Void> tester = new MethodTester<>(TestClass.class, void.class, "nonExistentMethod", new TestEventHandlerEN());
        Assert.assertNull(tester.test());
    }

    @Test
    public void wrongReturnType() {
        MethodTester<String> tester = new MethodTester<>(TestClass.class, String.class, "returnsInteger", new TestEventHandlerEN());
        tester.test();
    }

    @Test
    public void strictReturnType() {
        MethodTester<Integer> tester = new MethodTester<>(TestClass.class,int.class,"returnsInteger",new TestEventHandlerEN());
        tester.testStrict();
    }


    @Test
    public void wrongParams(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class,void.class,"oneIntParam",new TestEventHandlerEN());
        tester.test("text");
    }

    @Test
    public void wrongMultiParams(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class,void.class,"intParamStringParam",new TestEventHandlerEN());
        tester.test("text", 23.5);
    }

    @Test
    public void wrongOrderParams(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class,void.class,"intParamStringParam",new TestEventHandlerEN());
        tester.test();
    }

    @Test
    public void exceptionThrowing(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class, void.class,"exceptionThrower", new TestEventHandlerEN());
        tester.test(); //should throw the exception from the method being tested
    }

}
