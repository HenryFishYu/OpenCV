package com.faceidentify.faceresize;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

@Service
public class FaceService {
    public ResponseMessage identifyAndResizeFace(MultipartFile file) throws Exception{
        ResponseMessage responseMessage=new ResponseMessage<BufferedImage>();
            BufferedImage bufferedImage= ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {//如果image=null 表示上传的不是图片格式
                responseMessage.setResponseCode("02");
                return responseMessage;
            }
        System.load("D:\\OpenCV\\opencv\\build\\java\\x64\\opencv_java341.dll");
        System.out.println("\nRunning FaceDetector");
        CascadeClassifier faceDetector = new CascadeClassifier();
        faceDetector.load(
                "D:\\OpenCV\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt2.xml");
        Mat image = Imgcodecs.imdecode(new MatOfByte(file.getBytes()), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image,faceDetections,(double)(2),5,0,new Size(0,0),new Size(1000,1000));
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
        if(maxRect.area()>0) {
            //创建人脸拷贝区域
            Mat roi_img = new Mat(image, maxRect);
            //创建临时的人脸拷贝图形
            Mat tmp_img = new Mat();
            //人脸拷贝
            roi_img.copyTo(tmp_img);
            Imgproc.resize(tmp_img,tmp_img,new Size(50,50));
            responseMessage.setResponseCode("00");
            byte[] return_buff = new byte[(int) (tmp_img.total() *
                    tmp_img.channels())];
            tmp_img.get(0, 0, return_buff);
            responseMessage.setData(Mat2BufferedImage(tmp_img));
            return responseMessage;
        }
        if(maxRect.area()<=0){
            responseMessage.setResponseCode("03");
            return responseMessage;
        }
        return null;
    }
    static BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte ba[]=mob.toArray();

        BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
        return bi;
    }
}
