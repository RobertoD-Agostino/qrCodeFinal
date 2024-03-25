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
import java.util.Base64;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class MethodUtils {

    // , String logoCenterUrl, String logoBorderUrl, String requestUrl, int qrWidth, int qrHeight, Color qrCodeColor, Color backgroundColor, Color borderColor, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize
    
// public static byte[] generateQrCodeImage(String text, int width, int height, Color qrCodeColor, Color backgroundColor) throws WriterException, IOException {
//     //Viene istanziato un oggetto QRCodeWriter, che è fornito dalla libreria ZXing e sarà utilizzato per generare il codice QR.
//     QRCodeWriter qrCodeWriter = new QRCodeWriter();
//     //Viene generata la matrice di bit (BitMatrix) del codice QR utilizzando il testo fornito, il formato del codice a barre (QR_CODE) e le dimensioni desiderate (larghezza e altezza).La matrice di bit viene utilizzata per rappresentare il modello del codice QR. Ogni bit nella matrice corrisponde a un modulo (punto) nel codice QR. I bit sono organizzati in una griglia che rappresenta il contenuto del codice QR stesso, inclusi dati e posizioni di riferimento. Questa matrice di bit è l'essenza del codice QR.
//     BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//     //Viene creato un oggetto MatrixToImageConfig con i colori specificati per il codice QR e lo sfondo.L'oggetto MatrixToImageConfig è utilizzato per specificare le opzioni di configurazione durante la conversione della matrice di bit in un'immagine.
//     MatrixToImageConfig con = new MatrixToImageConfig(qrCodeColor.getRGB(), backgroundColor.getRGB());
//     //L'immagine del codice QR viene generata dalla matrice di bit utilizzando i colori specificati tramite MatrixToImageWriter.
//     BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
//     // Calcola le dimensioni del riquadro bianco al centro
//     int whiteBoxSize = (int) (Math.min(width, height) * 0.135); // Riduci la dimensione del riquadro bianco
//     int whiteBoxX = (width - whiteBoxSize) / 2;
//     int whiteBoxY = (height - whiteBoxSize) / 2;

//     //Viene creata un'immagine sovrapposta che conterrà il riquadro bianco.
//     BufferedImage overlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//     Graphics2D graphics = overlayImage.createGraphics();
//     //Viene disegnato un rettangolo bianco trasparente su overlayImage, che sarà il riquadro bianco sopra il codice QR.
//     graphics.setColor(new Color(255, 255, 255, 0)); // Trasparente
//     graphics.fillRect(0, 0, width, height);
//     graphics.setColor(Color.WHITE);
//     graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
//     graphics.dispose();

//     int topBorder = 15;
//     int bottomBorder = 80;
//     int leftBorder = 15;
//     int rightBorder = 15;

//     // Il riquadro bianco viene sovrapposto all'immagine del codice QR.
//     Graphics2D qrGraphics = image.createGraphics();
//     qrGraphics.drawImage(overlayImage, 0, 0, null);
//     qrGraphics.dispose();
//     //Viene aggiunto un bordo all'immagine del codice QR utilizzando i margini specificati e un colore specifico.
//     BufferedImage imageWithBorder = addBorder(image, topBorder, bottomBorder, leftBorder, rightBorder, Color.decode("#4b0082"));
//     //Viene aggiunto del testo al bordo dell'immagine.
//     addTextToBorder(imageWithBorder, "SCAN ME", Color.black, 20,bottomBorder);

//     String imagePath = "img/scanMe.png";
//     BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));
//     //L'immagine del logo viene ridimensionata per adattarsi alle dimensioni del riquadro bianco.
//     logo = resizeImage(logo, whiteBoxSize, whiteBoxSize);
//     //Il logo viene sovrapposto al centro dell'immagine del codice QR
//     BufferedImage imageWithLogo =addLogoToCenter(imageWithBorder, logo,topBorder,bottomBorder,leftBorder,rightBorder);
//     //L'immagine con il logo viene scritta in un flusso di output in formato PNG.
//     ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//     ImageIO.write(imageWithLogo, "PNG", pngOutputStream);
//     return pngOutputStream.toByteArray();
// }

public static byte[] generateQrCodeImage(String text, int width, int height, int qrWidth, int qrHeight, Color qrCodeColor, Color backgroundColor, Color borderColor, int topBorderSize, int bottomBorderSize, int leftBorderSize, int rightBorderSize) throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, qrWidth, qrHeight);
    MatrixToImageConfig con = new MatrixToImageConfig(qrCodeColor.getRGB(), backgroundColor.getRGB());
    BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
    // Calcola le dimensioni del riquadro bianco al centro
    int whiteBoxSize = (int) (Math.min(qrWidth, qrHeight) * 0.135); // Riduci la dimensione del riquadro bianco
    int whiteBoxX = (qrWidth - whiteBoxSize) / 2;
    int whiteBoxY = (qrHeight - whiteBoxSize) / 2;

    BufferedImage overlayImage = new BufferedImage(qrWidth, qrHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = overlayImage.createGraphics();
    graphics.setColor(new Color(255, 255, 255, 0)); // Trasparente
    graphics.fillRect(0, 0, qrWidth, qrHeight);
    graphics.setColor(Color.WHITE);
    graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
    graphics.dispose();

    // Il riquadro bianco viene sovrapposto all'immagine del codice QR.
    Graphics2D qrGraphics = image.createGraphics();
    qrGraphics.drawImage(overlayImage, 0, 0, null);
    qrGraphics.dispose();

    // Aggiungi un bordo all'immagine del codice QR utilizzando i margini specificati e un colore specifico.
    BufferedImage imageWithBorder = addBorder(image, topBorderSize, bottomBorderSize, leftBorderSize, rightBorderSize, borderColor);
    
    // Aggiungi del testo al bordo dell'immagine.
    addTextToBorder(imageWithBorder, "SCAN ME", Color.black, 20, bottomBorderSize);

    // Carica il logo e ridimensionalo per adattarlo alle dimensioni del riquadro bianco.
    String imagePath = "img/scanMe.png";
    BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));
    logo = resizeLogoForBorder(logo, whiteBoxSize, whiteBoxSize);

    // Sovrappone il logo al centro dell'immagine del codice QR.
    BufferedImage imageWithLogo = addLogoToCenter(imageWithBorder, logo, topBorderSize, bottomBorderSize, leftBorderSize, rightBorderSize);

    // Scrive l'immagine con il logo in un flusso di output in formato PNG.
    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    ImageIO.write(imageWithLogo, "PNG", pngOutputStream);
    return pngOutputStream.toByteArray();
}



    private static BufferedImage loadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }
    
    public static String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
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


// public static byte[] generateQrCodeImage(String text, int width, int height, Color qrCodeColor, Color backgroundColor) throws WriterException, IOException {
//         //Viene istanziato un oggetto QRCodeWriter, che è fornito dalla libreria ZXing e sarà utilizzato per generare il codice QR.
//         QRCodeWriter qrCodeWriter = new QRCodeWriter();
//         //Viene generata la matrice di bit (BitMatrix) del codice QR utilizzando il testo fornito, il formato del codice a barre (QR_CODE) e le dimensioni desiderate (larghezza e altezza).La matrice di bit viene utilizzata per rappresentare il modello del codice QR. Ogni bit nella matrice corrisponde a un modulo (punto) nel codice QR. I bit sono organizzati in una griglia che rappresenta il contenuto del codice QR stesso, inclusi dati e posizioni di riferimento. Questa matrice di bit è l'essenza del codice QR.
//         BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
//         //Viene creato un oggetto MatrixToImageConfig con i colori specificati per il codice QR e lo sfondo.L'oggetto MatrixToImageConfig è utilizzato per specificare le opzioni di configurazione durante la conversione della matrice di bit in un'immagine.
//         MatrixToImageConfig con = new MatrixToImageConfig(qrCodeColor.getRGB(), backgroundColor.getRGB());
//         //L'immagine del codice QR viene generata dalla matrice di bit utilizzando i colori specificati tramite MatrixToImageWriter.
//         BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix, con);
//         // Calcola le dimensioni del riquadro bianco al centro
//         int whiteBoxSize = (int) (Math.min(width, height) * 0.135); // Riduci la dimensione del riquadro bianco
//         int whiteBoxX = (width - whiteBoxSize) / 2;
//         int whiteBoxY = (height - whiteBoxSize) / 2;

//         //Viene creata un'immagine sovrapposta che conterrà il riquadro bianco.
//         BufferedImage overlayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//         Graphics2D graphics = overlayImage.createGraphics();
//         //Viene disegnato un rettangolo bianco trasparente su overlayImage, che sarà il riquadro bianco sopra il codice QR.
//         graphics.setColor(new Color(255, 255, 255, 0)); // Trasparente
//         graphics.fillRect(0, 0, width, height);
//         graphics.setColor(Color.WHITE);
//         graphics.fillRect(whiteBoxX, whiteBoxY, whiteBoxSize, whiteBoxSize);
//         graphics.dispose();

//         int topBorder = 15;
//         int bottomBorder = 80;
//         int leftBorder = 15;
//         int rightBorder = 15;

//         // Il riquadro bianco viene sovrapposto all'immagine del codice QR.
//         Graphics2D qrGraphics = image.createGraphics();
//         qrGraphics.drawImage(overlayImage, 0, 0, null);
//         qrGraphics.dispose();
//         //Viene aggiunto un bordo all'immagine del codice QR utilizzando i margini specificati e un colore specifico.
//         BufferedImage imageWithBorder = addBorder(image, topBorder, bottomBorder, leftBorder, rightBorder, Color.decode("#4b0082"));
//         //Viene aggiunto del testo al bordo dell'immagine.
//         addTextToBorder(imageWithBorder, "SCAN ME", Color.black, 20,bottomBorder);

//         String imagePath = "img/scanMe.png";
//         BufferedImage logo = ImageIO.read(MethodUtils.class.getClassLoader().getResourceAsStream(imagePath));
//         //L'immagine del logo viene ridimensionata per adattarsi alle dimensioni del riquadro bianco.
//         logo = resizeImage(logo, whiteBoxSize, whiteBoxSize);
//         //Il logo viene sovrapposto al centro dell'immagine del codice QR
//         BufferedImage imageWithLogo =addLogoToCenter(imageWithBorder, logo,topBorder,bottomBorder,leftBorder,rightBorder);
//         //L'immagine con il logo viene scritta in un flusso di output in formato PNG.
//         ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
//         ImageIO.write(imageWithLogo, "PNG", pngOutputStream);
//         return pngOutputStream.toByteArray();
//     }