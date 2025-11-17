package com.champsoft.cardshop.Utilities;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Cannot find card with id: " + id);
    }
}
