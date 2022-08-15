//package com.jovinn.capstoneproject.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import lombok.experimental.SuperBuilder;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.Type;
//
//import javax.persistence.*;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@NoArgsConstructor
//@AllArgsConstructor
//@SuperBuilder
//@Table(schema = "jovinn_server")
//public class PackageOptional extends  BaseEntity {
//    @Id
//    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Type(type = "uuid-char")
//    UUID id;
//    String title;
//    Double optionPrice;
//    Integer extraTime;
//
////    @ManyToOne(fetch = FetchType.EAGER)
////    @JoinColumn(name = "package_id", referencedColumnName = "id")
////    Package pack;
//}
