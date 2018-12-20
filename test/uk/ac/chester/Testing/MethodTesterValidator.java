package uk.ac.chester.Testing;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class MethodTesterValidator {


    class GenericHandler implements MethodTester.MethodTestEventHandler {

        @Override
        public void notFound(String methodName) {
            Assert.fail("No method with the name \""+ methodName + "\" was found");
        }

        @Override
        public void incorrectReturnType(String methodName, Class requiredReturnType) {
            Assert.fail("A method named \"" + methodName + "\" was found, but it does not return the correct type: " + requiredReturnType.getName());
        }

        @Override
        public void incorrectParameters(String methodName, Class[] requiredParamTypes) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < requiredParamTypes.length; i++) {

                sb.append(requiredParamTypes[i].getSimpleName());
                if (i < requiredParamTypes.length - 1){
                    sb.append(", ");
                }
            }
            Assert.fail("Method \"" + methodName + "\" found, but parameters are incorrect: " + sb.toString());
        }

        @Override
        public void incorrectParamOrder(String methodName, Class[] requiredParams){
            Assert.fail("Method \"" + methodName + "\" found, but parameters are not in the correct order");
        }
    }

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
        MethodTester<Void> tester = new MethodTester<>(TestClass.class, void.class, "nonExistentMethod", new GenericHandler());
        Assert.assertNull(tester.test());
    }

    @Test
    public void wrongReturnType() {
        MethodTester<String> tester = new MethodTester<>(TestClass.class, String.class, "returnsInteger", new GenericHandler());
        tester.test();
    }

    @Test
    public void strictReturnType() {
        MethodTester<Integer> tester = new MethodTester<>(TestClass.class,int.class,"returnsInteger",new GenericHandler());
        tester.testStrict();
    }


    @Test
    public void wrongParams(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class,void.class,"oneIntParam",new GenericHandler());
        tester.test("text");
    }

    @Test
    public void wrongMultiParams(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class,void.class,"intParamStringParam",new GenericHandler());
        tester.test("text", 23.5);
    }

    @Test
    public void wrongOrderParams(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class,void.class,"intParamStringParam",new GenericHandler());
        tester.test();
    }

    @Test
    public void exceptionThrowing(){
        MethodTester<Void> tester = new MethodTester<>(TestClass.class, void.class,"exceptionThrower", new GenericHandler());
        tester.test();
    }

}
