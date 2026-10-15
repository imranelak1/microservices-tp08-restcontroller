package ma.rest.spring;
import ma.rest.spring.entities.*;import ma.rest.spring.repositories.*;
import org.springframework.boot.*;import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;import java.sql.Date;
/** Démarrage du serveur et jeu de trois comptes de démonstration. */
@SpringBootApplication
public class MsBanqueApplication {
    // Les dates du TP ne doivent pas changer de jour entre JDBC et JSON.
    static { java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("UTC")); }
    public static void main(String[] args){SpringApplication.run(MsBanqueApplication.class,args);}
    @Bean CommandLineRunner start(CompteRepository comptes) {
        return args->{
            if(comptes.count()!=0)return;
            
            comptes.save(new Compte(null,5000,Date.valueOf("2024-10-30"),TypeCompte.EPARGNE));
            comptes.save(new Compte(null,2500,Date.valueOf("2024-10-31"),TypeCompte.COURANT));
            comptes.save(new Compte(null,7200,Date.valueOf("2024-11-01"),TypeCompte.EPARGNE));
            comptes.findAll().forEach(System.out::println);
        };
    }
}
