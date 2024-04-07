package demo.demoqrcode.Utils;

import java.awt.Color;
import java.util.Objects;
import java.util.stream.Stream;

import Exceptions.ColorNotValidException;
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

    
    // public Color getQrCodeColorAsColor() {
    //     return Color.decode(qrCodeColor);
    // }

    // public Color getBackgroundColorAsColor() {
    //     return Color.decode(backgroundColor);
    // }

    // public Color getBorderColorAsColor() {
    //     return Color.decode(borderColor);
    // }
    public Color getBorderColorAsColor() {
        // Verifica se il codice del colore è nel formato corretto
        if (!borderColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new ColorNotValidException("Il codice del colore non è valido");
        }
        // Restituisce il colore decodificato
        return Color.decode(borderColor);
    }

    public Color getBackgroundColorAsColor() {
        // Verifica se il codice del colore è nel formato corretto
        if (!borderColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new ColorNotValidException("Il codice del colore non è valido");
        }
        // Restituisce il colore decodificato
        return Color.decode(backgroundColor);
    }

    public Color getQrCodeColorAsColor() {
        // Verifica se il codice del colore è nel formato corretto
        if (!borderColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
            throw new ColorNotValidException("Il codice del colore non è valido");
        }
        // Restituisce il colore decodificato
        return Color.decode(qrCodeColor);
    }
    

}
