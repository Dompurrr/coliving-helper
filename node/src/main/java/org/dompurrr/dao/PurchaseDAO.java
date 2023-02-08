package org.dompurrr.dao;

import org.dompurrr.entities.Purchase;
import org.dompurrr.entities.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseDAO extends JpaRepository<Purchase, Long> {
    @EntityGraph(attributePaths = {"residents"})
    Purchase findByPurchaseId(Long id);

    @EntityGraph(attributePaths = {"buyer"})
    Purchase findPurchaseWithBuyerByPurchaseId(Long id);

    @EntityGraph(attributePaths = {"residents"})
    List<Purchase> findByRoom(Room room);

}
