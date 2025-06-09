package uk.ac.chester.testing;

/**
 * Base Tester class for Constructor / Method / Field tester subclasses.
 * Allows user defined convention checkers to be used
 */
abstract class Tester {

    private ConventionChecker conventionChecker = new ConventionChecker(){};

    ConventionChecker getConventionChecker() {
        return conventionChecker;
    }

    @SuppressWarnings("unused")
    public void setConventionChecker(ConventionChecker conventionChecker) {
        this.conventionChecker = conventionChecker;
    }
}
