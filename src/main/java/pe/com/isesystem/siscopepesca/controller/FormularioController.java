package pe.com.isesystem.siscopepesca.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @PostMapping("/saveFormulario")
    public ResponseEntity<String> saveFormulario( @RequestBody Object formulario) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String descargaJson = objectMapper.writeValueAsString(formulario);
        JsonNode jsonNode = objectMapper.readTree(descargaJson);
        //String numTicket = jsonNode.get("numTicket").toString();
        mongoTemplate.save(formulario, "forms");
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}
