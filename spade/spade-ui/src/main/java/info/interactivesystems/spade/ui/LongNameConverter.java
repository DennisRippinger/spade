package info.interactivesystems.spade.ui;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = String.class, value = "converter.longName")
public class LongNameConverter implements Converter, Serializable {

    private static final long serialVersionUID = 2479669304984009829L;

    private static final int MAXIMUM = 28;

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String productName = (String) value;

        if (productName.length() > MAXIMUM) {
            productName = productName.substring(0, MAXIMUM);
            productName += " ...";
        }

        return productName;

    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        // Direction not needed
        return null;
    }
}