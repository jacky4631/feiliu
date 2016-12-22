package com.jiam365.modules.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FilenameUtils;

public class FileUtils
{
    public static void saveFile(String fileContent, String fullFileName)
    {
        try
        {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fullFileName), "UTF-8");Throwable localThrowable3 = null;
            try
            {
                osw.write(fileContent);
                osw.flush();
            }
            catch (Throwable localThrowable1)
            {
                localThrowable3 = localThrowable1;throw localThrowable1;
            }
            finally
            {
                if (osw != null) {
                    if (localThrowable3 != null) {
                        try
                        {
                            osw.close();
                        }
                        catch (Throwable localThrowable2)
                        {
                            localThrowable3.addSuppressed(localThrowable2);
                        }
                    } else {
                        osw.close();
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteFile(String fullFileName)
    {
        try
        {
            File file = new File(fullFileName);
            if ((file.isFile()) && (file.exists())) {
                return file.delete();
            }
        }
        catch (Exception localException) {}
        return false;
    }

    public static String readFile(String fileName)
    {
        BufferedReader br = null;
        try
        {
            File file = new File(fileName);
            StringBuffer buffer = new StringBuffer();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            br = new BufferedReader(isr);
            int s;
            while ((s = br.read()) != -1) {
                buffer.append((char)s);
            }
            return buffer.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Read file error: " + e.getMessage());
        }
        finally
        {
            if (br != null) {
                try
                {
                    br.close();
                }
                catch (IOException localIOException1) {}
            }
        }
    }

    public static String createPath(String path)
    {
        File localFolder = new File(path);
        if (!localFolder.exists()) {
            localFolder.mkdirs();
        }
        return path;
    }

    public static String zipFile(String fullFilename, boolean... removeOrigiFile)
    {
        byte[] buffer = new byte['?'];
        String zipFilename = FilenameUtils.removeExtension(fullFilename) + ".zip";
        try
        {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilename));Throwable localThrowable6 = null;
            try
            {
                ZipEntry ze = new ZipEntry(FilenameUtils.getName(fullFilename));
                zos.putNextEntry(ze);
                FileInputStream in = new FileInputStream(fullFilename);Throwable localThrowable7 = null;
                try
                {
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                catch (Throwable localThrowable1)
                {
                    localThrowable7 = localThrowable1;throw localThrowable1;
                }
                finally {}
                zos.closeEntry();
            }
            catch (Throwable localThrowable4)
            {
                localThrowable6 = localThrowable4;throw localThrowable4;
            }
            finally
            {
                if (zos != null) {
                    if (localThrowable6 != null) {
                        try
                        {
                            zos.close();
                        }
                        catch (Throwable localThrowable5)
                        {
                            localThrowable6.addSuppressed(localThrowable5);
                        }
                    } else {
                        zos.close();
                    }
                }
            }
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
        if ((removeOrigiFile.length > 0) && removeOrigiFile[0]) {
            deleteFile(fullFilename);
        }
        return zipFilename;
    }

    public static long sizeOf(String fullFilename)
    {
        File file = new File(fullFilename);
        if (!file.exists())
        {
            String message = file + " does not exist";
            throw new IllegalArgumentException(message);
        }
        if (file.isDirectory()) {
            throw new IllegalArgumentException(file + "is a directory");
        }
        return file.length();
    }
}
