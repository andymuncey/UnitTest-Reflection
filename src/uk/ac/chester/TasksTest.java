package uk.ac.chester;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.Testing.MethodTester;

import java.util.Arrays;

public class TasksTest {


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
            Assert.fail("Method \"" + methodName + "\" found, but parameters are incorrect: " + Arrays.toString(requiredParamTypes));
        }

        @Override
        public void incorrectParamOrder(String methodName, Class[] requiredParams){
            Assert.fail("Method \"" + methodName + "\" found, but parameters are not in the correct order");
        }
    }


    private Tasks tasks;

    @Before
    public void setUp() throws Exception {
        tasks = new Tasks();
    }

    @After
    public void tearDown() throws Exception {
        tasks = null;
    }



    @Test
    public void arraySum() {

        int[] emptyArray = {};
        int resultWithEmpty = tasks.arraySum(emptyArray);
        Assert.assertEquals("empty array",resultWithEmpty,0);

        int[] singleItemArray = {4};
        int resultSingleItem = tasks.arraySum(singleItemArray);
        Assert.assertEquals("single item array", 4,resultSingleItem);

        int[]multiItemArray = {3,6,8};
        int resultMultiItem = tasks.arraySum(multiItemArray);
        Assert.assertEquals("multi item array",17,resultMultiItem);

        int[]largerValuesArray = {951, 762, 60485};
        int resultLarger = tasks.arraySum(largerValuesArray);
        Assert.assertEquals("larger values",62198, resultLarger);
    }

    //Generic handler

    @Test
    public void arraySumReflection() {

        MethodTester<Integer> tester = new MethodTester<>(Tasks.class,int.class,"arraySum",new GenericHandler() );

        int[] emptyArray = {};
        int resultWithEmpty = tester.test(emptyArray);
        Assert.assertEquals("empty array",resultWithEmpty,0);

        int[] singleItemArray = {4};
        int resultSingleItem = tester.test(singleItemArray);
        Assert.assertEquals("single item array", 4,resultSingleItem);

        int[]multiItemArray = {3,6,8};
        int resultMultiItem = tester.test(multiItemArray);
        Assert.assertEquals("multi item array",17,resultMultiItem);

        int[]largerValuesArray = {951, 762, 60485};
        int resultLarger = tester.test(largerValuesArray);
        Assert.assertEquals("larger values",62198, resultLarger);
    }


    @Test
    public void testNonExistentMethod() {
        MethodTester<Void> tester = new MethodTester<>(Tasks.class, void.class, "nonExistentMethod", new GenericHandler());
        Assert.assertNull(tester.test());
    }

    @Test
    public void testWrongReturnType() {
        MethodTester<Double> tester = new MethodTester<>(Tasks.class, double.class, "arraySum", new GenericHandler());
        tester.test();
    }

    @Test
    public void testStrictReturnTyoe() {
        MethodTester<Integer> tester = new MethodTester<>(Tasks.class,Integer.class,"arraySum",new GenericHandler());
        int[] multiItemArray = {3,6,8};
        int resultMultiItem = tester.testStrict((Object)multiItemArray);
        Assert.assertEquals("multi item array",17,resultMultiItem);

    }


}



