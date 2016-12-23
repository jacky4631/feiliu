//
// Decompiled by Procyon v0.5.30
//

package com.jiam365.flow.base.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;

public class DownloadUtils
{
    public static void download(final HttpServletRequest request, final HttpServletResponse response, final String srcFileName, final String displayFileName) {
        final File file = new File(srcFileName);
        if (!file.exists()) {
            return;
        }
        try (final BufferedInputStream is = new BufferedInputStream(new FileInputStream(srcFileName));
             final BufferedOutputStream os = new BufferedOutputStream((OutputStream)response.getOutputStream())) {
            response.setContentType(request.getServletContext().getMimeType(displayFileName));
            final String dispalyName = encode(displayFileName, request);
            response.setHeader("Content-disposition", "attachment;filename=" + dispalyName);
            final long contentLength = file.length();
            response.setHeader("Content-Length", String.valueOf(contentLength));
            final byte[] buf = new byte[2048];
            int bytesRead;
            while ((bytesRead = is.read(buf, 0, buf.length)) != -1) {
                os.write(buf, 0, bytesRead);
            }
            os.flush();
        }
        catch (Exception e) {
            throw new RuntimeException("Download error", e);
        }
    }

    private static void writeBOM(final OutputStream out) throws IOException {
        out.write(new byte[] { -17, -69, -65 });
    }

    private static String encode(final String displayFileName, final HttpServletRequest request) throws UnsupportedEncodingException {
        final String userAgent = request.getHeader("user-agent");
        if (isIE(userAgent)) {
            return URLEncoder.encode(displayFileName, "utf-8");
        }
        return new String(displayFileName.getBytes("utf-8"), "ISO-8859-1");
    }

    private static boolean isIE(final String agent) {
        return agent != null && (agent.toLowerCase().contains("msie") || agent.toLowerCase().contains("trident"));
    }
}
