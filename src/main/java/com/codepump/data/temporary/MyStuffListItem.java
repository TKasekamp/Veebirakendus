package com.codepump.data.temporary;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

/**
 * Class to transport values from both tables to the user. <br>
 * Used to populate MyStuff list. Searches for all code made by this user.<br>
 * 
 * @author TKasekamp
 * 
 */
@Entity
@NamedNativeQuery(name = "thisUserCodeByID", query = "select c.code_id, c.code_name, c.code_language, c.create_date FROM CodeItem as c JOIN webapp_user as w on w.user_id = c.user_id where c.user_id = :t_id ORDER BY c.create_date DESC LIMIT :limit OFFSET :offset", resultClass = MyStuffListItem.class)
public class MyStuffListItem implements Serializable {

	/**
	 * This has to be here
	 */
	private static final long serialVersionUID = 3304476411995025133L;
	private int codeId;
	private String codeName;
	private String codeLanguage;
	private Date createDate;

	private static final DateFormat FORMAT = new SimpleDateFormat(
			"HH:mm:ss dd.MM.yyyy");

	public MyStuffListItem() {
	}

	public MyStuffListItem(int codeId, String codeName, String codeLanguage,
			Date createDate) {
		super();
		this.codeId = codeId;
		this.codeName = codeName;
		this.codeLanguage = codeLanguage;
		this.createDate = createDate;
	}

	@Id
	@Column(name = "CODE_ID")
	public int getCodeId() {
		return codeId;
	}

	public void setCodeId(int codeId) {
		this.codeId = codeId;
	}

	@Column(name = "CODE_NAME")
	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Column(name = "CODE_LANGUAGE")
	public String getCodeLanguage() {
		return codeLanguage;
	}

	public void setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "MyStuffListItem [codeId=" + codeId + ", codeName=" + codeName
				+ ", codeLanguage=" + codeLanguage + ", createDate="
				+ createDate + "]";
	}

	/**
	 * Formats the creation date to "HH:mm:ss dd.MM.yyyy"
	 * 
	 * @return String format of date
	 */
	@Deprecated
	public String prettyCreateDate() {
		return FORMAT.format(createDate);
	}

	/**
	 * Sets create Date timeZone to given value. Formats the creation date to
	 * "HH:mm:ss dd.MM.yyyy"
	 * 
	 * @param timeZone
	 *            Timezone format as in "Europe/Helsinki"
	 * @return String format of date
	 */
	public String prettyCreateDate(String timeZone) {
		FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
		return FORMAT.format(createDate);
	}
}
