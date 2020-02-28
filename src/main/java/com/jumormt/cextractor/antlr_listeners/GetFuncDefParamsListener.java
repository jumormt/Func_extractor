package com.jumormt.cextractor.antlr_listeners;

import com.jumormt.cextractor.antlr4_files.CPP14BaseListener;
import com.jumormt.cextractor.antlr4_files.CPP14Parser;

import java.util.ArrayList;

public class GetFuncDefParamsListener extends CPP14BaseListener {
    private ArrayList<String> paramList = new ArrayList<>();

    @Override
    public void enterUnqualifiedid(CPP14Parser.UnqualifiedidContext ctx) {
        this.paramList.add(ctx.getText());
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }
}
