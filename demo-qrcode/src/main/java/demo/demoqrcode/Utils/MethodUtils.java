package demo.demoqrcode.Utils;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import demo.demoqrcode.Model.ResponseImage;


public class MethodUtils {

    public static byte[] generateQrCodeImage(RequestData requestData) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        BitMatrix bitMatrix = qrCodeWriter.encode(requestData.getRequestUrl(), BarcodeFormat.QR_CODE, requestData.getQrWidth(), requestData.getQrHeight());
        MatrixToImageConfig con = new MatrixToImageConfig(requestData.getQrCodeColorAsColor().getRGB(), requestData.getBackgroundColorAsColor().getRGB());

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
        int whiteBoxSize = (int) (Math.min(requestData.getQrWidth(), requestData.getQrHeight()) * 0.135);
        int whiteBoxX = (requestData.getQrWidth() - whiteBoxSize) / 2;
        int whiteBoxY = (requestData.getQrHeight() - whiteBoxSize) / 2;
    
        BufferedImage overlayImage = new BufferedImage(requestData.getQrWidth(), requestData.getQrHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = overlayImage.createGraphics();
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, requestData.getQrWidth(), requestData.getQrHeight());
        graphics.setColor(Color.WHITE);
        graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
        graphics.dispose();
    
        Graphics2D qrGraphics = image.createGraphics();
        qrGraphics.drawImage(overlayImage, 0, 0, null);
        qrGraphics.dispose();
    
        BufferedImage imageWithBorder = addBorder(image, requestData.getTopBorderSize(), requestData.getBottomBorderSize(), requestData.getLeftBorderSize(), requestData.getRightBorderSize(), requestData.getBorderColorAsColor());
        addTextToBorder(imageWithBorder, requestData.getTextBorder(), Color.black, 20, requestData.getBottomBorderSize());
    
        BufferedImage centerLogo = loadImageFromUrl(requestData.getLogoCenterUrl());
        centerLogo = resizeImage(centerLogo, whiteBoxSize, whiteBoxSize);
    
        BufferedImage borderLogo = loadImageFromUrl(requestData.getLogoBorderUrl());
        borderLogo = resizeImage(borderLogo, whiteBoxSize, whiteBoxSize);
    
        BufferedImage imageWithLogo = addLogoToCenter(imageWithBorder, centerLogo, requestData.getTopBorderSize(), requestData.getBottomBorderSize(), requestData.getLeftBorderSize(), requestData.getRightBorderSize());
        BufferedImage imageWithBothLogo = addLogoToBorder(imageWithLogo, borderLogo, 20, 10, requestData.getBottomBorderSize());
    
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageWithBothLogo, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public static byte[] generateQrCodeBase(String text) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 350,350);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig(0xFFFFFFFF, 0xFF000000);

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

   

    public static SeparateFields separateFields(RequestData requestData) {
        ArrayList<String> stringFields = new ArrayList<>();
        ArrayList<Integer> intFields = new ArrayList<>();

        Field[] fields = requestData.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            
            if (!fieldName.equals("requestUrl")){
                try {
                    Object value = field.get(requestData);
                    if (value instanceof String) {
                        stringFields.add((String) value);
                    } else if (value instanceof Integer) {
                        intFields.add((Integer) value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } 
            }
        }
        return new SeparateFields(stringFields, intFields);
    }

    public static boolean areAllZero(ArrayList<Integer> l){
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i)!=0) {
                return false;
            }
        }
        return true;
    }

    public static boolean areAllStringsEmpty(ArrayList<String> l){
        for (String string : l) {
            String cleanedString = string.replaceAll(" ", "").replaceAll(",", "");
            if (!cleanedString.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsOnlyEmptyStrings(ArrayList<String> l) {
        for (String string : l) {
            if (!string.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static ResponseImage result(byte[] qrBytes) throws IOException{
        
            BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrBytes));
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            ImageIO.write(qrCodeImage, "PNG", pngOutputStream);
            String base64 = Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
            base64 = "data:image/png;base64," + base64;

            ResponseImage response = new ResponseImage();
            response.setImageBase64(base64);
            return response;
    }


    public static byte[] qrCodeResult(RequestData requestData) throws WriterException, IOException{
        if (areAllZero(separateFields(requestData).getIntFields()) && containsOnlyEmptyStrings(separateFields(requestData).getStringFields())){
            return generateQrCodeBase(requestData.getRequestUrl());
        }else{
            return generateQrCodeImage(requestData);
        }
    }

    
    private static BufferedImage loadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }


    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        //L'immagine originale viene ridimensionata alle dimensioni desiderate utilizzando il metodo getScaledInstance, che ritorna un oggetto Image ridimensionato in base alle dimensioni specificate. Il parametro Image.SCALE_SMOOTH indica di utilizzare un algoritmo di ridimensionamento liscio.
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        //Viene creata una nuova immagine di tipo BufferedImage con le dimensioni desiderate e il tipo di immagine ARGB (Alfa, Rosso, Verde, Blu) che supporta la trasparenza.
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        //Viene ottenuto il contesto grafico (Graphics2D) per l'immagine di output appena creata.
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    public static BufferedImage addLogoToCenter(BufferedImage baseImage, BufferedImage logo, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize) {
        // Calcola le dimensioni effettive dell'immagine senza i bordi
        int effectiveWidth = baseImage.getWidth() - leftBorderSize - rightBorderSize;
        int effectiveHeight = baseImage.getHeight() - topBorderSize - bottomBorderSize;
    
        // Calcola le coordinate del logo
        int logoX = (effectiveWidth - logo.getWidth()) / 2 + leftBorderSize;
        int logoY = (effectiveHeight - logo.getHeight()) / 2 + topBorderSize;
    
        // Viene creata una nuova immagine con le stesse dimensioni dell'immagine di base, in cui verrà disegnato il logo.
        BufferedImage imageWithLogo = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithLogo.createGraphics();
        g.drawImage(baseImage, 0, 0, null);
        g.drawImage(logo, logoX, logoY, null);
        g.dispose();
    
        return imageWithLogo;
    }
    

    public static BufferedImage addBorder(BufferedImage img, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize, Color borderColor) {
        //Vengono calcolate le nuove dimensioni dell'immagine, tenendo conto delle dimensioni del bordo che verrà aggiunto.
        int newWidth = img.getWidth() + leftBorderSize + rightBorderSize;
        int newHeight = img.getHeight() + topBorderSize + bottomBorderSize;
    
        BufferedImage imgWithBorder = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imgWithBorder.createGraphics();
        g.setColor(borderColor);
        g.fillRect(0, 0, newWidth, newHeight);
        g.drawImage(img, leftBorderSize, topBorderSize, null);
        g.dispose();
    
        return imgWithBorder;
    }
    
    
    public static BufferedImage addLogoToBorder(BufferedImage baseImage, BufferedImage logo, int fontSize, int logoMargin, int bottomBorderSize) {
    
        // Calcola le coordinate Y del centro del testo
        int textCenterY = baseImage.getHeight() - bottomBorderSize / 2 - logo.getHeight()/2;

        // Disegna il logo
        Graphics2D g2 = baseImage.createGraphics();
        g2.drawImage(logo, 0, textCenterY, null);
        g2.dispose();
    
        return baseImage;
    }
    
    public static BufferedImage addTextToBorder(BufferedImage img, String text, Color textColor, int fontSize, int bottomBorderSize) {
        Graphics2D g = img.createGraphics();
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(font);
    
        // Calcola le dimensioni del testo
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(text);
        // int textHeight = metrics.getHeight();

        // //Calcola la larghezza del testo         
        FontMetrics fontMetrics = g.getFontMetrics(font);         

        // //Calcola l'altezza della lettera più alta del testo         
        int maxHeight = 0;             
        for (int i = 0; i < text.length(); i++) {                 
        int charHeight = fontMetrics.getAscent() - fontMetrics.getDescent();                
        maxHeight = Math.max(maxHeight, charHeight);             
        }         
 
        // Calcola le coordinate x e y per posizionare il testo al centro del bordo inferiore         
        // int x = (img.getWidth() - textWidth) / 2;         
        int y = img.getHeight()-bottomBorderSize/2+(maxHeight/2);

    
        // Calcola le coordinate x per posizionare il testo al centro del bordo inferiore
        int textX = (img.getWidth() - textWidth) / 2;
        // Calcola le coordinate y per posizionare il testo esattamente nella metà del bordo inferiore
        // int textY = img.getHeight() - bottomBorderSize / 2 + textHeight / 2; // Posiziona il testo esattamente nella metà del bordo inferiore
    
        // Disegna il testo
        g.drawString(text, textX, y);
        g.dispose();
    
        return img;
    }

}

