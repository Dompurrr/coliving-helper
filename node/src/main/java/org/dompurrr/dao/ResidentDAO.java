package org.dompurrr.dao;

import org.dompurrr.entities.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResidentDAO extends JpaRepository<Resident, Long> {
    Resident findByTgPageRef(Long pgRef);

    Resident findByResidentId(Long id);
}
