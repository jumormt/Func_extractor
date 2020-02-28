package com.jumormt.cextractor.beans;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class UserFuncBean {
    ParseTree UserFuncTree = null; // 该函数所在行的tree
    ArrayList<String> paramList = new ArrayList<>(); // 该函数的形参
    Integer startLine = null;
    Integer endLine = null;
    String FuncName = null;

    public UserFuncBean() {

    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }


    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }


    public ParseTree getUserFuncTree() {
        return UserFuncTree;
    }

    public void setUserFuncTree(ParseTree userFuncTree) {
        UserFuncTree = userFuncTree;
    }

    public String getFuncName() {
        return FuncName;
    }

    public void setFuncName(String funcName) {
        FuncName = funcName;
    }

    public ArrayList<String> getParamList() {
        return paramList;
    }


    public void setParamList(ArrayList<String> paramList) {
        this.paramList = paramList;
    }


    @Override
    public String toString() {
        return '\n' + "funcname:" + FuncName + " paramlist:" + paramList;
    }
}
