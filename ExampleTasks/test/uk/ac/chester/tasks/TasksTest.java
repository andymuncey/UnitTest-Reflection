package uk.ac.chester.tasks;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.ac.chester.testing.MethodsTester;
import uk.ac.chester.testing.handlers.MethodTestEventHandlerEN;


public class TasksTest {

    private Tasks tasks;

    @Before
    public void setUp()  {
        tasks = new Tasks();
    }

    @After
    public void tearDown()  {
        tasks = null;
    }

    //region example standard unit test

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

    //endregion

    @Test
    public void arraySumReflection() {

        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,new MethodTestEventHandlerEN() );

        int[] emptyArray = {};
        int resultWithEmpty = tester.test(Integer.class,"arraySum",(Object)emptyArray);
        Assert.assertEquals("empty array",resultWithEmpty,0);

        int[] singleItemArray = {4};
        int resultSingleItem = tester.test(Integer.class,"arraySum",(Object)singleItemArray); //Casting to object will avoid warning
        Assert.assertEquals("single item array", 4,resultSingleItem);

        int[]multiItemArray = {3,6,8};
        int resultMultiItem = tester.test(Integer.class,"arraySum",(Object)multiItemArray);
        Assert.assertEquals("multi item array",17,resultMultiItem);

        int[]largerValuesArray = {951, 762, 60485};
        int resultLarger = tester.test(Integer.class,"arraySum",(Object)largerValuesArray);
        Assert.assertEquals("larger values",62198, resultLarger);
    }


    @Test
    public void tempConversion(){

        //Standard assertion
        Tasks tasks = new Tasks();
        Assert.assertEquals(285.372, tasks.fahrenheitToKelvin(54.0),0.001);

        //Reflection based assertion with method helper class
        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,  new MethodTestEventHandlerEN());
        Assert.assertEquals(285.372,tester.test(Double.class, "fahrenheitToKelvin",54.0),0.001);
    }

    @Test
    public void addTen(){
        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,  new MethodTestEventHandlerEN());
        int result = tester.test(Integer.class, "addTen",13); //needs to be stored or cast to int to avoid ambiguous overloads for the assertion
        Assert.assertEquals(23,result);

        //below is not valid as tester can only return an object
        //Assert.assertEquals(23,tester.test(13));
    }


    @Test
    public void reversedSentence(){
        MethodsTester<Tasks> tester = new MethodsTester<>(Tasks.class,  new MethodTestEventHandlerEN());
        Assert.assertEquals("mat the on sat cat the", tester.test(String.class, "reversedSentence","the cat sat on the mat"));
    }



}



