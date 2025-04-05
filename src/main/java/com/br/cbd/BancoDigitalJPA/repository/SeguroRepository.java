package com.br.cbd.BancoDigitalJPA.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.br.cbd.BancoDigitalJPA.model.entity.seguro.Seguro;

public interface SeguroRepository extends JpaRepository<Seguro,Long>{

    boolean existsByNumeroAPolice(String numeroAPolice);


}
