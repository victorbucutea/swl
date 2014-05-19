package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;

import java.io.File;

/**
 * Created by VictorBucutea on 26.04.2014.
 */
public class EntityNotInPackageException extends InvalidPackageException {

    public EntityNotInPackageException(File entityTemplate, String entityName) {
        super(entityName +" must be in a __package__ folder. Please check skeleton at "+entityTemplate.getAbsolutePath()+".");
    }
}
