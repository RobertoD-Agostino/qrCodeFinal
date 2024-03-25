package demo.demoqrcode.Utils;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class MethodUtils {

    // public static byte[] generateQrCodeImage(String text, int qrWidth, int qrHeight, Color qrCodeColor, Color backgroundColor, Color borderColor,String textBorder, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize, String logoCenter, String logoBorder) throws WriterException, IOException {
    //     QRCodeWriter qrCodeWriter = new QRCodeWriter();
    //     BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
    //     MatrixToImageConfig con = new MatrixToImageConfig(qrCodeColor.getRGB(), backgroundColor.getRGB());
    //     BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
    //     // Calcola le dimensioni del riquadro bianco al centro
    //     int whiteBoxSize = (int) (Math.min(qrWidth, qrHeight) * 0.135); // Riduci la dimensione del riquadro bianco
    //     int whiteBoxX = (qrWidth - whiteBoxSize) / 2;
    //     int whiteBoxY = (qrHeight - whiteBoxSize) / 2;

    //     BufferedImage overlayImage = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_ARGB);
    //     Graphics2D graphics = overlayImage.createGraphics();
    //     graphics.setColor(new Color(255, 255, 255, 0)); // Trasparente
    //     graphics.fillRect(0, 0, qrWidth, qrHeight);
    //     graphics.setColor(Color.WHITE);
    //     graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
    //     graphics.dispose();

    //     // Il riquadro bianco viene sovrapposto all'immagine del codice QR.
    //     Graphics2D qrGraphics = image.createGraphics();
    //     qrGraphics.drawImage(overlayImage, 0, 0, null);
    //     qrGraphics.dispose();

    //     // Aggiungi un bordo all'immagine del codice QR utilizzando i margini specificati e un colore specifico.
    //     BufferedImage imageWithBorder = addBorder(image, topBorderSize, bottomBorderSize, leftBorderSize, rightBorderSize, borderColor);
        
    //     // Aggiungi del testo al bordo dell'immagine.
    //     addTextToBorder(imageWithBorder, textBorder, Color.black, 20, bottomBorderSize);

    //     // Carica il logo e ridimensionalo per adattarlo alle dimensioni del riquadro bianco.
    //     // String imagePath = "img/scanMe.png";
    //     // BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));

    //     BufferedImage centerLogo = loadImageFromUrl(logoCenter);
    //     centerLogo = resizeLogoForBorder(centerLogo, whiteBoxSize, whiteBoxSize);

    //     BufferedImage borderLogo = loadImageFromUrl(logoBorder);
    //     borderLogo = resizeImage(borderLogo, whiteBoxSize, whiteBoxSize);


    //     // Sovrappone il logo al centro dell'immagine del codice QR.
    //     BufferedImage imageWithLogo = addLogoToCenter(imageWithBorder, centerLogo, topBorderSize, bottomBorderSize, leftBorderSize, rightBorderSize);

    //     BufferedImage imageWithBothLogo = addLogoToBorder(imageWithLogo, borderLogo, 20, 10, bottomBorderSize);


    //     // Scrive l'immagine con il logo in un flusso di output in formato PNG.
    //     ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    //     ImageIO.write(imageWithBothLogo, "PNG", pngOutputStream);
    //     return pngOutputStream.toByteArray();
    // }

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
        centerLogo = resizeLogoForBorder(centerLogo, whiteBoxSize, whiteBoxSize);
    
        BufferedImage borderLogo = loadImageFromUrl(requestData.getLogoBorderUrl());
        borderLogo = resizeImage(borderLogo, whiteBoxSize, whiteBoxSize);
    
        BufferedImage imageWithLogo = addLogoToCenter(imageWithBorder, centerLogo, requestData.getTopBorderSize(), requestData.getBottomBorderSize(), requestData.getLeftBorderSize(), requestData.getRightBorderSize());
        BufferedImage imageWithBothLogo = addLogoToBorder(imageWithLogo, borderLogo, 20, 10, requestData.getBottomBorderSize());
    
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageWithBothLogo, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    



    private static BufferedImage loadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }
    
    // public static String encodeImageToBase64(byte[] imageBytes) {
    //     return Base64.getEncoder().encodeToString(imageBytes);
    // }

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
    
    
    public static BufferedImage resizeLogoForBorder(BufferedImage logo, int targetWidth, int targetHeight) {
        // Ridimensiona il logo nel bordo alle dimensioni desiderate
        return resizeImage(logo, targetWidth, targetHeight);
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
