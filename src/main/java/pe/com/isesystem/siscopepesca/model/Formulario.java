package pe.com.isesystem.siscopepesca.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@Setter
@Getter
public class Formulario {
    public String _id;
    public int  idFormulario;
    public String nombreFormulario;
    public String icono;
    public String mensaje;
    public boolean estado;
    public String title;
    public List<Forms> formulario;
}

