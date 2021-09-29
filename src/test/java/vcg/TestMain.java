package vcg;

import vcg.Utils.ImgDownloader;
import vcg.Utils.Resources;

import java.io.IOException;
import java.net.URL;


public class TestMain {
    public static void main(String[] args){
        System.out.println(System.currentTimeMillis());
        try {
            System.out.println(ImgDownloader.download(new URL(Resources.IMG_API_URL), Resources.IMG_STORAGE_PATH,0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
