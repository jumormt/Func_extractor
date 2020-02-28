package com.jumormt.cextractor;

import com.jumormt.cextractor.antlr4_files.CPP14Lexer;
import com.jumormt.cextractor.antlr4_files.CPP14Parser;
import com.jumormt.cextractor.antlr_listeners.GetFuncStartEndListener;
import com.jumormt.cextractor.beans.UserFuncBean;
import com.jumormt.cextractor.error_recovers.ErrorListenerSimple;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleExtractor {
    private static final Logger logger = LoggerFactory.getLogger(SingleExtractor.class);


    /**
     * generate lines count for each function
     *
     * @param inputFile source code
     * @return lines for each function e.g. {"foo": 10}
     * @throws Exception
     */
    public static HashMap<String, Integer> generateLineCounts(File inputFile) throws Exception {
        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }
        ANTLRInputStream input = new ANTLRInputStream(is);
        CPP14Lexer lexer = new CPP14Lexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CPP14Parser parser = new CPP14Parser(tokens);
        ErrorListenerSimple errorListener = new ErrorListenerSimple();
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);// 添加错误处理机制

        ParseTree tree = parser.translationunit();
        GetFuncStartEndListener getFuncStartEndListener
                = new GetFuncStartEndListener();// 只需要得到函数的tree
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(getFuncStartEndListener, tree);

        //如果在解析语法树的时候发生错误，那么停止对该文件的处理
        if (errorListener.isErrorOccurred()) {
            is = null;
            input = null;
            lexer = null;
            tokens = null;
            parser = null;
            errorListener = null;
            walker = null;
            getFuncStartEndListener = null;
            System.out.println("Skip " + inputFile.getCanonicalPath());
            return null;
        }
        ArrayList<UserFuncBean> userFuncBeans = getFuncStartEndListener.getUserFuncBeans();

        // release resources
        is = null;
        input = null;
        lexer = null;
        tokens = null;
        parser = null;
        walker = null;

        return buildFooToLineCount(userFuncBeans);
    }

    /**
     *
     * @param userFuncBeans user func beans produced by antlr
     * @return lines for each function e.g. {"foo": 10}
     */
    private static HashMap<String, Integer> buildFooToLineCount(ArrayList<UserFuncBean> userFuncBeans) {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        for (UserFuncBean userFuncBean : userFuncBeans) {
            String funcName = userFuncBean.getFuncName();
            Integer startLine = userFuncBean.getStartLine();
            Integer endLine = userFuncBean.getEndLine();
            Integer lineCount = endLine - startLine + 1;
            stringIntegerHashMap.put(funcName, lineCount);

        }
        return stringIntegerHashMap;
    }


    public static void main(String[] args) throws Exception {
        logger.info("now {}" , "starting server");
        File file = new File("D:\\CodingProject\\java_project\\cextractor\\src\\main\\resources\\testCode\\SVFG.cpp");
        HashMap<String, Integer> res = SingleExtractor.generateLineCounts(file);
        logger.info("now {}" , "starting server");


    }
}
