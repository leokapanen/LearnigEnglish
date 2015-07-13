package datamodel;

import java.io.Serializable;

/**
 * Created by Leonid Kabanen on 13.07.15.
 * Dictionary record item
 * {id : 234, sentence : “cloud”, translation : “облако”}
 */
public class DicRecord implements Serializable {

    private int id;
    private String sentence;
    private String translation;
    private boolean studied;

    private boolean selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isStudied() {
        return studied;
    }

    public void setStudied(boolean studied) {
        this.studied = studied;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
