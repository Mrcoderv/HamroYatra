package com.hamroyatra.controller;

import com.hamroyatra.model.Booking;
import com.hamroyatra.model.TourPackage;
import com.hamroyatra.model.User;
import com.hamroyatra.service.BookingService;
import com.hamroyatra.service.TourPackageService;
import com.hamroyatra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    
    private final BookingService bookingService;
    private final UserService userService;
    private final TourPackageService tourPackageService;
    
    @Autowired
    public BookingController(BookingService bookingService, UserService userService, TourPackageService tourPackageService) {
        this.bookingService = bookingService;
        this.userService = userService;
        this.tourPackageService = tourPackageService;
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    setValue(LocalDate.parse(text, DateTimeFormatter.ISO_DATE));
                }
            }
            
            @Override
            public String getAsText() {
                LocalDate value = (LocalDate) getValue();
                return (value != null ? value.format(DateTimeFormatter.ISO_DATE) : "");
            }
        });
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam(required = false) Long tourPackageId) {
        Booking booking = new Booking();
        
        // If tourPackageId is provided, fetch the package details
        if (tourPackageId != null) {
            Optional<TourPackage> packageOpt = tourPackageService.getTourPackageById(tourPackageId);
            if (packageOpt.isPresent()) {
                TourPackage tourPackage = packageOpt.get();
                model.addAttribute("tourPackage", tourPackage);
                booking.setTourPackage(tourPackage);
            }
        } else {
            // If no package ID is provided, get all packages for selection
            model.addAttribute("allPackages", tourPackageService.getAllTourPackages());
        }
        
        // Set default tour start date to tomorrow
        booking.setTourStartDate(LocalDate.now().plusDays(1));
        
        // Pre-fill user information if authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String username = auth.getName();
            Optional<User> userOpt = userService.getUserByUsername(username);
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                booking.setCustomerName(user.getFullName());
                booking.setEmail(user.getEmail());
                booking.setPhone(user.getPhone());
                model.addAttribute("user", user);
            }
        }
        
        model.addAttribute("booking", booking);
        model.addAttribute("pageTitle", "Create Booking - HamroYatra");
        return "bookings/create";
    }
    
    @PostMapping("/create")
    public String createBooking(
            @Valid @ModelAttribute("booking") Booking booking,
            BindingResult result,
            @RequestParam("tourPackageId") Long tourPackageId,
            Model model) {
        
        if (result.hasErrors()) {
            Optional<TourPackage> packageOpt = tourPackageService.getTourPackageById(tourPackageId);
            if (packageOpt.isPresent()) {
                model.addAttribute("tourPackage", packageOpt.get());
            }
            model.addAttribute("pageTitle", "Create Booking - HamroYatra");
            return "bookings/create";
        }
        
        try {
            // If user is authenticated, pre-fill booking info
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
                String username = auth.getName();
                Optional<User> userOpt = userService.getUserByUsername(username);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    if (booking.getCustomerName() == null || booking.getCustomerName().isEmpty()) {
                        booking.setCustomerName(user.getFullName());
                    }
                    if (booking.getEmail() == null || booking.getEmail().isEmpty()) {
                        booking.setEmail(user.getEmail());
                    }
                    if (booking.getPhone() == null || booking.getPhone().isEmpty() && user.getPhone() != null) {
                        booking.setPhone(user.getPhone());
                    }
                }
            }
            
            Booking savedBooking = bookingService.createBooking(booking, tourPackageId);
            return "redirect:/bookings/confirmation/" + savedBooking.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Optional<TourPackage> packageOpt = tourPackageService.getTourPackageById(tourPackageId);
            if (packageOpt.isPresent()) {
                model.addAttribute("tourPackage", packageOpt.get());
            }
            model.addAttribute("pageTitle", "Create Booking - HamroYatra");
            return "bookings/create";
        }
    }
    
    @GetMapping("/confirmation/{id}")
    public String bookingConfirmation(@PathVariable Long id, Model model) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isPresent()) {
            model.addAttribute("booking", bookingOpt.get());
            model.addAttribute("pageTitle", "Booking Confirmation - HamroYatra");
            return "bookings/confirmation";
        } else {
            return "redirect:/dashboard";
        }
    }
}
