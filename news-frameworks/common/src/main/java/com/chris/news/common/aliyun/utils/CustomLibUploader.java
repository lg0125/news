package com.chris.news.common.aliyun.utils;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;

/**
 * 用于自定义图库上传图片
 */
public class CustomLibUploader {
    public String uploadFile(String host, String uploadFolder, String ossAccessKeyId,
                             String policy, String signature,
                             String filepath) throws Exception {
        LinkedHashMap<String, String> textMap = new LinkedHashMap<>();
        // key
        String objectName = uploadFolder + "/imglib_" + UUID.randomUUID() + ".jpg";
        textMap.put("key", objectName);
        // Content-Disposition
        textMap.put("Content-Disposition", "attachment;filename="+filepath);
        // OSSAccessKeyId
        textMap.put("OSSAccessKeyId", ossAccessKeyId);
        // policy
        textMap.put("policy", policy);
        // Signature
        textMap.put("Signature", signature);

        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("file", filepath);

        String ret = formUpload(host, textMap, fileMap);
        System.out.println("[" + host + "] post_object:" + objectName);
        System.out.println("post response:" + ret);
        return objectName;
    }

    private static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) throws Exception {
        String res;
        HttpURLConnection conn = null;
        String BOUNDARY = "9431149156168";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                int i = 0;
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    if (i == 0) {
                        strBuf.append("--").append(BOUNDARY).append(
                                "\r\n");
                    } else {
                        strBuf.append("\r\n").append("--").append(BOUNDARY).append(
                                "\r\n");

                    }
                    strBuf.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"\r\n\r\n");
                    strBuf.append(inputValue);

                    i++;
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                for (Map.Entry<String, String> stringStringEntry : fileMap.entrySet()) {
                    String inputName = (String) ((Map.Entry) stringStringEntry).getKey();
                    String inputValue = (String) ((Map.Entry) stringStringEntry).getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    String contentType = new MimetypesFileTypeMap().getContentType(file);
                    if (contentType == null || contentType.isEmpty()) {
                        contentType = "application/octet-stream";
                    }

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"").append(inputName).append("\"; filename=\"").append(filename).append("\"\r\n");
                    strBuf.append("Content-Type: ").append(contentType).append("\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(Files.newInputStream(file.toPath()));
                    int bytes;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
                StringBuffer strBuf = new StringBuffer();
                out.write(strBuf.toString().getBytes());
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
        } catch (Exception e) {
            System.err.println("发送POST请求出错: " + urlStr);
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }
}
