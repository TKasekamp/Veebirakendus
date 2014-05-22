package com.codepump.data.container;

/**
 * Created when an user wants to edit code. Holds the SID to authorise the edit.<br>
 * @author TKasekamp
 *
 */
public class EditContainer {
	private int id;
	private String text;
	private String SID;
	public EditContainer() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	@Override
	public String toString() {
		return "EditContainer [id=" + id + ", text=" + text + ", SID=" + SID
				+ "]";
	}

}
