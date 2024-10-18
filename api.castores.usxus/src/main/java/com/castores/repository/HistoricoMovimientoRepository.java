package com.castores.repository;

import com.castores.entities.HistoricoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoMovimientoRepository extends JpaRepository<HistoricoMovimiento, Integer> {
    
}
