package com.hamroyatra.controller;

import com.hamroyatra.model.Booking;
import com.hamroyatra.model.TourPackage;
import com.hamroyatra.service.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/packages")
public class TourPackageController {
    
    private final TourPackageService tourPackageService;
    
    @Autowired
    public TourPackageController(TourPackageService tourPackageService) {
        this.tourPackageService = tourPackageService;
    }
    
    @GetMapping
    public String getAllPackages(Model model) {
        List<TourPackage> packages = tourPackageService.getAllTourPackages();
        model.addAttribute("packages", packages);
        model.addAttribute("pageTitle", "Tour Packages - HamroYatra");
        model.addAttribute("activeTab", "packages");
        return "packages/list";
    }
    
    @GetMapping("/{id}")
    public String getPackageDetails(@PathVariable Long id, Model model) {
        Optional<TourPackage> packageOpt = tourPackageService.getTourPackageById(id);
        
        if (packageOpt.isPresent()) {
            TourPackage tourPackage = packageOpt.get();
            model.addAttribute("package", tourPackage);
            model.addAttribute("booking", new Booking());
            model.addAttribute("pageTitle", tourPackage.getName() + " - HamroYatra");
            model.addAttribute("activeTab", "packages");
            
            return "packages/details";
        } else {
            return "redirect:/packages";
        }
    }
}
