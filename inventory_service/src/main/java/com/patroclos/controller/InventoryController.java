package com.patroclos.controller;

import com.patroclos.common.dto.InventoryRequestDTO;
import com.patroclos.common.dto.InventoryResponseDTO;
import com.patroclos.inventory.dto.ItemDTO;
import com.patroclos.service.InventoryService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("stock")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping("/{itemId}")
    public ItemDTO getItem(@PathVariable final UUID itemId){
        return this.inventoryService.getItem(itemId);
    }

    @PostMapping("/deduct")
    public InventoryResponseDTO deduct(@RequestBody final InventoryRequestDTO requestDTO){
        return this.inventoryService.deductInventory(requestDTO);
    }

    @PostMapping("/add")
    public void add(@RequestBody final InventoryRequestDTO requestDTO){
        this.inventoryService.addInventory(requestDTO);
    }

}
