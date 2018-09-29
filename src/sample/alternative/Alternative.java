package sample.alternative;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alternative {
    private StringProperty idAlt;
    private StringProperty textAlt;
    private String text;

    public Alternative(){}

    public Alternative(String textAlt) {
        this.text = textAlt;
    }

    public Alternative(String idAlt, String textAlt) {
        this.idAlt = new SimpleStringProperty(idAlt);
        this.textAlt = new SimpleStringProperty(textAlt);
    }

    public String getIdAlt() {
        return idAlt.get();
    }

    public String getText() {
        return text;
    }

    public StringProperty idAltProperty() {
        return idAlt;
    }

    public void setIdAlt(String idAlt) {
        this.idAlt.set(idAlt);
    }

    public String getTextAlt() {
        return textAlt.get();
    }

    public StringProperty textAltProperty() {
        return textAlt;
    }

    public void setTextAlt(String textAlt) {
        this.textAlt.set(textAlt);
    }
}
