package com.champsoft.cardshop.Utilities;

public class CollectorNotFoundException extends RuntimeException {
    public CollectorNotFoundException(Long id) {
        super("Cannot find collector with id: " + id);
    }
}
