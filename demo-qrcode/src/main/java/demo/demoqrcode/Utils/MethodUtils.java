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

import Exceptions.BorderNotPresent;
import Exceptions.TopOrBottomBorderNotSpecifiedException;
import Exceptions.UrlNotPresentException;
import Exceptions.WidthAndHeightNotEnoughException;
import Exceptions.BorderColorNotPresent;
import demo.demoqrcode.Model.ResponseImage;


public class MethodUtils {

    //METODO PER GENERARE IL QRCODE CON TUTTE LE PERSONALIZZAZIONI
    public static byte[] generateQrCodeImage(RequestData requestData) throws WriterException, IOException, RuntimeException{
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        if (requestData.getQrWidth()<200 || requestData.getQrHeight() <200){
            throw new WidthAndHeightNotEnoughException("Se vuoi personalizzare le dimensioni devono essere minimo 200");
        }

        if (requestData.getRequestUrl().isEmpty()) {
            throw new UrlNotPresentException("Specificare un URL");
        }

        // Encode QR code with specified width, height, and URL
        BitMatrix bitMatrix = qrCodeWriter.encode(requestData.getRequestUrl(), BarcodeFormat.QR_CODE, requestData.getQrWidth(), requestData.getQrHeight());
        
        // // Set default colors if not provided
        if (requestData.getBackgroundColor().isEmpty()) {
            requestData.setBackgroundColor("#ffffff");
        }else if (requestData.getQrCodeColor().isEmpty()){
            requestData.setQrCodeColor("#000000");
        }


        
        MatrixToImageConfig con = new MatrixToImageConfig(requestData.getQrCodeColorAsColor().getRGB(), requestData.getBackgroundColorAsColor().getRGB());


        // Convert bitMatrix to BufferedImage
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
        
        // Add white box to the center
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
    
        // Merge QR code image with the white box
        Graphics2D qrGraphics = image.createGraphics();
        qrGraphics.drawImage(overlayImage, 0, 0, null);
        qrGraphics.dispose();
    
        // Add borders if provided
        if (requestData.getTopBorderSize() != 0 || requestData.getBottomBorderSize() != 0 ||
            requestData.getLeftBorderSize() != 0 || requestData.getRightBorderSize() != 0) {

            if (requestData.getBorderColor().isEmpty()) {
                // Throw exception if border color is provided without border sizes
                throw new BorderColorNotPresent("Inserire un colore per i bordi");
            }

            BufferedImage imageWithBorder = addBorder(image, requestData.getTopBorderSize(), requestData.getBottomBorderSize(),requestData.getLeftBorderSize(),requestData.getRightBorderSize(), requestData.getBorderColorAsColor());
            
            image = imageWithBorder;
        } else if (!requestData.getBorderColor().isEmpty()) {
            // Throw exception if border color is provided without border sizes
            throw new BorderNotPresent("Inserire almeno un bordo per inserire il colore");
        }
    
        
        // Add center logo if provided
        if (!requestData.getLogoCenterUrl().isEmpty()) {
            BufferedImage centerLogo = loadImageFromUrl(requestData.getLogoCenterUrl());
            centerLogo = resizeImage(centerLogo, whiteBoxSize, whiteBoxSize);
            image = addLogoToCenter(image, centerLogo, requestData.getTopBorderSize(), requestData.getBottomBorderSize(),
                    requestData.getLeftBorderSize(), requestData.getRightBorderSize());
        }
    
        if (requestData.getTopOrBottom()!=null) {
            // Add border logo if provided
            if (!requestData.getLogoBorderUrl().isEmpty()) {
                BufferedImage borderLogo = loadImageFromUrl(requestData.getLogoBorderUrl());
                borderLogo = resizeImage(borderLogo, whiteBoxSize, whiteBoxSize);

                if (requestData.getTopOrBottom().toLowerCase().equals("top")) {
                    image = addLogoToBorder(image, borderLogo, 20, 10, requestData.getTopBorderSize(),requestData.getTopOrBottom()); 
                    if (requestData.getTopBorderSize()<60) {
                        throw new TopOrBottomBorderNotSpecifiedException("Il bordo su cui inserire il logo o il testo deve essere minimo 60");
                    }
                }
                else if (requestData.getTopOrBottom().toLowerCase().equals("bottom")) {
                    if (requestData.getBottomBorderSize()<60) {
                    throw new TopOrBottomBorderNotSpecifiedException("Il bordo su cui inserire il logo o il testo deve essere minimo 60");
                    }
                    image = addLogoToBorder(image, borderLogo, 20, 10, requestData.getBottomBorderSize(),requestData.getTopOrBottom()); 
                }
                else{
                    throw new TopOrBottomBorderNotSpecifiedException("Specificare il bordo sul quale inserire testo e logo");
                }
            }

            // Add text to border if provided
            if (!requestData.getTextBorder().isEmpty()) {
                if (requestData.getTopOrBottom().toLowerCase().equals("top")){
                    if (requestData.getTopBorderSize()<60) {
                        throw new TopOrBottomBorderNotSpecifiedException("Il bordo su cui inserire il logo o il testo deve essere minimo 60");
                    }
                    addTextToBorder(image, requestData.getTextBorder(), Color.BLACK, 20, requestData.getTopBorderSize(),requestData.getTopOrBottom());
                }
                else if(requestData.getTopOrBottom().toLowerCase().equals("bottom")){
                    if (requestData.getBottomBorderSize()<60) {
                        throw new TopOrBottomBorderNotSpecifiedException("Il bordo su cui inserire il logo o il testo deve essere minimo 60");
                    }
                    addTextToBorder(image, requestData.getTextBorder(), Color.BLACK, 20, requestData.getBottomBorderSize(),requestData.getTopOrBottom());
                }
                else{
                    throw new TopOrBottomBorderNotSpecifiedException("Specificare il bordo sul quale inserire testo e logo");
                }
            } 
        }
        

        

        // Convert BufferedImage to byte array
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    
    // public static void requestText(RequestData requestData){

    // }



    //METODO PER GENERARE IL QRCODE BASE SENZA PERSONALIZZAZIONI
    public static byte[] generateQrCodeBase(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width,height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

   
    //METODO PER CREARE LE LISTE CON I PARAMETRI DELLA CLASSE REQUESTDATA
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

    //METODO PER VERIFICARE SE NELLA LISTA DEI PARAMETRI INT SONO TUTTI ZERI
    public static boolean areAllZero(ArrayList<Integer> l){
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i)!=0) {
                return false;
            }
        }
        return true;
    }

    //METODO PER VERIFICARE SE LA LISTA DI PARAMETRI STRING E' VUOTA 
    public static boolean containsOnlyEmptyStrings(ArrayList<String> l) {
        for (String string : l) {
            if (!string.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //METODO PER RESTITUIRE IL RISULTATO AL CONTROLLER
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

    //METODO PER SCEGLIERE CHE TIPO DI QRCODE GENERARE
    public static byte[] qrCodeResult(RequestData requestData) throws WriterException, IOException, RuntimeException{

        boolean areZero = areAllZero(separateFields(requestData).getIntFields());
        boolean emptyString = containsOnlyEmptyStrings(separateFields(requestData).getStringFields());
        int width = requestData.getQrWidth();
        int height = requestData.getQrHeight();


        if ((areZero && emptyString)){
            width = 350;
            height = 350;
            System.out.println("COndizione base");

            return generateQrCodeBase(requestData.getRequestUrl(), width, height);

        }else if(emptyString && width!=0 || emptyString && height!=0){
            if (width<200 || height <200){
                throw new WidthAndHeightNotEnoughException("Se vuoi personalizzare le dimensioni devono essere minimo 200");
            }
            System.out.println("COndizione base con controllo");

            return generateQrCodeBase(requestData.getRequestUrl(), width, height);
        }

        else{
            
            return generateQrCodeImage(requestData);
        }
    }

    public static ResponseEntity handleRuntimeException(RuntimeException e) {
        if (e instanceof WidthAndHeightNotEnoughException || e instanceof BorderNotPresent || e instanceof BorderColorNotPresent || e instanceof UrlNotPresentException || e instanceof TopOrBottomBorderNotSpecifiedException) {
            String errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    
    //METODO PER CARICARE UN IMMAGINE DA UN URL
    private static BufferedImage loadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }

    

    //METODO PER RIDIMENSIONARE IL LOGO ALLE DIMENSIONI DEL QUADRATO BIANCO
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

    //METODO PER AGGIUNGERE IL LOGO AL CENTRO
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
    
    //METODO PER AGGIUNGERE IL BORDO
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
    
    //METODO PER AGGIUNGERE IL LOGO AL BORDO
    public static BufferedImage addLogoToBorder(BufferedImage baseImage, BufferedImage logo, int fontSize, int logoMargin, int borderSize, String topOrBottom) {
    
        // Calcola le coordinate Y del centro del testo
        // int textCenterY = baseImage.getHeight() - borderSize / 2 - logo.getHeight()/2;

    // Calcola le coordinate Y del centro del testo
    int textCenterY;
    if (topOrBottom.equalsIgnoreCase("top")) {
        textCenterY = borderSize / 2 - logo.getHeight() / 2;
    } else {
        textCenterY = baseImage.getHeight() - borderSize / 2 - logo.getHeight() / 2;
    }

        // Disegna il logo
        Graphics2D g2 = baseImage.createGraphics();
        g2.drawImage(logo, 0, textCenterY, null);
        g2.dispose();
    
        return baseImage;
    }
    
    //METODO PER AGGIUNGERE IL TESTO AL BORDO
    public static BufferedImage addTextToBorder(BufferedImage img, String text, Color textColor, int fontSize, int borderSize, String topOrBottom) {
        if (text.isEmpty()) {
            // Se il testo nel bordo non è presente, non fare nulla
            return img;
        }
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
        // int y = img.getHeight()-borderSize/2+(maxHeight/2);

        // Calcola le coordinate x per posizionare il testo al centro del bordo inferiore
        int textX = (img.getWidth() - textWidth) / 2;
        // Calcola le coordinate y per posizionare il testo esattamente nella metà del bordo inferiore
        // int textY = img.getHeight() - bottomBorderSize / 2 + textHeight / 2; // Posiziona il testo esattamente nella metà del bordo inferiore
        int textY;
        if (topOrBottom.equalsIgnoreCase("top")) {
            textY = borderSize / 2 + (maxHeight/2);
        } else {
            textY = img.getHeight() - borderSize / 2 + (maxHeight/2);
        }
        // Disegna il testo
 
        g.drawString(text, textX, textY);
        g.dispose();
    
        return img;
    }

}

