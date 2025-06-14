package com.hamroyatra.controller;

import com.hamroyatra.model.Booking;
import com.hamroyatra.model.Destination;
import com.hamroyatra.model.TourPackage;
import com.hamroyatra.model.User;
import com.hamroyatra.service.BookingService;
import com.hamroyatra.service.DestinationService;
import com.hamroyatra.service.TourPackageService;
import com.hamroyatra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    private final UserService userService;
    private final DestinationService destinationService;
    private final TourPackageService tourPackageService;
    private final BookingService bookingService;

    @Autowired
    public AdminController(UserService userService,
                           DestinationService destinationService,
                           TourPackageService tourPackageService,
                           BookingService bookingService) {
        this.userService = userService;
        this.destinationService = destinationService;
        this.tourPackageService = tourPackageService;
        this.bookingService = bookingService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        try {
            // Get counts for dashboard
            long userCount = userService.getAllUsers().size();
            long destinationCount = destinationService.getAllDestinations().size();
            long packageCount = tourPackageService.getAllTourPackages().size();
            long bookingCount = bookingService.getAllBookings().size();

            model.addAttribute("userCount", userCount);
            model.addAttribute("destinationCount", destinationCount);
            model.addAttribute("packageCount", packageCount);
            model.addAttribute("bookingCount", bookingCount);
            model.addAttribute("pageTitle", "Admin Dashboard - HamroYatra");

            // Get recent bookings
            List<Booking> recentBookings = bookingService.getAllBookings();
            model.addAttribute("recentBookings", recentBookings);

            return "admin/index";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in admin dashboard", e);
            model.addAttribute("errorMessage", "An error occurred while loading the dashboard.");
            return "error/general";
        }
    }

    // Users management
    @GetMapping("/users")
    public String listUsers(Model model) {
        try {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("pageTitle", "Manage Users - HamroYatra");
            return "admin/users/list";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing users", e);
            model.addAttribute("errorMessage", "An error occurred while loading users.");
            return "error/general";
        }
    }

    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        try {
            Optional<User> userOpt = userService.getUserById(id);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                model.addAttribute("user", user);
                model.addAttribute("pageTitle", "View User - HamroYatra");
                return "admin/users/view";
            } else {
                model.addAttribute("errorMessage", "User not found with ID: " + id);
                return "redirect:/admin/users";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error viewing user with ID: " + id, e);
            model.addAttribute("errorMessage", "An error occurred while loading user details.");
            return "error/general";
        }
    }

    // Destinations management
    @GetMapping("/destinations")
    public String listDestinations(Model model) {
        try {
            logger.info("Fetching all destinations");
            List<Destination> destinations = destinationService.getAllDestinations();
            logger.info("Found " + destinations.size() + " destinations");

            model.addAttribute("destinations", destinations);
            model.addAttribute("pageTitle", "Manage Destinations - HamroYatra");
            return "admin/destinations/list";
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Database error while listing destinations", e);
            model.addAttribute("errorMessage", "A database error occurred while loading destinations.");
            model.addAttribute("destinations", new ArrayList<>());
            return "admin/destinations/list";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing destinations", e);
            model.addAttribute("errorMessage", "An error occurred while loading destinations.");
            return "error/general";
        }
    }

    @GetMapping("/destinations/new")
    public String newDestinationForm(Model model) {
        try {
            model.addAttribute("destination", new Destination());
            model.addAttribute("pageTitle", "Add Destination - HamroYatra");
            return "admin/destinations/form";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading new destination form", e);
            model.addAttribute("errorMessage", "An error occurred while loading the form.");
            return "error/general";
        }
    }

    @PostMapping("/destinations/save")
    public String saveDestination(@Valid @ModelAttribute("destination") Destination destination,
                                  BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("pageTitle", "Add Destination - HamroYatra");
                return "admin/destinations/form";
            }

            destinationService.saveDestination(destination);
            return "redirect:/admin/destinations";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving destination", e);
            model.addAttribute("errorMessage", "An error occurred while saving the destination.");
            model.addAttribute("pageTitle", "Add Destination - HamroYatra");
            return "admin/destinations/form";
        }
    }

    @GetMapping("/destinations/edit/{id}")
    public String editDestinationForm(@PathVariable Long id, Model model) {
        try {
            Optional<Destination> destinationOpt = destinationService.getDestinationById(id);

            if (destinationOpt.isPresent()) {
                model.addAttribute("destination", destinationOpt.get());
                model.addAttribute("pageTitle", "Edit Destination - HamroYatra");
                return "admin/destinations/form";
            } else {
                model.addAttribute("errorMessage", "Destination not found with ID: " + id);
                return "redirect:/admin/destinations";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing destination with ID: " + id, e);
            model.addAttribute("errorMessage", "An error occurred while loading destination details.");
            return "error/general";
        }
    }

    @GetMapping("/destinations/delete/{id}")
    public String deleteDestination(@PathVariable Long id, Model model) {
        try {
            destinationService.deleteDestination(id);
            return "redirect:/admin/destinations";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting destination with ID: " + id, e);
            model.addAttribute("errorMessage", "An error occurred while deleting the destination.");
            return "redirect:/admin/destinations";
        }
    }

    // Tour packages management
    @GetMapping("/packages")
    public String listPackages(Model model) {
        try {
            List<TourPackage> packages = tourPackageService.getAllTourPackages();
            model.addAttribute("packages", packages);
            model.addAttribute("pageTitle", "Manage Tour Packages - HamroYatra");
            return "admin/packages/list";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing packages", e);
            model.addAttribute("errorMessage", "An error occurred while loading tour packages.");
            return "error/general";
        }
    }

    @GetMapping("/packages/new")
    public String newPackageForm(Model model) {
        try {
            model.addAttribute("tourPackage", new TourPackage());
            model.addAttribute("destinations", destinationService.getAllDestinations());
            model.addAttribute("pageTitle", "Add Tour Package - HamroYatra");
            return "admin/packages/form";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading new package form", e);
            model.addAttribute("errorMessage", "An error occurred while loading the form.");
            return "error/general";
        }
    }

    @PostMapping("/packages/save")
    public String savePackage(@Valid @ModelAttribute("tourPackage") TourPackage tourPackage,
                              BindingResult result,
                              Model model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("destinations", destinationService.getAllDestinations());
                model.addAttribute("pageTitle", "Add Tour Package - HamroYatra");
                return "admin/packages/form";
            }

            tourPackageService.saveTourPackage(tourPackage);
            return "redirect:/admin/packages";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error saving tour package", e);
            model.addAttribute("errorMessage", "An error occurred while saving the tour package.");
            model.addAttribute("destinations", destinationService.getAllDestinations());
            model.addAttribute("pageTitle", "Add Tour Package - HamroYatra");
            return "admin/packages/form";
        }
    }

    @GetMapping("/packages/edit/{id}")
    public String editPackageForm(@PathVariable Long id, Model model) {
        try {
            Optional<TourPackage> packageOpt = tourPackageService.getTourPackageById(id);

            if (packageOpt.isPresent()) {
                model.addAttribute("tourPackage", packageOpt.get());
                model.addAttribute("destinations", destinationService.getAllDestinations());
                model.addAttribute("pageTitle", "Edit Tour Package - HamroYatra");
                return "admin/packages/form";
            } else {
                model.addAttribute("errorMessage", "Tour package not found with ID: " + id);
                return "redirect:/admin/packages";
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing tour package with ID: " + id, e);
            model.addAttribute("errorMessage", "An error occurred while loading tour package details.");
            return "error/general";
        }
    }

    @GetMapping("/packages/delete/{id}")
    public String deletePackage(@PathVariable Long id, Model model) {
        try {
            tourPackageService.deleteTourPackage(id);
            return "redirect:/admin/packages";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting tour package with ID: " + id, e);
            model.addAttribute("errorMessage", "An error occurred while deleting the tour package.");
            return "redirect:/admin/packages";
        }
    }

    // Bookings management
    @GetMapping("/bookings")
    public String listBookings(Model model) {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            model.addAttribute("bookings", bookings);
            model.addAttribute("pageTitle", "Manage Bookings - HamroYatra");
            return "admin/bookings/list";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error listing bookings", e);
            model.addAttribute("errorMessage", "An error occurred while loading bookings.");
            return "error/general";
        }
    }

    @GetMapping("/bookings/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        try {
            Optional<Booking> bookingOpt = bookingService.getBookingById(id);

            if (bookingOpt.isPresent()) {
                model.addAttribute("booking", bookingOpt.get());
                model.addAttribute("pageTitle", "View Booking - HamroYatra");
                model.addAttribute("bookingStatuses", Booking.BookingStatus.values());
                return "admin/bookings/view";
            } else {
                model.addAttribute("errorMessage", "Booking not found with ID: " + id);
                return "redirect:/admin/bookings";
            }
        } catch (DataAccessException e) {
            logger.log(Level.SEVERE, "Database error while viewing booking with ID: " + id, e);
            model.addAttribute("errorMessage", "A database error occurred while loading booking details.");
            return "redirect:/admin/bookings";
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error viewing booking with ID: " + id, e);
            model.addAttribute("errorMessage", "An error occurred while loading booking details.");
            return "error/general";
        }
    }

    @PostMapping("/bookings/{id}/status")
    public String updateBookingStatus(@PathVariable Long id,
                                      @RequestParam("status") Booking.BookingStatus status,
                                      Model model) {
        try {
            bookingService.updateBookingStatus(id, status);
            return "redirect:/admin/bookings/" + id;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating booking status for ID: " + id, e);
            model.addAttribute("errorMessage", "Error updating booking status. Please try again.");
            return "redirect:/admin/bookings/" + id;
        }
    }
}
