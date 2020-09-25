package org.spr.springboot_file.repository;

import org.spr.springboot_file.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>{

}
