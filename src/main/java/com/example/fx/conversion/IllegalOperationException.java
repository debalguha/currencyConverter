package com.example.fx.conversion;

import com.example.fx.FXEntry;

public class IllegalOperationException extends RuntimeException {
    private final FXEntry sourceEntryToBuiltFrom;

    public IllegalOperationException(String message, FXEntry fxEntry){
        super(message);
        this.sourceEntryToBuiltFrom = fxEntry;
    }

    public FXEntry getSourceEntryToBuiltFrom() {
        return sourceEntryToBuiltFrom;
    }
}
