import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

public class Demo {
    public static double calcArea(Rect rect)
    {
        return rect.width*rect.height;
    }
    public static void main(String[] args)
    {
        String name="6.jpg";
        // TODO Auto-generated method stub
        System.load("D:\\OpenCV\\opencv\\build\\java\\x64\\opencv_java341.dll");
        System.out.println("\nRunning FaceDetector");
        CascadeClassifier faceDetector = new CascadeClassifier();
        faceDetector.load(
                "D:\\OpenCV\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml");
        Mat image = Imgcodecs.imread("C:\\Users\\HenryYu\\Desktop\\zjl\\"+name,CV_LOAD_IMAGE_GRAYSCALE);
        Mat image1=new Mat();
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image,faceDetections);
        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
        Rect maxRect=new Rect(0,0,0,0);
        for (Rect rect : faceDetections.toArray())
        {
            if(maxRect.area()<rect.area())
            {
                maxRect=rect;

            }
            //给脸上面画矩形
            //Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }
        if(calcArea(maxRect)>0) {
            //创建人脸拷贝区域
            Mat roi_img = new Mat(image, maxRect);
            //创建临时的人脸拷贝图形
            Mat tmp_img = new Mat();
            //人脸拷贝
            roi_img.copyTo(tmp_img);
            Imgproc.resize(tmp_img,tmp_img,new Size(100,100));
            String filename = "D:\\faces\\zjl\\"+name;
            Imgcodecs.imwrite(filename, tmp_img);
        }

    }
}
