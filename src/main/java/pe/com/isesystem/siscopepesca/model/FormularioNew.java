package pe.com.isesystem.siscopepesca.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

import java.util.List;

@Setter
@Getter
public class FormularioNew {
    public int  idFormulario;
    public String nombreFormulario;
    public String icono;
    public String mensaje;
    public boolean estado;
    public String title;
    public List<Forms> formulario;
}

