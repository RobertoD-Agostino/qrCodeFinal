package demo.demoqrcode.Utils;

import java.awt.Color;

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
    private int topBorderSize;
    private int bottomBorderSize;
    private int leftBorderSize;
    private int rightBorderSize;
    // private String logoCenterUrl;
    // private String logoBorderUrl;


    // "logoCenterUrl" : "https://cdn-icons-png.flaticon.com/512/13915/13915094.png",
    // "logoBorderUrl" : "https://cdn-icons-png.flaticon.com/512/0/191.png"

                       // requestData.getLogoCenterUrl(),
                    // requestData.getLogoBorderUrl()

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
