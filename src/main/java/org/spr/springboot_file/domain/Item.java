package org.spr.springboot_file.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="item3")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;

	@Column(length = 50)
	private String itemName;

	private Integer price;

	@Column(length = 250)
	private String description;
	
	@OneToMany(cascade= CascadeType.ALL)
	@JoinColumn(name="item_id")
	private List<ItemFile> itemFiles = new ArrayList<ItemFile>();
	
	@Transient
	private String[] files;

	public void addItemFile(ItemFile itemFile) {
		itemFiles.add(itemFile);
	}

	public void clearItemFile() {
		itemFiles.clear();
	}
	
}
