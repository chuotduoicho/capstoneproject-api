//package com.jovinn.capstoneproject.model;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.experimental.SuperBuilder;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.Type;
//
//import javax.persistence.*;
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Data
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@NoArgsConstructor
//@AllArgsConstructor
//@SuperBuilder
//@Table(schema = "jovinn_server")
//public class ServiceType extends BaseEntity {
//    @Id
//    @GeneratedValue(generator = "uuid2", strategy = GenerationType.AUTO)
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
//    @Type(type = "uuid-char")
//    UUID id;
//    String name;
//
////    @ManyToOne(fetch = FetchType.EAGER)
////    @JoinColumn(name = "sub_category_id", referencedColumnName = "id")
////    @JsonBackReference
////    SubCategory subCategory;
//
////    @OneToMany(mappedBy = "serviceType")
////    @JsonManagedReference
////    //@JsonIgnore
////    List<Box> boxes;
//}
