package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yangzq80@gmail.com
 * @date 2020-12-08
 */
public class CommandExec {

    public static String execCmd(String cmdLine) {

        try {
            Process pro = Runtime.getRuntime().exec(cmdLine);

            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                strbr.append(line).append("\n");
            }
            //System.out.println(cmdLine+strbr.toString());

            return strbr.toString();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String extractSubString(String source,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return "";
    }
}
