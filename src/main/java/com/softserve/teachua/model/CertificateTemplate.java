package com.softserve.teachua.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "certificate_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
public class CertificateTemplate implements Convertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_type", referencedColumnName = "id")
    private CertificateType certificateType;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @Column(name = "course_description")
    private String courseDescription;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "picture_path")
    private String picturePath;

    @Column(name = "properties")
    private String properties;

}
