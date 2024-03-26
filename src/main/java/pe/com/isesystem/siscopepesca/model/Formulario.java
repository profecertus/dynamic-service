package pe.com.isesystem.siscopepesca.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Formulario {
    public Object _id;
    public int  idFormulario;
    public String nombreFormulario;
    public String icono;
    public String mensaje;
    public String title;
    public List<Forms> formulario;
}

