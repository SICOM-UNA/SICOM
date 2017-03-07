package com.sicom.validator;

import com.sicom.entities.Login;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author William Víquez Quirós
 */
@FacesValidator("passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent componente, Object contrasena) throws ValidatorException {
        String contrasenaActual = ((Login) context.getExternalContext().getSessionMap().get("login")).getContrasena();
        
        if(!contrasena.toString().equals(contrasenaActual)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "La contraseña actual no es válida", null));
        }
    }
}