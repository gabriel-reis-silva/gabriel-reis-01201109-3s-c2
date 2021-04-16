package br.com.bandtec.lutalivregabrielreis.repositorio;

import br.com.bandtec.lutalivregabrielreis.dominio.Lutador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LutadorRepository extends JpaRepository<Lutador, Integer> {

    List<Lutador> findByVivo(Boolean vivo);
    long countByVivo(Boolean vivo);

    @Query("select l from Lutador l ORDER BY l.forcaGolpe")
    List<Lutador> findOrderByForca();

}
