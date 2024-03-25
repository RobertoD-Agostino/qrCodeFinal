package demo.demoqrcode.Controller;




import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;

import demo.demoqrcode.Model.ResponseImage;
import demo.demoqrcode.Utils.MethodUtils;
import demo.demoqrcode.Utils.RequestData;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

@RestController
public class QrCodeController {


//     public static final int DEFAULT_QR_WIDTH = 350;
//     public static final int DEFAULT_QR_HEIGHT = 350;

//     @PostMapping("/generate")
// public ResponseEntity<ResponseImage> downloadQrCodeBase64(@RequestParam String requestUrl) {
//     try {
//         byte[] qrCodeBytes = MethodUtils.generateQrCodeImage(requestUrl, DEFAULT_QR_WIDTH, DEFAULT_QR_HEIGHT, Color.decode("#4b0082"), Color.decode("#ffff66"));
//         BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
        
//         String imagePath = "img/phone2.png";
//         BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));

//         // Aggiungi il logo all'immagine con il testo
//         BufferedImage finalImage = MethodUtils.addLogoToBorder(qrCodeImage, logo,20,10,80);
        
//         ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//         ImageIO.write(finalImage, "PNG", pngOutputStream);
//         String base64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
//         base64 = "data:image/png;base64," + base64;
        
//         ResponseImage response = new ResponseImage();
//         response.setImageBase64(base64);
        
//         return ResponseEntity.status(HttpStatus.OK).body(response);
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
//     }
// }


@PostMapping("/generate")
public ResponseEntity<ResponseImage> downloadQrCodeBase64(@RequestBody RequestData requestData) {
    try {
        byte[] qrCodeBytes = MethodUtils.generateQrCodeImage(
    requestData.getRequestUrl(), 
    requestData.getQrWidth(), 
    requestData.getQrHeight(), 
    requestData.getQrWidth(), // Usa le stesse dimensioni per l'immagine complessiva
    requestData.getQrHeight(), // Usa le stesse dimensioni per l'immagine complessiva
    requestData.getQrCodeColorAsColor(), 
    requestData.getBackgroundColorAsColor(),
    requestData.getBorderColorAsColor(),
    requestData.getTopBorderSize(),
    requestData.getBottomBorderSize(),
    requestData.getLeftBorderSize(),
    requestData.getRightBorderSize()
);

        BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));

        String imagePath = "img/phone2.png";
        BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));

        // Aggiungi il logo all'immagine con il testo
        BufferedImage finalImage = MethodUtils.addLogoToBorder(qrCodeImage, logo, 20, 10, 80);

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



// @PostMapping("/generate")
//     public ResponseEntity<ResponseImage> generateQrCodeImage(@RequestBody RequestData requestData) throws NumberFormatException, WriterException {
//         try {
//             byte[] qrCodeBytes = MethodUtils.generateQrCodeImage(
//                     requestData.getRequestUrl(),
//                     requestData.getQrWidth(),
//                     requestData.getQrHeight(),
//                     Color.decode(requestData.getQrCodeColor()),
//                     Color.decode(requestData.getBackgroundColor()),
//                     Color.decode(requestData.getBorderColor()),
//                     requestData.getTopBorderSize(),
//                     requestData.getBottomBorderSize(),
//                     requestData.getLeftBorderSize(),
//                     requestData.getRightBorderSize()
//             );

//             // String base64 = MethodUtils.encodeImageToBase64(qrCodeBytes);

//             // ResponseImage response = new ResponseImage();
//             // response.setImageBase64(base64);


//             ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

//             String base64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
//             base64 = "data:image/png;base64," + base64;
            
//             ResponseImage response = new ResponseImage();
//             response.setImageBase64(base64);

//             return ResponseEntity.status(HttpStatus.OK).body(response);
//         } catch (IOException e) {
//             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//         }
//     }