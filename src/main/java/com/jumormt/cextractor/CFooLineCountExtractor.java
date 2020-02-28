package com.jumormt.cextractor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CFooLineCountExtractor {

    private static final Logger logger = LoggerFactory.getLogger(CFooLineCountExtractor.class);

    private ArrayList<File> codes = new ArrayList<File>();
    private File inputDir;
    private File outputFilePath;

    CFooLineCountExtractor(String inputDir, String outputFilePath) {
        this.inputDir = new File(inputDir);
        this.outputFilePath = new File(outputFilePath);
    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option input = new Option("i", "input", true, "input dir path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file e.g. screen.json");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        String inputDir = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        if (!new File(inputDir).exists()) {
            logger.info("invalid input dir {}", inputDir);
            return;
        }
        logger.info("[start program]");

        CFooLineCountExtractor cFooLineCountExtractor = new CFooLineCountExtractor(inputDir, outputFilePath);
        cFooLineCountExtractor.extract();

        logger.info("[end program]");


    }

    /**
     *
     */
    public void extract() throws Exception {
        getSourceCodeFiles(inputDir);
        JSONObject jsonObject = new JSONObject();
        int count = 0;
        int skip = 0;
        logger.info("start - parsing project {}..", inputDir.getAbsolutePath());

        for (File file : codes) {
            logger.info("parsing file {}..", file.getAbsolutePath());
            HashMap<String, Integer> res = SingleExtractor.generateLineCounts(file);
            if (res == null){
                logger.info("skip file {}!", file.getAbsolutePath());
                skip++;
                continue;
            }
            logger.info("end parsing file {}! total {} methods", file.getAbsolutePath(), res.size());
            for (Map.Entry<String, Integer> entry : res.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
            count++;
        }
        String jsonObjStr = jsonObject.toJSONString();
        PrintWriter printWriter = new PrintWriter(outputFilePath, "UTF-8");
        printWriter.print(jsonObjStr);
        printWriter.close();
        logger.info("end - parsing project! parsed {} files, skipped {} files", count, skip);


    }

    /**
     * get all .c and .h files in the project
     *
     * @param inputDir
     */
    private void getSourceCodeFiles(File inputDir) {
        File[] files = inputDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getSourceCodeFiles(file);
            }

            if (file.isFile()) {
                String fileName = file.getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                if (suffix.equals("c") || suffix.equals("h") || suffix.equals("cpp")) {
                    codes.add(file);
                }
            }
        }
    }

}
