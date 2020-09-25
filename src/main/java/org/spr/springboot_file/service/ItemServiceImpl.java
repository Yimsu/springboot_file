package org.spr.springboot_file.service;

import java.util.ArrayList;
import java.util.List;

import org.spr.springboot_file.domain.Item;
import org.spr.springboot_file.domain.ItemFile;
import org.spr.springboot_file.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository repository;

	@Override
	public void regist(Item item) throws Exception {
		Item itemEntity = new Item();

		itemEntity.setItemName(item.getItemName());
		itemEntity.setPrice(item.getPrice());
		itemEntity.setDescription(item.getDescription());

		String[] files = item.getFiles();

		if (files == null) {
			return;
		}

		for (String fileName : files) {
			ItemFile itemFile = new ItemFile();
			itemFile.setFullName(fileName);

			itemEntity.addItemFile(itemFile);
		}

		repository.save(itemEntity);
	}

	@Override
	public Item read(Long itemId) throws Exception {
		return repository.getOne(itemId);
	}

	@Override
	public void modify(Item item) throws Exception {
		Item itemEntity = repository.getOne(item.getItemId());

		itemEntity.setItemName(item.getItemName());
		itemEntity.setPrice(item.getPrice());
		itemEntity.setDescription(item.getDescription());

		String[] files = item.getFiles();

		if (files != null) {
			itemEntity.clearItemFile();

			for (String fileName : files) {
				ItemFile itemFile = new ItemFile();
				itemFile.setFullName(fileName);

				itemEntity.addItemFile(itemFile);
			}
		}
		repository.save(itemEntity);
	}

	@Override
	public void remove(Long itemId) throws Exception {
		repository.deleteById(itemId);
	}

	@Override
	public List<Item> list() throws Exception {
		return repository.findAll(Sort.by(Direction.DESC, "itemId"));
	}

	@Override
	public List<String> getAttach(Long itemId) throws Exception {
		Item itemEntity = repository.getOne(itemId);

		List<ItemFile> itemFiles = itemEntity.getItemFiles();

		List<String> attachList = new ArrayList<String>();
		for(ItemFile itemFile : itemFiles) {
			attachList.add(itemFile.getFullName());
		}

		return attachList;
	}

}
