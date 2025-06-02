package com.example.Hospital.Controller;

import com.example.Hospital.Projection.LeitoLivreProjection;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.Service.LeitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leitos")
public class LeitoController {

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    private LeitoService leitoService;

    @GetMapping("/livres")
    public ResponseEntity<List<LeitoLivreProjection>> listarLeitosLivres() {
        List<LeitoLivreProjection> leitosLivres = leitoRepository.contarLeitosLivresPorSpecialty();
        System.out.println("Leitos livres encontrados: " + leitosLivres.size());
        leitosLivres.forEach(l -> System.out.println(l.getSpecialty() + ": " + l.getQuantidadeLeitosLivres()));
        return ResponseEntity.ok(leitosLivres);
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarLeito(@PathVariable Long id, @RequestParam String status){
        boolean atualizado = leitoService.atualizarStatus(id, status);
        if(atualizado){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity<?>deletarLeito(@PathVariable Long id){
        boolean deletado = leitoService.deletarLeito(id);
        if(deletado){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
