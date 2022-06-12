package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BoxController {
    @Autowired
    private BoxService boxService;

    @GetMapping("/a")
    public String test(){
        return "done";
    }

    //API add Service
    @PostMapping("/addService")
    public Box addService(@RequestBody Box box){
        return boxService.saveBox(box);
    }

    //API update Service
    @PutMapping("/updateService")
    public Box updateService(@RequestParam("id")UUID id,@RequestBody Box box){
        return boxService.updateBox(box,id);
    }

    //Api delete Service
    @DeleteMapping("/deleteService/{id}")
    public boolean deleteBox(@PathVariable UUID id){
        return boxService.deleteBox(id);
    }

    //Api get Service By Seller Id
    @GetMapping("/listServiceBySellerId/{sellerId}")
    public List<Box> getBoxServiceBySellerId(@PathVariable UUID sellerId){
        return boxService.getListServiceBySellerId(sellerId);
    }

    //Api get all Service
    @GetMapping("/listAllService")
    public List<Box> getAllService(){
        return boxService.getAllService();
    }

    //Api view detail Service
    @GetMapping("/serviceDetail/{id}")
    public Box getServiceById(@PathVariable UUID id){
        return boxService.getServiceByID(id);
    }
}
