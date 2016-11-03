package com.example.model;

import java.io.File;

/**
 * 文件实体类
 * **/
public class Fileinfo {
	private File f;
	private String filename;
	private String fileType;
	private String fileiocn;
	private boolean isselect=false;
	public boolean isIsselect() {
		return isselect;
	}
	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}
	public File getF() {
		return f;
	}
	public void setF(File f) {
		this.f = f;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileiocn() {
		return fileiocn;
	}
	public void setFileiocn(String fileiocn) {
		this.fileiocn = fileiocn;
	}
	
}
