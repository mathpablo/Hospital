package com.example.Hospital.Service;

import com.example.Hospital.Dto.QuantidadeLeitoLivreDto;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Projection.QuantidadeLeitoLivreProjection;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.model.Leito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeitoService {

    @Autowired
    private LeitoRepository leitoRepository;

    public Optional<Leito> buscarLeitoDisponivelPorEspecialidade(Specialty specialty){
        return leitoRepository.findFirstByRoom_Ala_SpecialtyAndStatus(specialty, StatusLeito.LIVRE);
    }

    public boolean atualizarStatus(Long id, String statusStr){

        Optional<Leito> optionalLeito = leitoRepository.findById(id);
        if (optionalLeito.isPresent()){
            Leito leito = optionalLeito.get();
            try {
                StatusLeito status = StatusLeito.valueOf(statusStr.toUpperCase());
                leito.setStatus(status);
                leitoRepository.save(leito);
                return true;
            }catch (IllegalArgumentException e){
                return false;
            }
            }
        return false;
        }

        public boolean deletarLeito(Long id){
        Optional<Leito>optionalLeito = leitoRepository.findById(id);
        if (optionalLeito.isPresent()){
            leitoRepository.deleteById(id);
            return false;
        }
        return false;
        }

    public List<QuantidadeLeitoLivreDto> listarQuantidadeDeLeitosLivres() {
        List<QuantidadeLeitoLivreProjection> projections = leitoRepository.findLeitosLivresPorEspecialidade();
        List<QuantidadeLeitoLivreDto> quantidadeLeitoLivreDtos =  projections.stream().map(projection -> {
            QuantidadeLeitoLivreDto leitoLivreDto = new QuantidadeLeitoLivreDto();
            leitoLivreDto.setQuantidadeLeitoLivre(projection.getQuantidadeLeitosLivres());
            leitoLivreDto.setSpecialty(projection.getSpecialty());
            return  leitoLivreDto;
        }).collect(Collectors.toList());
        return quantidadeLeitoLivreDtos;
    }


}


