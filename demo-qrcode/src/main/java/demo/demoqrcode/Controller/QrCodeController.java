package demo.demoqrcode.Controller;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.demoqrcode.Model.ResponseImage;
import demo.demoqrcode.Utils.MethodUtils;
import demo.demoqrcode.Utils.RequestData;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Base64;

import javax.imageio.ImageIO;

@RestController
public class QrCodeController {

    @PostMapping("/generate")
    public ResponseEntity<ResponseImage> downloadQrCodeBase64(@RequestBody RequestData requestData) {
        try 
        {
            byte[] qrCodeBytes = MethodUtils.qrCodeResult(requestData);              
            ResponseImage response = MethodUtils.result(qrCodeBytes);
            
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    
}

