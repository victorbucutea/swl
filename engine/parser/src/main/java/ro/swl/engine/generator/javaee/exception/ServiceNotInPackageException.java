package ro.swl.engine.generator.javaee.exception;

import ro.swl.engine.generator.CreateException;

import java.io.File;

/**
 * Created by VictorBucutea on 26.04.2014.
 */
public class ServiceNotInPackageException extends InvalidPackageException {


    public ServiceNotInPackageException(File servicePlaceholder) {
        super("Service "+servicePlaceholder.getAbsolutePath() + " must be in a __package__ folder.");
    }
}
