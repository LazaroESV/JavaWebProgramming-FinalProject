package com.champsoft.cardshop.Utilities;

public class CollectorOwnsCardsException extends RuntimeException {
    public CollectorOwnsCardsException(Long collector) {
      super("Collector " + collector + " cannot be deleted for they still own cards.");
    }
}
