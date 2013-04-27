package kr.ac.inha.itlab.daegikim;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenCV245FaceDetectionTest {
    public static CascadeClassifier faceDetector = null;
    public static Mat image = null;
    public static MatOfRect faceDetections = null;

    public static void main(String[] args) {
        System.loadLibrary("lib/opencv_java245");

        faceDetector = new CascadeClassifier("data/lbpcascade_frontalface.xml");
        faceDetections = new MatOfRect();
        List<File> fileList = new ArrayList<File>();

        walk(fileList, "data/input");
        detection(fileList);
    }

    /**
     * 파라미터로 받은 파일리스트(이미지)에서 얼굴을 검출한다.
     * 검출 결과를 이미지에 Drawing 하여 data/output 경로에 출력한다.
     * @param fileList
     */
    public static void detection(List<File> fileList) {
        for(File f : fileList){
            image = Highgui.imread(f.getAbsolutePath());
            //faceDetector.detectMultiScale(image, faceDetections);
            faceDetector.detectMultiScale(image,faceDetections, 1.4, 1, 0, new Size(128,128), new Size(256,256));

            for (Rect rect : faceDetections.toArray()) {
                Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
            }

            String filename = "data/output/"+f.getName();
            System.out.println(String.format("Writing %s", filename));
            Highgui.imwrite(filename, image);
        }
    }

    /**
     * recursively directory walk method
     * 파라미터로 받은 경로를 재귀적으로 탐색하면서 파일 리스트를 생성한다.
     * @param fileList
     * @param path
     */
    public static void walk(List<File> fileList, String path ) {
        File root = new File( path );
        File[] list = root.listFiles();

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                walk(fileList, f.getAbsolutePath() );
            }
            else {
                fileList.add(f);
            }
        }
    }
}