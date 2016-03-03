package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {

    @Test
    public void tellJoke() {
        String result = "";
        Context context = new MockContext();
        EndpointsAsyncTask task = new EndpointsAsyncTask();
        task.execute(context);
        try {
            result = task.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(result.length() > 0);
    }

}