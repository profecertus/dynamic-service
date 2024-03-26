package pe.com.isesystem.siscopepesca.controller;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.*;
import pe.com.isesystem.siscopepesca.model.Formulario;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
@RequestMapping("/formulario/v1")
public class FormularioController {

    private final MongoTemplate mongoTemplate;
    private static final Logger logger = LogManager.getLogger(FormularioController.class);

    public FormularioController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/getFormularios")
    public ResponseEntity<List<Formulario>> getGastos() {
        Query miQuery = new Query(where("idFormulario").gt(0));

        List<Formulario> documentos = mongoTemplate.find(
                miQuery,
                Formulario.class,
                "forms"
        );
        return new ResponseEntity<>(documentos, HttpStatus.OK);
    }
}
