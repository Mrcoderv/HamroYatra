package com.hamroyatra.controller;

import com.hamroyatra.model.Booking;
import com.hamroyatra.model.User;
import com.hamroyatra.service.BookingService;
import com.hamroyatra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class DashboardController {
    
    private static final Logger logger = Logger.getLogger(DashboardController.class.getName());
    
    private final UserService userService;
    private final BookingService bookingService;
    
    @Autowired
    public DashboardController(UserService userService, BookingService bookingService) {
        this.userService = userService;
        this.bookingService = bookingService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            
            Optional<User> userOpt = userService.getUserByUsername(username);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                model.addAttribute("user", user);
                
                // Get user's bookings - handle potential database schema issues
                List<Booking> bookings = Collections.emptyList();
                try {
                    bookings = bookingService.getBookingsByEmail(user.getEmail());
                } catch (InvalidDataAccessResourceUsageException e) {
                    logger.warning("Database schema issue detected: " + e.getMessage());
                    // Continue with empty bookings list
                }
                
                model.addAttribute("bookings", bookings != null ? bookings : Collections.emptyList());
                model.addAttribute("pageTitle", "Dashboard - HamroYatra");
                model.addAttribute("activeTab", "dashboard");
                
                // Check if user is admin
                if (user.hasRole("ADMIN")) {
                    // Still show the dashboard for admin users, don't redirect
                    model.addAttribute("isAdmin", true);
                }
                
                return "dashboard/index";
            } else {
                return "redirect:/login";
            }
        } catch (Exception e) {
            logger.severe("Error in dashboard: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/login";
        }
    }
    
    @GetMapping("/dashboard/profile")
    public String profile(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "My Profile - HamroYatra");
            model.addAttribute("activeTab", "dashboard");
            return "dashboard/profile";
        } else {
            return "redirect:/login";
        }
    }
    
    @GetMapping("/dashboard/bookings")
    public String bookings(Model model) {
        // Get current authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        Optional<User> userOpt = userService.getUserByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("user", user);
            
            // Get user's bookings - handle potential database schema issues
            List<Booking> bookings = Collections.emptyList();
            try {
                bookings = bookingService.getBookingsByEmail(user.getEmail());
            } catch (InvalidDataAccessResourceUsageException e) {
                logger.warning("Database schema issue detected: " + e.getMessage());
                // Continue with empty bookings list
            }
            
            model.addAttribute("bookings", bookings != null ? bookings : Collections.emptyList());
            model.addAttribute("pageTitle", "My Bookings - HamroYatra");
            model.addAttribute("activeTab", "dashboard");
            
            return "dashboard/bookings";
        } else {
            return "redirect:/login";
        }
    }
}
