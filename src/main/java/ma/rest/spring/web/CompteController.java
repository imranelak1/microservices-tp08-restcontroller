package ma.rest.spring.web;

import ma.rest.spring.entities.Compte;
import ma.rest.spring.repositories.CompteRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.validation.Valid;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;

/** CRUD Spring MVC ; /api est conservé comme alias pour les clients du cours. */
@RestController
@RequestMapping({"/banque", "/api"})
@CrossOrigin(origins = "*")
public class CompteController {
  private final CompteRepository comptes;

  public CompteController(CompteRepository comptes) {
    this.comptes = comptes;
  }

  @Operation(summary = "Lister les comptes")
  @GetMapping(
      value = "/comptes",
      produces = {"application/json", "application/xml"})
  public List<Compte> getAllComptes() {
    return comptes.findAll();
  }

  @Operation(summary = "Consulter un compte")
  @GetMapping(
      value = "/comptes/{id}",
      produces = {"application/json", "application/xml"})
  public ResponseEntity<Compte> getCompteById(@PathVariable Long id) {
    return comptes
        .findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Créer un compte")
  @PostMapping(
      value = "/comptes",
      consumes = {"application/json", "application/xml"},
      produces = {"application/json", "application/xml"})
  public ResponseEntity<Compte> createCompte(@Valid @RequestBody Compte compte) {
    compte.setId(null);
    Compte saved = comptes.save(compte);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri())
        .body(saved);
  }

  @Operation(summary = "Modifier un compte existant")
  @PutMapping(
      value = "/comptes/{id}",
      consumes = {"application/json", "application/xml"},
      produces = {"application/json", "application/xml"})
  public ResponseEntity<Compte> updateCompte(
      @PathVariable Long id, @Valid @RequestBody Compte details) {
    return comptes
        .findById(id)
        .map(
            existing -> {
              existing.setSolde(details.getSolde());
              existing.setDateCreation(details.getDateCreation());
              existing.setType(details.getType());
              return ResponseEntity.ok(comptes.save(existing));
            })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Supprimer un compte")
  @DeleteMapping("/comptes/{id}")
  public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
    return comptes
        .findById(id)
        .map(
            existing -> {
              comptes.delete(existing);
              return ResponseEntity.ok().<Void>build();
            })
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
