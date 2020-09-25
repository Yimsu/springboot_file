package org.spr.springboot_file.controller;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.spr.springboot_file.domain.Item;
import org.spr.springboot_file.service.ItemService;
import org.spr.springboot_file.util.MediaUtils;
import org.spr.springboot_file.util.UploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/items")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Value("${upload.path}")
	private String uploadPath;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> list() throws Exception {
		log.info("list");
		List<Item> itemList = this.itemService.list();

		return new ResponseEntity<>(itemList, HttpStatus.OK);
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<Item> read(@PathVariable("itemId") Long itemId) throws Exception {
		log.info("read");

		Item item = this.itemService.read(itemId);

		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@RequestMapping(value = "/{itemId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable("itemId") Long itemId) throws Exception {
		log.info("remove");

		this.itemService.remove(itemId);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<Item> register(@Validated @RequestBody Item item) throws Exception {
		String[] files = item.getFiles();

		for (int i = 0; i < files.length; i++) {
			log.info("files[i] = " + files[i]);
		}

		this.itemService.regist(item);

		log.info("register item.getItemId() = " + item.getItemId());

		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	public ResponseEntity<Item> modify(@Validated @RequestBody Item item) throws Exception {
		String[] files = item.getFiles();

		for (int i = 0; i < files.length; i++) {
			log.info("files[i] = " + files[i]);
		}

		this.itemService.modify(item);

		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public ResponseEntity<String> upload(MultipartFile file) throws Exception {
		log.info("originalName: " + file.getOriginalFilename());

		String savedName = UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes());

		return new ResponseEntity<String>(savedName, HttpStatus.CREATED);
	}

	@RequestMapping("/display")
	public ResponseEntity<byte[]> display(String fileName) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		log.info("FILE NAME: " + fileName);

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

			MediaType mType = MediaUtils.getMediaType(formatName);

			HttpHeaders headers = new HttpHeaders();

			in = new FileInputStream(uploadPath + fileName);

			if (mType != null) {
				headers.setContentType(mType);
			} else {

				fileName = fileName.substring(fileName.indexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}
		return entity;
	}

	@RequestMapping("/attach/{itemId}")
	public List<String> attach(@PathVariable("itemId") Long itemId) throws Exception {
		log.info("attach itemId: " + itemId);

		return itemService.getAttach(itemId);
	}

}
