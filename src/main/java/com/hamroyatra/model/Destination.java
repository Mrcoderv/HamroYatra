package com.hamroyatra.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "destinations")
public class Destination {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    private String imageUrl;
    
    private String location;
    
    @Column(columnDefinition = "TEXT")
    private String highlights;
    
    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL)
    private Set<TourPackage> tourPackages = new HashSet<>();
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHighlights() {
        return highlights;
    }

    public void setHighlights(String highlights) {
        this.highlights = highlights;
    }

    public Set<TourPackage> getTourPackages() {
        return tourPackages;
    }

    public void setTourPackages(Set<TourPackage> tourPackages) {
        this.tourPackages = tourPackages;
    }
}
