package pe.com.isesystem.siscopepesca.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.*;
import pe.com.isesystem.siscopepesca.model.Formulario;
import pe.com.isesystem.siscopepesca.model.FormularioNew;
import pe.com.isesystem.siscopepesca.model.HttpRespuesta;

import java.util.Comparator;
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
    public ResponseEntity<List<Formulario>> getFormulario() {
        Query miQuery = new Query(where("idFormulario").gt(0));

        List<Formulario> documentos = mongoTemplate.find(
                miQuery,
                Formulario.class,
                "forms"
        );
        return new ResponseEntity<>(documentos, HttpStatus.OK);
    }

    @GetMapping("/getFormulariosMobile")
    public ResponseEntity<List<Formulario>> getFormularioMobile() {
        Query miQuery = new Query(where("estado").is(true));

        List<Formulario> documentos = mongoTemplate.find(
                miQuery,
                Formulario.class,
                "forms"
        );
        return new ResponseEntity<>(documentos, HttpStatus.OK);
    }

    @PostMapping("/saveDatosForm")
    public ResponseEntity<HttpRespuesta> saveDatosForm( @RequestBody Formulario formulario) throws JsonProcessingException {
        int valorRetorno = 0;

        mongoTemplate.save(formulario, "respuestas");

        return new ResponseEntity<HttpRespuesta>(new HttpRespuesta("OK", 1, valorRetorno), HttpStatus.OK);
    }

    @PostMapping("/saveFormulario")
    public ResponseEntity<HttpRespuesta> saveFormulario( @RequestBody Formulario formulario) throws JsonProcessingException {
        int valorRetorno = 0;

        if(formulario._id == null){
            //En este caso debo de buscar el nuevo IdFormulario
            valorRetorno = this.getMaxIdFormulario() + 1;
            FormularioNew fn = new FormularioNew();
            fn.setFormulario(formulario.getFormulario());
            fn.setIdFormulario(valorRetorno);
            fn.setNombreFormulario(formulario.nombreFormulario);
            fn.setMensaje(formulario.getMensaje());
            fn.setIcono(formulario.getIcono());
            fn.setEstado(formulario.isEstado());
            fn.setTitle(formulario.getTitle());
            mongoTemplate.save(fn, "forms");
        }else{
            mongoTemplate.save(formulario, "forms");
        }
        return new ResponseEntity<HttpRespuesta>(new HttpRespuesta("OK", 1, valorRetorno), HttpStatus.OK);
    }

    public int getMaxIdFormulario() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group().max("idFormulario").as("maxId")
        );

        AggregationResults<ResultMaxId> results = mongoTemplate.aggregate(aggregation, "forms", ResultMaxId.class);
        ResultMaxId resultMaxId = results.getUniqueMappedResult();
        return resultMaxId != null ? resultMaxId.getMaxId() : 0;
    }

    private static class ResultMaxId {
        private int maxId;

        public int getMaxId() {
            return maxId;
        }

        public void setMaxId(int maxId) {
            this.maxId = maxId;
        }
    }
}
