package hunmanfacecomponent;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.UUID;

@Service
public class FaceService {
    MultiLayerNetwork multiLayerNetwork;

    public FaceService() throws Exception{
            this.multiLayerNetwork = ModelSerializer.restoreMultiLayerNetwork("D:\\humanface.bin");
    }

    public String getHumanResult(String uuid) throws  Exception{
        String path="D:\\ResizedFaces\\";
        File trainData = new File(path+uuid+".jpg");
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS,true);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();
        ImageRecordReader recordReader = new ImageRecordReader(100,100,1,labelMaker);
        recordReader.initialize(train);
        DataSetIterator dataIter = new RecordReaderDataSetIterator(recordReader,1,1,1);
        DataNormalization scaler = new ImagePreProcessingScaler(0,1);
        scaler.fit(dataIter);
        dataIter.setPreProcessor(scaler);
        DataSet img = dataIter.next();
        String[] softMax=multiLayerNetwork.output(img.getFeatureMatrix() , false).toString()
                .replace("[","")
                .replace("]","")
                .replace(" ","")
                .split(",");
        double tempMax=0;
        int tempIndex=0;
        for(int i=0;i<softMax.length;i++){
            if(Double.valueOf(softMax[i])>tempMax){
                tempMax=Double.valueOf(softMax[i]);
                tempIndex=i;
            }
        }

        System.out.println(multiLayerNetwork.output(img.getFeatureMatrix() , false).toString());
        System.out.println("Max:"+multiLayerNetwork.output(img.getFeatureMatrix() , false).maxNumber()+"  MaxIndex:"+tempIndex);
        return Singleton.INSTANCE.getNameById(tempIndex);
    }
    public ResponseMessage identifyFace(MultipartFile file) throws Exception{
        ResponseMessage<String> responseMessage=new ResponseMessage();
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
        if(maxRect.area()>0) {
            Mat roi_img = new Mat(image, maxRect);
            Mat tmp_img = new Mat();
            roi_img.copyTo(tmp_img);
            Imgproc.resize(tmp_img,tmp_img,new Size(100,100));
            String uuid=UUID.randomUUID().toString();
            String path = "D:\\ResizedFaces\\";
            System.out.println(path+uuid);
            Imgcodecs.imwrite(path+uuid+".jpg", tmp_img);
            responseMessage.setResponseCode("00");
            responseMessage.setData(uuid);
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
