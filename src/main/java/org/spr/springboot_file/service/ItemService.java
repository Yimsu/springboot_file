package org.spr.springboot_file.service;

import org.spr.springboot_file.domain.Item;

import java.util.List;

public interface ItemService {
	public void regist(Item item) throws Exception;

	public Item read(Long itemId) throws Exception;

	public void modify(Item item) throws Exception;

	public void remove(Long itemId) throws Exception;

	public List<Item> list() throws Exception;

	public List<String> getAttach(Long itemId) throws Exception;

}
