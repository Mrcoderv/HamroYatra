package com.hamroyatra.repository;

import com.hamroyatra.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerNameContainingIgnoreCase(String customerName);
    List<Booking> findByEmail(String email);
    List<Booking> findByTourPackageId(Long tourPackageId);
}
