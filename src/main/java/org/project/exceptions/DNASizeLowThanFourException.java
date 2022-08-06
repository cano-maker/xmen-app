package org.project.exceptions;


public class DNASizeLowThanFourException extends RuntimeException
{
    private String message;

    public DNASizeLowThanFourException(String message) {
        super(message);
        this.message = message;
    }
}
