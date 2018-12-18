package uk.ac.chester;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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




}



