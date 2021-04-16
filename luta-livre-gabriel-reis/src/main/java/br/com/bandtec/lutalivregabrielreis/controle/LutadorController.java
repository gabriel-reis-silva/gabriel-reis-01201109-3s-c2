package br.com.bandtec.lutalivregabrielreis.controle;

import br.com.bandtec.lutalivregabrielreis.dominio.Golpeador;
import br.com.bandtec.lutalivregabrielreis.dominio.Lutador;
import br.com.bandtec.lutalivregabrielreis.repositorio.LutadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lutadores")
public class LutadorController {

    @Autowired
    public LutadorRepository repository;
    int contador = 0;
    List<Lutador> lutador;


    @PostMapping
    public ResponseEntity postaLutador(@RequestBody Lutador novoLutador) {
        repository.save(novoLutador);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity getLutadores() {
        lutador = repository.findOrderByForca();
        if (lutador.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(lutador);
    }

    @GetMapping("/contagem-vivos")
    public ResponseEntity countVivos() {
        return ResponseEntity.status(200).body(repository.countByVivo(true));
    }

    @PostMapping("/{id}/concentrar")
    public ResponseEntity concentraLutador(@PathVariable Integer id) {
        Optional<Lutador> lutador = repository.findById(id);

        if (lutador.isPresent()) {
            System.out.println(contador);
            if (lutador.get().getConcentracoesRealizadas() >= 3) {
                return ResponseEntity.status(400).body("O lutador já se concentrou 3 vezes!");
            } else {
                contador++;
                System.out.println(contador);
                lutador.get().setConcentracoesRealizadas(contador);
                lutador.get().setVida(lutador.get().getVida() * 1.15);
                repository.save(lutador.get());
                return ResponseEntity.status(200).build();
            }
        } else {
            return ResponseEntity.status(404).body("Lutador não encontrado!");
        }
    }

    @PostMapping("/golpe")
    public ResponseEntity golpeia(@RequestBody Golpeador novoGolpe){
        Golpeador golpe = novoGolpe;
        Lutador lutadorBate = lutador.get(golpe.getIdLutadorBate());
        Lutador lutadorApanha = lutador.get(golpe.getIdLutadorApanha());
        lutadorApanha.setVida(lutadorApanha.getVida()-lutadorBate.getForcaGolpe());
        repository.save(lutadorApanha);
    return ResponseEntity.status(200).body(repository.findAll());
    }


    @GetMapping("/mortos")
    public ResponseEntity getMortos() {
        if (repository.countByVivo(false) > 0) {
            return ResponseEntity.status(200).body(repository.findByVivo(false));
        } else {
            return ResponseEntity.status(204).build();
        }
    }

}
