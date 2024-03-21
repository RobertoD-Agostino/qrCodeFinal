package demo.demoqrcode.Utils;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class MethodUtils {

    public static byte[] generateQrCodeImage(String text, int width, int height, Color qrCodeColor, Color backgroundColor) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageConfig con = new MatrixToImageConfig(qrCodeColor.getRGB(), backgroundColor.getRGB());
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
        // Calcola le dimensioni del riquadro bianco al centro
        int whiteBoxSize = (int) (Math.min(width, height) * 0.135); // Riduci la dimensione del riquadro bianco
        int whiteBoxX = (width - whiteBoxSize) / 2;
        int whiteBoxY = (height - whiteBoxSize) / 2;

        // Imposta il riquadro bianco trasparente
        BufferedImage overlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = overlayImage.createGraphics();
        graphics.setColor(new Color(255, 255, 255, 0)); // Trasparente
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
        graphics.dispose();

        int topBorder = 15;
        int bottomBorder = 80;
        int leftBorder = 15;
        int rightBorder = 15;

        // Sovrappone l'immagine al codice QR
        Graphics2D qrGraphics = image.createGraphics();
        qrGraphics.drawImage(overlayImage, 0, 0, null);
        qrGraphics.dispose();
        BufferedImage imageWithBorder = addBorder(image, topBorder, bottomBorder, leftBorder, rightBorder, Color.BLUE);
        addTextToBorder(imageWithBorder, "SCAN ME", Color.black, 20,bottomBorder);

        String imagePath = "img/scanMe.png";
        BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));

        logo = resizeImage(logo, whiteBoxSize, whiteBoxSize);
        BufferedImage imageWithLogo =addLogoToCenter(imageWithBorder, logo,topBorder,bottomBorder,leftBorder,rightBorder);
        
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageWithLogo, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    public static BufferedImage addLogoToCenter(BufferedImage baseImage, BufferedImage logo, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize) {
        // Calcola le dimensioni effettive dell'immagine comprensiva dei bordi
        int effectiveWidth = baseImage.getWidth() - leftBorderSize - rightBorderSize;
        int effectiveHeight = baseImage.getHeight() - topBorderSize - bottomBorderSize;
    
        // Calcola le coordinate del logo
        int logoX = (effectiveWidth - logo.getWidth()) / 2 + leftBorderSize;
        int logoY = (effectiveHeight - logo.getHeight()) / 2 + topBorderSize;
    
        // Crea un'immagine con il logo centrato
        BufferedImage imageWithLogo = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageWithLogo.createGraphics();
        g.drawImage(baseImage, 0, 0, null);
        g.drawImage(logo, logoX, logoY, null);
        g.dispose();
    
        return imageWithLogo;
    }
    

    public static BufferedImage addBorder(BufferedImage img, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize, Color borderColor) {
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
        // Calcola la larghezza del testo
        Graphics2D g = baseImage.createGraphics();
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        FontMetrics metrics = g.getFontMetrics();
        int textHeight = metrics.getHeight();
        g.dispose();
    
        // Calcola le coordinate Y del centro del testo
        int textCenterY = baseImage.getHeight() - bottomBorderSize / 2 + textHeight / 2;
    
        // Calcola le coordinate Y del centro del logo
        int logoCenterY = (logo.getHeight() / 2) + logoMargin;
    
        // Calcola la posizione verticale del logo
        int logoY = textCenterY - logoCenterY;
    
        // Disegna il logo
        Graphics2D g2 = baseImage.createGraphics();
        g2.drawImage(logo, 0, logoY, null);
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
