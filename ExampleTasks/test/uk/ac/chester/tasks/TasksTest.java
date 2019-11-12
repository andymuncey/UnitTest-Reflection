package uk.ac.chester.tasks;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.chester.testing.InstanceTester;
import uk.ac.chester.testing.MethodsTester;
import uk.ac.chester.testing.handlers.InstanceTestEventHandlerEN;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;


public class TasksTest {

    //region example standard unit testExistence for static methods

    @Test
    public void arraySum() {

        int[] emptyArray = {};
        int resultWithEmpty = Tasks.arraySum(emptyArray);
        Assert.assertEquals("empty array",resultWithEmpty,0);

        int[] singleItemArray = {4};
        int resultSingleItem = Tasks.arraySum(singleItemArray);
        Assert.assertEquals("single item array", 4,resultSingleItem);

        int[]multiItemArray = {3,6,8};
        int resultMultiItem = Tasks.arraySum(multiItemArray);
        Assert.assertEquals("multi item array",17,resultMultiItem);

        int[]largerValuesArray = {951, 762, 60485};
        int resultLarger = Tasks.arraySum(largerValuesArray);
        Assert.assertEquals("larger values",62198, resultLarger);
    }

    //endregion

    @Test
    public void arraySumReflection() {

        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,new MethodTestEventHandlerEN() );

        int[] emptyArray = {};
        boolean methodCreatedCorrectly = tester.testExistenceForValues(Integer.class,"arraySum", emptyArray);

        if (methodCreatedCorrectly) {
            int resultWithEmpty = tester.executeStatic(Integer.class, "arraySum", emptyArray);
            Assert.assertEquals("empty array", resultWithEmpty, 0);

            int[] singleItemArray = {4};
            int resultSingleItem = tester.executeStatic(Integer.class, "arraySum", (Object)singleItemArray); //Casting to object will avoid warning, and is necessary for arrays with one item
            Assert.assertEquals("single item array", 4, resultSingleItem);

            int[] multiItemArray = {3, 6, 8};
            int resultMultiItem = tester.executeStatic(Integer.class, "arraySum", multiItemArray);
            Assert.assertEquals("multi item array", 17, resultMultiItem);

            int[] largerValuesArray = {951, 762, 60485};
            int resultLarger = tester.executeStatic(Integer.class, "arraySum", largerValuesArray);
            Assert.assertEquals("larger values", 62198, resultLarger);
        }
    }


    @Test
    public void tempConversion(){

        //Standard assertion
        Tasks tasks = new Tasks();
        Assert.assertEquals(285.372, Tasks.fahrenheitToKelvin(54.0),0.001);


        //Reflection based assertion with method helper class
        InstanceTester<Tasks> instanceTester = new InstanceTester<>(Tasks.class, new InstanceTestEventHandlerEN()); //no check for method creation here -
        Assert.assertEquals(285.372,instanceTester.executeMethod(Double.class, "fahrenheitToKelvin",54.0),0.001);
    }

    @Test
    public void addTen(){
        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,  new MethodTestEventHandlerEN());
        int result = tester.executeStatic(Integer.class, "addTen",13); //needs to be stored or cast to int to avoid ambiguous overloads for the assertion
        Assert.assertEquals(23,result);

        //below is not valid as tester can only return an object
        //Assert.assertEquals(23,tester.testExistence(13));
    }


    @Test
    public void reversedSentence(){
        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,  new MethodTestEventHandlerEN());
        Assert.assertEquals("mat the on sat cat the", tester.executeStatic(String.class, "reversedSentence","the cat sat on the mat"));
    }



}



