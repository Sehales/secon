
package net.sehales.secon.utils.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class FileUtils {
    
    /**
     * check for existance and if not create a new file
     * 
     * @param file
     * @return true if the file was created or already exists, if an error
     *         occured false
     */
    public static boolean checkFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * get the content of a file as a string
     * 
     * @param file
     *            the file to convert
     * @param fileFormat
     *            the file format to use (UTF-8,UTF-16,...)
     * @return the content string or an empty string if an error occurred
     */
    public static String convertToString(File file, String charSet) {
        if (file == null || !file.exists() || file.isDirectory() || !file.canRead()) {
            return "";
        }
        
        Writer stringWriter = new StringWriter();
        InputStream is = null;
        Reader reader = null;
        char[] buffer = new char[1 << 10];
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, charSet));
            
            int n;
            while ((n = reader.read(buffer)) != -1) {
                stringWriter.write(buffer, 0, n);
            }
            reader.close();
            is.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return stringWriter.toString();
    }
    
    /**
     * copy a file or a directory and its contents
     * 
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copy(File src, File dest) throws IOException {
        
        if (src.isDirectory()) {
            
            // if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
                // System.out.println("Directory copied from " + src + "  to " +
                // dest);
            }
            
            // list all the directory contents
            String files[] = src.list();
            
            for (String file : files) {
                // construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // recursive copy
                copy(srcFile, destFile);
            }
            
        } else {
            // if file, then copy it
            // Use byte stream to support all file types
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            
            byte[] buffer = new byte[1024];
            
            int length;
            // copy the file content in bytes
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            
            in.close();
            out.close();
            // System.out.println("File copied from " + src + " to " + dest);
        }
    }
    
    /**
     * deletes a file recursively
     * if the file is a directory it will remove all subdirectories (and files)
     * too
     * 
     * @param file
     *            the file to delete
     */
    public static void delete(File file) {
        synchronized (file) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    delete(f);
                }
            }
            file.delete();
        }
    }
    
    /**
     * write the string to the target file
     * 
     * @param source
     * @param file
     * @return true on success, false otherwise
     */
    public static boolean writeToFile(String source, File file, String charSet) {
        FileOutputStream fos = null;
        OutputStreamWriter writer = null;
        try {
            fos = new FileOutputStream(file);
            writer = new OutputStreamWriter(fos, charSet);
            writer.write(source);
            writer.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
