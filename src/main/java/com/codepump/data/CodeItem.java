package com.codepump.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

/**
 * Represents one item of code in the DB.<br>
 * <code>UserID</code> will be set to -1 if no user is specified. Object User of
 * this class will hold the user_ID.
 * 
 * @author TKasekamp
 * 
 */
@Entity
@Table(name = "CODEITEM")
@Indexed
@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
		@TokenFilterDef(factory = WordDelimiterFilterFactory.class),
		@TokenFilterDef(factory = LowerCaseFilterFactory.class),
		@TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = { @Parameter(name = "language", value = "English") }) })
public class CodeItem implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String text;
	private String language;
	private String privacy;
	private Date createDate;
	private Date expireDate;
	private User user;

	private static final DateFormat FORMAT = new SimpleDateFormat(
			"HH:mm dd.MM.yyyy");

	public CodeItem() {
	}

	public CodeItem(Integer id, String name, String text, String language,
			String privacy) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
		this.createDate = new Date();
		this.expireDate = new Date();
	}

	public CodeItem(Integer id, String name, String text, String language,
			String privacy, Date createdDate, Date expireDate, User user) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
		this.createDate = createdDate;
		this.expireDate = expireDate;
		this.user = user;
	}

	public CodeItem(String name, String text, String language, String privacy) {
		super();
		this.name = name;
		this.text = text;
		this.language = language;
		this.privacy = privacy;
	}

	/**
	 * Generates a string with id. It can be done with a costum
	 * com.codepump.serializer, but this is easier.
	 * 
	 * @return id in Json format
	 */
	public String JsonID() {
		return "{\"id\":\"" + Integer.toString(id) + "\"}";
	}

	@Id
	@Column(name = "CODE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	/** Generation has something to do with Postgre serial */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Analyzer(definition = "customanalyzer")
	@Boost(value = 1.5f)
	@Fields({ @Field, @Field(name = "sorting_name", analyze = Analyze.NO) })
	@Column(name = "CODE_NAME", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Analyzer(definition = "customanalyzer")
	@Field
	@Column(name = "CODE_TEXT", nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "CODE_LANGUAGE", nullable = false, length = 20)
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Column(name = "PRIVACY", nullable = false, length = 10)
	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	@DateBridge(resolution = Resolution.DAY)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false, length = 7)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXPIRE_DATE", nullable = false, length = 7)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CodeItem [id=" + id + ", name=" + name + ", text=" + text
				+ ", language=" + language + ", privacy=" + privacy
				+ ", createDate=" + createDate + ", expireDate=" + expireDate
				+ ", user=" + user + "]";
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
		if (createDate != null) {
			return FORMAT.format(createDate);
		} else
			return "";
	}

}
