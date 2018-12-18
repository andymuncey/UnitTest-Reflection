package uk.ac.chester;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.Testing.MethodTester;

import static org.junit.Assert.*;

public class TasksTest {

    Tasks tasks;

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

    class GenericHandler implements MethodTester.MethodTestEventHandler {

        @Override
        public void notFound(String methodName) {
            Assert.fail("No method with the name: "+ methodName + " was found");
        }

        @Override
        public void incorrectReturnType(String methodName, Class requiredReturnType) {
            Assert.fail("A method with the correct name was found, but it does not return the correct type: " + requiredReturnType.getName());
        }

        @Override
        public void incorrectParameters(String methodName) {

        }

        @Override
        public void incorrectParamOrder(String methodName, String requiredParamOrder) {

        }
    }


    @Test
    public void arraySumReflection() {

        MethodTester tester = new MethodTester(Tasks.class, "arraySum", new GenericHandler());

        int[] args = {1,3};
        tester.testExecution(4,(Object)args);

    }


}



