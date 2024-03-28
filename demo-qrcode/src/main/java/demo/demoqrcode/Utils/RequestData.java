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
    private String topOrBottom;
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

}
