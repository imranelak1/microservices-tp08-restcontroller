package ma.rest.spring.web;
import org.springframework.web.bind.annotation.*;import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;import java.util.Map;
/** Une entrée invalide reçoit un message clair et un statut 400. */
@RestControllerAdvice
public class ApiErrors {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> invalid(MethodArgumentNotValidException error){return ResponseEntity.badRequest().body(Map.of("erreur","Solde positif, date et type obligatoires"));}
}
