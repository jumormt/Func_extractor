package com.jumormt.cextractor.antlr_listeners;

import com.jumormt.cextractor.antlr4_files.CPP14BaseListener;
import com.jumormt.cextractor.antlr4_files.CPP14Parser;
import com.jumormt.cextractor.beans.UserFuncBean;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetFuncStartEndListener extends CPP14BaseListener {
    private ArrayList<UserFuncBean> userFuncBeans = new ArrayList<UserFuncBean>();// 用户自定义函数列表
    private ParseTree currentTopTree = null; //当前顶层函数的语法树
    private String currentTopName = null; //当前顶层函数的名称
    private Map<String, ArrayList<String>> paramMap = new HashMap<>(); // key为函数定义时的名字，value为函数定义（非调用）的参数列表
    private ArrayList<String> currentTopParamList = null;

    @Override
    public void enterFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        this.currentTopTree = ctx;
        ParseTreeWalker walker = new ParseTreeWalker();
        GetFuncDefNameListener getFuncDefNameListener = new GetFuncDefNameListener();
        walker.walk(getFuncDefNameListener, ctx);
        this.currentTopName = getFuncDefNameListener.getFuncDefName();
        this.currentTopParamList = getFuncDefNameListener.getParamList();
        this.paramMap.put(this.currentTopName, this.currentTopParamList);

        // 用户自定义函数，用于函数内控制流分析
        UserFuncBean userFuncBean = new UserFuncBean();
        userFuncBean.setStartLine(ctx.start.getLine());
        userFuncBean.setEndLine(ctx.stop.getLine());
        userFuncBean.setFuncName(this.currentTopName);
        userFuncBean.setParamList(this.currentTopParamList);
        userFuncBean.setUserFuncTree(ctx);
        this.userFuncBeans.add(userFuncBean);
    }

    public ArrayList<UserFuncBean> getUserFuncBeans() {
        return userFuncBeans;
    }
}

