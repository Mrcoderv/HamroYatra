package com.hamroyatra.controller;

import com.hamroyatra.model.Destination;
import com.hamroyatra.model.TourPackage;
import com.hamroyatra.service.DestinationService;
import com.hamroyatra.service.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    
    @Autowired
    public HomeController(DestinationService destinationService, TourPackageService tourPackageService) {
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
    }
    
    @GetMapping("/")
    public String home(Model model) {
        // Get popular destinations (limit to 3)
        List<Destination> allDestinations = destinationService.getAllDestinations();
        List<Destination> popularDestinations = allDestinations.stream()
                .limit(3)
                .collect(Collectors.toList());
        
        // Get featured packages (limit to 4)
        List<TourPackage> allPackages = tourPackageService.getAllTourPackages();
        List<TourPackage> featuredPackages = allPackages.stream()
                .limit(4)
                .collect(Collectors.toList());
        
        model.addAttribute("popularDestinations", popularDestinations);
        model.addAttribute("featuredPackages", featuredPackages);
        model.addAttribute("pageTitle", "HamroYatra - Tourist Management");
        model.addAttribute("activeTab", "home");
        
        return "index";
    }
    
    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About Us - HamroYatra");
        model.addAttribute("activeTab", "about");
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Contact Us - HamroYatra");
        model.addAttribute("activeTab", "contact");
        return "contact";
    }
    
    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("pageTitle", "Photo Gallery - HamroYatra");
        model.addAttribute("activeTab", "gallery");
        return "gallery";
    }
    
    @GetMapping("/services")
    public String services(Model model) {
        model.addAttribute("pageTitle", "Our Services - HamroYatra");
        model.addAttribute("activeTab", "services");
        return "services";
    }
    
    @GetMapping("/faq")
    public String faq(Model model) {
        model.addAttribute("pageTitle", "Frequently Asked Questions - HamroYatra");
        model.addAttribute("activeTab", "faq");
        return "faq";
    }
    
    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("pageTitle", "Terms and Conditions - HamroYatra");
        return "terms";
    }
    
    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("pageTitle", "Privacy Policy - HamroYatra");
        return "privacy";
    }
}
