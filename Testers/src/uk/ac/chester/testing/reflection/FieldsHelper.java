package uk.ac.chester.testing.reflection;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FieldsHelper<C> {

    final private Class<C> searchClass;

    public FieldsHelper(Class<C> searchClass) {
        this.searchClass = searchClass;
    }

    /**
     * Gets all fields within the class, excluding inherited  and synthetic fields
     * @return a Set of Field objects
     */
    public Set<Field> fields(){
        Set<Field> fields = new HashSet<>();
        Collections.addAll(fields,searchClass.getDeclaredFields());
        fields.removeIf(Field::isSynthetic);
        return fields;
    }


    /**
     * Returns any fields (excluding inherited/synthetic ones) matching the name, ignoring case
     * @param name the name of the field to find
     * @return a set of Fields, which may be empty
     */
    public Set<Field> fieldsIgnoringCase(String name){
        Set<Field> fields = new HashSet<>();
        for (Field field : fields()){
            if (field.getName().equalsIgnoreCase(name)){
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Finds a field in this class by name, excludes inherited & synthetic fields
     * @param name the name of the field to search for
     * @return an Optional containing a field, if found
     */
    Optional<Field> field(String name){
        for (Field field : fields()){
            if (field.getName().equals(name)){
                return Optional.of(field);
            }
        }
        return Optional.empty();
    }

}
