package com.project.forum.enity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.forum.enums.StatusAds;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    int views;

    String status;

    @CreatedDate()
    @Column(updatable = false)
    LocalDateTime created_at;

    @ManyToOne
    @JoinColumn(name = "post_id")
    Posts posts;

    @ManyToOne
    @JoinColumn(name = "ads_package_id")
    AdsPackage adsPackage;

}
