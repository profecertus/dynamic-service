package pe.com.isesystem.siscopepesca.model;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Forms {
    public String title;
    public List<Campo> fields;

}
