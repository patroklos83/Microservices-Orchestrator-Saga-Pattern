package com.patroclos.service;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.patroclos.common.dto.InventoryRequestDTO;
import com.patroclos.common.dto.InventoryResponseDTO;
import com.patroclos.common.enums.InventoryStatus;
import com.patroclos.configuration.GlobalModelMapper;
import com.patroclos.entity.Item;
import com.patroclos.inventory.dto.ItemDTO;
import com.patroclos.repository.ItemRepository;

import java.util.UUID;

@Service
public class InventoryService {

	private static org.slf4j.Logger Logger = LoggerFactory.getLogger(InventoryService.class);

	@Autowired
	private ItemRepository itemRepository;

	private ModelMapper modelMapper = GlobalModelMapper.getModelMapper();

	public InventoryResponseDTO deductInventory(final InventoryRequestDTO requestDTO){
		Item item = itemRepository.findById(requestDTO.getItemId()).block();

		if (item == null)
			throw new RuntimeException("Item not found");

		InventoryResponseDTO responseDTO = new InventoryResponseDTO();
		responseDTO.setOrderId(requestDTO.getOrderId());
		responseDTO.setCustomerId(requestDTO.getCustomerId());
		responseDTO.setItemId(requestDTO.getItemId());
		
		if(item.getStockAvailable() > 0){
			responseDTO.setStatus(InventoryStatus.INSTOCK);
			item.setStockAvailable(item.getStockAvailable() - 1);	
			itemRepository.save(item).block();
			Logger.info("Item with id " + requestDTO.getItemId() + " deducted from stock");
		}
		else
		{
			responseDTO.setStatus(InventoryStatus.OUTOFSTOCK);
			Logger.info("Item with id " + requestDTO.getItemId() + " is out of stock");
		}

		return responseDTO;
	}

	public void addInventory(final InventoryRequestDTO requestDTO){
		Item item = itemRepository.findById(requestDTO.getItemId()).block();
		if (item == null)
			throw new RuntimeException("Item not found");

		item.setStockAvailable(item.getStockAvailable() + 1);	
		itemRepository.save(item).block();
		
		Logger.info("Stock was updated as +1 availability for item with id " +  item.getItemId());
	}

	public ItemDTO getItem(UUID itemId){
		return getItemDTO(itemRepository.findById(itemId).block());
	}

	private ItemDTO getItemDTO(Item i) {
		return modelMapper.map(i, ItemDTO.class);
	}

}
