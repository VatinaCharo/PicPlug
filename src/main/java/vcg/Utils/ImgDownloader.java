package vcg.Utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImgDownloader {
    /**
     * @param url 图片下载地址
     * @param imagePath 图片存放地址
     * @param id task id
     * @return file name
     * @throws IOException 下载错误或文件读写错误
     */
    public static String download(URL url, String imagePath, int id) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + id + ".jpg";
        InputStream in = url.openConnection().getInputStream();
        FileOutputStream out = new FileOutputStream(imagePath +  fileName);
        for (int imgByte; (imgByte = in.read()) != -1; ) {
            out.write(imgByte);
        }
        in.close();
        out.close();
        return fileName;
    }
    public static String download(String url, String imagePath) throws IOException {
        return download(new URL(url),imagePath,0);
    }
}
