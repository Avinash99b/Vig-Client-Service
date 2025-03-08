package com.avinash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//        try {
//            ProcessBuilder builder = new ProcessBuilder("speedtest", " -f json-pretty");
//            Process process = builder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder jsonResult = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                jsonResult.append(line);
//            }
//            int exitVal = process.waitFor();
//
//            System.out.println(exitVal);
//
//        } catch (Exception e) {
//            System.out.println(e.getStackTrace());
//        }
        SpeedTester.main(args);
    }
}
