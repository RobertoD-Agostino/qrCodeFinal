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
