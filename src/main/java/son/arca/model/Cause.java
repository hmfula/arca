package son.arca.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Model class
 * @author Harrison Mfula
 * @since 9.2.2016.
 */
@Entity
public class Cause {
    @Id
    @GeneratedValue
    private Long id;
    private  String text;

    public Long getFrequency() {
        return frequency;
    }

    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }

    private Long frequency;


    public Cause(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
