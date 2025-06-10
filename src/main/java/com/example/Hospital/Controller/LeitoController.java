package com.example.Hospital.Controller;

import com.example.Hospital.Dto.QuantidadeLeitoLivreDto;
import com.example.Hospital.Dto.StatusDto;
import com.example.Hospital.Projection.QuantidadeLeitoLivreProjection;
import com.example.Hospital.Service.LeitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leitos")
public class LeitoController {


    @Autowired
    private LeitoService leitoService;

    @GetMapping("/quantidades-livres")
    public ResponseEntity<List<QuantidadeLeitoLivreDto>> listarQuantidadeLeitosLivres() {
        return ResponseEntity.ok(this.leitoService.listarQuantidadeDeLeitosLivres());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarLeito(@PathVariable Long id, @RequestBody StatusDto statusDto){
        boolean atualizado = leitoService.atualizarStatus(id, statusDto.getStatus());
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
