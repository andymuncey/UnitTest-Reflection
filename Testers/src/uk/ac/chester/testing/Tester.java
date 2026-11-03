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

    /**
     * Allows the convention checker to be changed from the default
     * @param conventionChecker an implementation of {@link ConventionChecker}
     */
    @SuppressWarnings("unused")
    public void setConventionChecker(ConventionChecker conventionChecker) {
        this.conventionChecker = conventionChecker;
    }
}
