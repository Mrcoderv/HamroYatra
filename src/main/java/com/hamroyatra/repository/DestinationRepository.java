package com.hamroyatra.repository;

import com.hamroyatra.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    // Custom query methods if needed
}
