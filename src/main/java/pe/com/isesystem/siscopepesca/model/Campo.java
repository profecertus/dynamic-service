package pe.com.isesystem.siscopepesca.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Campo {
    public String label;
    public String fieldType;
    public int decimales;
    public int entero;
    public int maxLetras;
    public int limit;
    public List<Opcion> options;
}
