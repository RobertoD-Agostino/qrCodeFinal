package demo.demoqrcode.Utils;

import java.awt.Color;
import java.util.Objects;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestData {
    
    private String requestUrl;
    private int qrWidth;
    private int qrHeight;
    private String qrCodeColor;
    private String backgroundColor;
    private String borderColor;
    private String textBorder;
    private int topBorderSize;
    private int bottomBorderSize;
    private int leftBorderSize;
    private int rightBorderSize;
    private String logoCenterUrl;
    private String logoBorderUrl;

    
    public Color getQrCodeColorAsColor() {
        return Color.decode(qrCodeColor);
    }

    public Color getBackgroundColorAsColor() {
        return Color.decode(backgroundColor);
    }

    public Color getBorderColorAsColor() {
        return Color.decode(borderColor);
    }


       // Verifica se ci sono altri parametri oltre all'URL
       public boolean hasAdditionalParameters() {
        return qrWidth != 0 ||
               qrHeight != 0 ||
               qrCodeColor != null ||
               backgroundColor != null ||
               borderColor != null ||
               textBorder != null ||
               topBorderSize != 0 ||
               bottomBorderSize != 0 ||
               leftBorderSize != 0 ||
               rightBorderSize != 0 ||
               logoCenterUrl != null ||
               logoBorderUrl != null;
    }

        public boolean hasOnlyUrl() {
        return Stream.of(qrWidth, qrHeight, qrCodeColor, backgroundColor, borderColor, textBorder,
                topBorderSize, bottomBorderSize, leftBorderSize, rightBorderSize, logoCenterUrl, logoBorderUrl)
                .allMatch(Objects::isNull);
    }
}
