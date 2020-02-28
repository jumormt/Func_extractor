package com.jumormt.cextractor.error_recovers;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorListenerSimple extends BaseErrorListener {

    private boolean errorOccurred = false;



    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        this.errorOccurred = true;
    }

    public boolean isErrorOccurred() {
        return errorOccurred;
    }
}
