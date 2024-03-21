package demo.demoqrcode.Controller;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.demoqrcode.Model.ResponseImage;
import demo.demoqrcode.Utils.MethodUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

@RestController
public class QrCodeController {

    public static final int DEFAULT_QR_WIDTH = 350;
    public static final int DEFAULT_QR_HEIGHT = 350;

    @PostMapping("/generate")
public ResponseEntity<ResponseImage> downloadQrCodeBase64(@RequestParam String requestUrl) {
    try {
        byte[] qrCodeBytes = MethodUtils.generateQrCodeImage(requestUrl, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT, Color.decode("#4b0082"), Color.decode("#ffff66"));
        BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
        
        String imagePath = "img/phone2.png";
        BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));

        // Aggiungi il logo all'immagine con il testo
        BufferedImage finalImage = MethodUtils.addLogoToBorder(qrCodeImage, logo,20,10,80);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(finalImage, "PNG", pngOutputStream);
        String base64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
        base64 = "data:image/png;base64," + base64;
        
        ResponseImage response = new ResponseImage();
        response.setImageBase64(base64);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}


}