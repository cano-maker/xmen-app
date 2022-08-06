package org.project.exceptions;


public class DNASequenceNotValid extends RuntimeException
{
    private String message;

    public DNASequenceNotValid(String message) {
        super(message);
        this.message = message;
    }
}
