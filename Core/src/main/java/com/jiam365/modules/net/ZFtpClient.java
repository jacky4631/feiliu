package com.jiam365.modules.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public final class ZFtpClient
{
    private FTPClient client;
    private ZFtpClientConfig config;

    public ZFtpClient(ZFtpClientConfig config)
    {
        this.client = new FTPClient();
        this.config = config;

        FTPClientConfig conf = new FTPClientConfig(config.getStyle());
        this.client.configure(conf);
    }

    public boolean connect()
            throws IOException
    {
        this.client.setConnectTimeout(15000);
        this.client.connect(this.config.getServer(), this.config.getPort());
        this.client.setDataTimeout(15000);
        if ((FTPReply.isPositiveCompletion(this.client.getReplyCode())) &&
                (this.client.login(this.config.getUsername(), this.config.getPassword())))
        {
            this.client.setControlEncoding(this.config.getRemoteEncoding());
            if (this.config.isPassiveMode()) {
                this.client.enterLocalPassiveMode();
            }
            if (this.config.isBinary()) {
                try
                {
                    this.client.setFileType(2);
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
            return true;
        }
        return false;
    }

    public void close()
            throws IOException
    {
        if (this.client.isConnected())
        {
            this.client.logout();
            this.client.disconnect();
        }
    }

    public String[] listFiles()
            throws IOException
    {
        FTPFile[] files = this.client.listFiles();
        int filesLength = files.length;
        String[] fileNameArr = new String[filesLength];
        for (int i = 0; i < filesLength; i++) {
            fileNameArr[i] = files[i].getName();
        }
        return fileNameArr;
    }

    public boolean upload(String filename, String remoteName)
            throws IOException
    {
        FileInputStream fis = new FileInputStream(filename);Throwable localThrowable3 = null;
        try
        {
            return this.client.storeFile(remoteName, fis);
        }
        catch (Throwable localThrowable4)
        {
            localThrowable3 = localThrowable4;throw localThrowable4;
        }
        finally
        {
            if (fis != null) {
                if (localThrowable3 != null) {
                    try
                    {
                        fis.close();
                    }
                    catch (Throwable localThrowable2)
                    {
                        localThrowable3.addSuppressed(localThrowable2);
                    }
                } else {
                    fis.close();
                }
            }
        }
    }

    public boolean upload(InputStream stream, String remoteName)
            throws IOException
    {
        try
        {
            return this.client.storeFile(remoteName, stream);
        }
        finally
        {
            stream.close();
        }
    }

    public boolean download(String path, String name)
            throws IOException
    {
        FileOutputStream fos = new FileOutputStream(path + name);Throwable localThrowable3 = null;
        try
        {
            return this.client.retrieveFile(new String(name.getBytes(this.config.getLocalEncoding()), this.config
                    .getRemoteEncoding()), fos);
        }
        catch (Throwable localThrowable4)
        {
            localThrowable3 = localThrowable4;throw localThrowable4;
        }
        finally
        {
            if (fos != null) {
                if (localThrowable3 != null) {
                    try
                    {
                        fos.close();
                    }
                    catch (Throwable localThrowable2)
                    {
                        localThrowable3.addSuppressed(localThrowable2);
                    }
                } else {
                    fos.close();
                }
            }
        }
    }

    public boolean removeFile(String path, String name)
            throws IOException
    {
        this.client.changeWorkingDirectory(this.config.getRootPath() + path);
        return this.client.deleteFile(name);
    }

    public void changeRemotePath(String path)
            throws IOException
    {
        this.client.changeWorkingDirectory(path);
    }

    public boolean createDirectory(String pathname)
            throws IOException
    {
        return this.client.makeDirectory(pathname);
    }
}
