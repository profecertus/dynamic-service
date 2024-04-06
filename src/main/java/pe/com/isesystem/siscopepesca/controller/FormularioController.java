package pe.com.isesystem.siscopepesca.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.isesystem.siscopepesca.model.Formulario;
import pe.com.isesystem.siscopepesca.model.FormularioNew;
import pe.com.isesystem.siscopepesca.model.HttpRespuesta;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/getRespuestas")
    public ResponseEntity<List<Response>> getRespuestas(@RequestParam(value = "estado", required = false) Boolean estado) {
        Query miQuery;
        if (estado != null) {
            miQuery = new Query(where("estado").is(estado));
        } else {
            miQuery = new Query();
        }

        miQuery.fields().include( "_id").include("usuario").
                include("nombreFormulario").include("latitud").
                include("longitud").include("altitud").include("fecha").include("estado");

        List<Response> documentos = mongoTemplate.find(
                miQuery,
                Response.class,
                "respuestas"
        );

        //if some field is null then set string empty
        for (Response response : documentos) {
            if (response.usuario == null) {
                response.usuario = "ND";
            }
            if (response.nombreFormulario == null) {
                response.nombreFormulario = "ND";
            }
            if (response.latitud == null) {
                response.latitud = "ND";
            }
            if (response.longitud == null) {
                response.longitud = "ND";
            }
            if (response.altitud == null) {
                response.altitud = "ND";
            }
            if (response.fecha == null) {
                response.fecha = "ND";
            }
            if (response.estado == null) {
                response.estado = false;
            }
        }
        return new ResponseEntity<>(documentos, HttpStatus.OK);
    }

    class Response{
        public String _id;
        public String usuario;
        public String nombreFormulario;
        public String latitud;
        public String longitud;
        public String altitud;
        public String fecha;
        public Boolean estado;
    }


    @PostMapping("/saveDatosForm")
    public ResponseEntity<HttpRespuesta> saveDatosForm( @RequestBody Map<String, Object> formulario) throws JsonProcessingException {
        int valorRetorno = 0;

        formulario.put("fecha", new Date());
        formulario.put("estado", false);
        mongoTemplate.save(formulario, "respuestas");

        return new ResponseEntity<HttpRespuesta>(new HttpRespuesta("OK", 1, valorRetorno), HttpStatus.OK);
    }

    @PostMapping("/validaUsuario")
    public ResponseEntity<HttpRespuesta> validaUsuario( @RequestBody Map<String, Object> formulario) throws JsonProcessingException {
        /*
        int valorRetorno = 0;

        formulario.put("fecha", new Date());
        formulario.put("estado", false);
        mongoTemplate.save(formulario, "respuestas");
        */
        return new ResponseEntity<HttpRespuesta>(new HttpRespuesta("OK", 1, 1), HttpStatus.OK);
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

    @GetMapping("/formularioEnviado")
    public ResponseEntity<List<Object>>  formularioEnviado(@RequestParam(value = "id", required = true) String id) {
        Query miQuery;

        if (id != null) {
            miQuery = new Query(where("_id").is(id));
        } else {
            miQuery = new Query();
        }

        //Exclude some fields
        miQuery.fields().exclude( "_id").exclude("usuario").exclude("idFormulario").
                exclude("nombreFormulario").exclude("latitud").
                exclude("longitud").exclude("altitud").exclude("fecha").exclude("estado");

        List<Object> documentos = mongoTemplate.find(
                miQuery,
                Object.class,
                "respuestas"
        );


        return new ResponseEntity<>(documentos, HttpStatus.OK);
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

