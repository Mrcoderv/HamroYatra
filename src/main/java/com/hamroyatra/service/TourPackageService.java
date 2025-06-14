package com.hamroyatra.service;

import com.hamroyatra.model.TourPackage;
import com.hamroyatra.repository.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourPackageService {
    
    private final TourPackageRepository tourPackageRepository;
    
    @Autowired
    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }
    
    public List<TourPackage> getAllTourPackages() {
        return tourPackageRepository.findAll();
    }
    
    public Optional<TourPackage> getTourPackageById(Long id) {
        return tourPackageRepository.findById(id);
    }
    
    public List<TourPackage> getTourPackagesByDestinationId(Long destinationId) {
        return tourPackageRepository.findByDestinationId(destinationId);
    }
    
    public TourPackage saveTourPackage(TourPackage tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }
    
    public void deleteTourPackage(Long id) {
        tourPackageRepository.deleteById(id);
    }
}
