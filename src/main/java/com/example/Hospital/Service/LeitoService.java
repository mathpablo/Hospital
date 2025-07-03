package com.example.Hospital.Service;

import com.example.Hospital.Dto.QuantidadeLeitoLivreDto;
import com.example.Hospital.Enum.Specialty;
import com.example.Hospital.Enum.StatusLeito;
import com.example.Hospital.Projection.QuantidadeLeitoLivreProjection;
import com.example.Hospital.Repository.LeitoRepository;
import com.example.Hospital.Repository.RoomRepository;
import com.example.Hospital.model.Leito;
import com.example.Hospital.model.Room;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeitoService {

    @Autowired
    private LeitoRepository leitoRepository;

    @Autowired
    RoomRepository roomRepository;

    @Transactional
    public Optional<Leito> buscarLeitoDisponivelPorEspecialidade(Specialty specialty){
        return leitoRepository.findFirstByRoom_Ala_SpecialtyAndStatus(specialty, StatusLeito.LIVRE);
    }

    @Transactional
    public Leito atualizarStatus(Long id, String statusStr){
        Leito leito = leitoRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leito não encontrado com o Id:" + id));
        try {
            StatusLeito status = StatusLeito.valueOf(statusStr.toUpperCase());
            leito.setStatus(status);
            return leitoRepository.save(leito);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + statusStr);
        }
    }

    @Transactional
    public boolean deletarLeito(Long id) {
        Leito leito = leitoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leito não encontrado com id: " + id));

        if (leito.getPatient() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é possível deletar leito ocupado por paciente internado.");
        }

        leitoRepository.delete(leito);
        return true;
    }

    @Transactional
    public List<QuantidadeLeitoLivreDto> listarQuantidadeDeLeitosLivres() {
        List<QuantidadeLeitoLivreProjection> projections = leitoRepository.findLeitosLivresPorEspecialidade();
        List<QuantidadeLeitoLivreDto> quantidadeLeitoLivreDtos =  projections.stream().map(projection -> {
            QuantidadeLeitoLivreDto leitoLivreDto = new QuantidadeLeitoLivreDto();
            leitoLivreDto.setQuantidadeLeitoLivres(projection.getQuantidadeLeitosLivres());
            leitoLivreDto.setSpecialty(projection.getSpecialty());
            return  leitoLivreDto;
        }).collect(Collectors.toList());
        return quantidadeLeitoLivreDtos;
    }
}


