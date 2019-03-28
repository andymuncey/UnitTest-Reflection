package uk.ac.chester.testing.reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ConstructorsHelper<C> {

    final private Class<C> searchClass;

    public ConstructorsHelper(Class<C> searchClass) {
        this.searchClass = searchClass;
    }

    /**
     * Finds a constructor matching specified criteria
     * @param includeNonPublic include constructors not marked as public
     * @param allowAutoboxing set to false if the parameter types must match exactly or true if primitive and boxed types can be used interchangeably
     * @param matchParamOrder set to true if the order of parameters in the constructor must match the order they are passed into this method.
     *                        Note that if this is set to false, the first constructor with matching parameters would be returned, even if there is a better match
     * @param params the type of params the constructor should take
     * @return An Optional containing a matching constructor, if found
     */
    public Optional<Constructor<C>> constructorForParamTypes(boolean includeNonPublic, boolean allowAutoboxing, boolean matchParamOrder, Class... params){
        @SuppressWarnings("unchecked") //casting declared constructors not of type, but constructors must be of the same type given the class in which they appear
                Constructor<C>[] constructorsArray = (Constructor<C>[]) (includeNonPublic ? searchClass.getDeclaredConstructors() : searchClass.getConstructors());

        Set<Constructor<C>> constructors = new HashSet<>(Arrays.asList(constructorsArray));
        constructors.removeIf(Constructor::isSynthetic);

        if (!matchParamOrder){
            Utilities.sortParamsTypesByName(params);
        }

        for (Constructor<C> c: constructors) {
            if (params.length == c.getParameterCount()){

                Class[] actualParamTypes = c.getParameterTypes();
                if (!matchParamOrder){
                    Utilities.sortParamsTypesByName(actualParamTypes);
                }
                if (typesMatch(allowAutoboxing, actualParamTypes, params)) {
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Verify if two sets of types can be considered equal
     * @param allowAutoboxing
     * @param actualParamTypes
     * @param params
     * @return
     */
    //todo: indicate that only 1 set of types are boxed and check whether this is desired
    private boolean typesMatch(boolean allowAutoboxing, Class[] actualParamTypes, Class[] params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i] != (allowAutoboxing ?  Utilities.classEquivalent(actualParamTypes[i]) : actualParamTypes[i] )){
                return false;
            }
        }
        return true;
    }

    /**
     * Finds a constructor matching specified criteria
     * @param includeNonPublic include constructors not marked as public
     * @param matchParamOrder set to true if the order of parameters in the constructor must match the order they are passed into this method
     * @param args example arguments for the constructor to take (the types of which will be used to locate the constructor)
     * @return An Optional containing a matching constructor, if found
     */
    public Optional<Constructor<C>> constructorForArgTypes(boolean includeNonPublic, boolean matchParamOrder, Object... args ){
        Class[] desiredParamTypes = Utilities.classesForArgs(args);
        return constructorForParamTypes(includeNonPublic,true,matchParamOrder,desiredParamTypes);
    }


}
