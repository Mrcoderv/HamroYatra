package com.hamroyatra.service;

import com.hamroyatra.model.Destination;
import com.hamroyatra.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DestinationService {

    private static final Logger logger = Logger.getLogger(DestinationService.class.getName());

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getAllDestinations() {
        try {
            logger.info("Fetching all destinations from repository");
            return destinationRepository.findAll();
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Database error while fetching all destinations", e);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while fetching all destinations", e);
            return new ArrayList<>();
        }
    }

    public Optional<Destination> getDestinationById(Long id) {
        try {
            logger.info("Fetching destination with ID: " + id);
            return destinationRepository.findById(id);
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Database error while fetching destination with ID: " + id, e);
            return Optional.empty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error while fetching destination with ID: " + id, e);
            return Optional.empty();
        }
    }

    public Destination saveDestination(Destination destination) {
        try {
            logger.info("Saving destination: " + destination.getName());
            return destinationRepository.save(destination);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving destination: " + destination.getName(), e);
            throw e;
        }
    }

    public void deleteDestination(Long id) {
        try {
            logger.info("Deleting destination with ID: " + id);
            destinationRepository.deleteById(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting destination with ID: " + id, e);
            throw e;
        }
    }
}
