package com.faceidentify.faceresize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

@Controller
public class FaceController {
    @Autowired
    private FaceService faceService;
    @Value("${server.port}")
    String port;
    @RequestMapping(value = "/identifyAndResizeFace",consumes = "multipart/form-data")
    @ResponseBody
    public BufferedImage identifyAndResizeFace(@RequestParam MultipartFile photo, HttpServletResponse response){
        //response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Type","image/jpeg");
        System.out.println(photo.getSize());
        ResponseMessage responseMessage=new ResponseMessage<BufferedImage>();
        try {
            responseMessage= faceService.identifyAndResizeFace(photo);
        }catch (Exception e){
            e.printStackTrace();
            responseMessage.setResponseCode("01");
        }
        return (BufferedImage)responseMessage.getData();
    }
}
