package com.lge.concoleutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

class LogMerger {
	private static String MERGED_FILE = "\\merged.txt";
    private static String MERGED_FILE_PATH = "";
    private static TreeMap<Double, LogData> mLogList = new TreeMap<Double, LogData>();
    
    public LogMerger(String[] args) {
        if (args != null && args.length > 1) {
            if (!isValidLogs(args)) {
                return;
            } else {
            	MERGED_FILE_PATH = args[0].substring(0, args[0].lastIndexOf("\\")) + MERGED_FILE;
            }
            
            for (int i=0 ; i<args.length ; i++) {
                createMergedList(args[i], mLogList);
            }
            createMergedLog(mLogList.keySet().iterator());
        } else {
            System.out.println("Two more logs should be needed to merge.");
        }
    }
    
    private static boolean isValidLogs(String[] logs) {
        for (int i=0 ; i<logs.length ; i ++) {
            for (int j=0 ; j<logs.length ; j++) {
                if (i != j && logs[i].equals(logs[j])) {
                    System.out.println("Cannot allow duplicated logs, name of logs should be different!!");
                    return false;
                }
            }
            if (!(new File(logs[i])).exists()) {
                System.out.println("Cannot find '" + logs[i] + "'!!");
                return false;                
            }
            if (!(new File(logs[i])).isFile()) {
                System.out.println("Only file is allowed!!");
                return false;                
            }
        }
        return true;
    }
    
    private static void createMergedList(String filePath, TreeMap<Double, LogData> list) {
        FileInputStream fis = null;
        InputStreamReader Isr = null;
        BufferedReader br = null;
        try{ 
            fis = new FileInputStream(filePath);
            Isr = new InputStreamReader(fis, "UTF-8");                
            br = new BufferedReader(Isr); 
            String line = null; 
            while((line = br.readLine()) != null) {
                double key = getDateTime(line);
                while (list.containsKey(key)) {
                    key += 0.0001;
                }
                list.put(key, new LogData(filePath.substring(filePath.lastIndexOf("\\") + 1), line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (Isr != null) Isr.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static double getDateTime(String line) {
        if (line.indexOf(".") > 0) {
            String dateTime = line.substring(0, line.indexOf("."));
            double lastDigit = getDigit(line.substring(line.indexOf(".")+1));
            DateFormat sdFormat = new SimpleDateFormat("mm-dd hh:mm:ss");
            try {
                Date date = sdFormat.parse(dateTime);
                double result = date.getTime();
                if (lastDigit > -1) {
                    result += lastDigit;
                }
                return result;
            } catch (ParseException e) {
            }
        }
        return 0;
    }
    
    private static int getDigit(String line) {
        if (line != null && line.length() != 0) {
            StringBuffer buffer = new StringBuffer();
            char[] readChar = line.toCharArray();
            int i = 0;
            while (readChar[i] != ' ' && readChar.length > i) {
                buffer.append(readChar[i]);
                i ++;
            }
            if (buffer.toString().length() != 0) {
                return Integer.parseInt(buffer.toString());
            }
        }
        return -1;
    }
        
    private static void createMergedLog(Iterator<Double> sortedList) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            fos = new FileOutputStream(MERGED_FILE_PATH);
            osw = new OutputStreamWriter(fos, "UTF-8");
            bw = new BufferedWriter(osw);
            while (sortedList.hasNext()) {
                double key = sortedList.next();
                String log = mLogList.get(key).getLogData();
                String filePath = mLogList.get(key).getFilePath();
                
                if (log.length() != 0 && Character.isDigit(log.toCharArray()[0])) {
                    bw.write("[" + filePath + "] " + log + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (osw != null) osw.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
