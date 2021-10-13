package uk.ac.chester.testing.handlers;

import org.junit.Assert;
import uk.ac.chester.testing.PackageTester;

public class PackageTestEventHandlerEN implements PackageTester.EventHandler {
    @Override
    public void notFound(String name) {

        Assert.fail("No class with the name " + name + " could be found");

    }

    @Override
    public void foundWrongCase(String desiredName, String foundName) {

        Assert.fail("A class named " + foundName + " was found, however the casing was incorrect, it should be named " + desiredName);

    }

    @Override
    public void foundInWrongPackage(String name, String desiredPackage, String actualPackage) {

        Assert.fail("A class with the name " + name + " was found, however it is in the " + actualPackage + " package. It should be in the " + desiredPackage + " package");

    }

    @Override
    public void foundWrongCaseAndPackage(String name, String foundName, String desiredPackage, String actualPackage) {

        Assert.fail("A class with the name " + name + " was found, however the casing was incorrect, it should be named \" + desiredName, in addition, it is in the \" + actualPackage + \" package. It should be in the \" + desiredPackage + \" package\"");
    }
}
