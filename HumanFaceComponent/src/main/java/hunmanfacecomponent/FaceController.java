package hunmanfacecomponent;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
@Controller
public class FaceController {
    @Autowired
    HttpServletResponse response;
    @Autowired
    private FaceService faceService;
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "/getHumanResult")
    @ResponseBody
    public ResponseMessage getHumanResult(String uuid) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        ResponseMessage<String> responseMessage=new ResponseMessage();
        try{
            responseMessage.setData(faceService.getHumanResult(uuid));
            responseMessage.setResponseCode("00");
        }catch (Exception e){
            e.printStackTrace();
            responseMessage.setResponseCode("01");
        }
        return responseMessage;
    }
    @RequestMapping(value = "/getResizedFace")
    @ResponseBody
    public void getResizedFace(String uuid) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        String path = "D:\\ResizedFaces\\";
        File photo=new File(path+uuid+".jpg");
        response.setContentType("image/jpg");
        response.getOutputStream().write(File2byte(photo));
    }
    @RequestMapping(value = "/identifyFace",consumes = "multipart/form-data")
    @ResponseBody
    public ResponseMessage identifyFace(@RequestParam MultipartFile photo){
        response.setHeader("Access-Control-Allow-Origin", "*");
        ResponseMessage responseMessage=new ResponseMessage<String>();
        try {
            responseMessage= faceService.identifyFace(photo);
        }catch (Exception e){
            e.printStackTrace();
            responseMessage.setResponseCode("01");
        }
        return responseMessage;
    }
    public static void responseSetting(){

    }
    public static byte[] File2byte(File file)
    {
        byte[] buffer = null;
        try
        {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }
}
