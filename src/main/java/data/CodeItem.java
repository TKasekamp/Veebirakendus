package data;

public class CodeItem {
	private String name;
	private String text;
	private Integer id;
	public Integer itemId;

	public CodeItem(Integer id, String name, String text) {
		super();
		this.name = name;
		this.text = text;
		this.id = id;
	}

	public CodeItem(String name, String text) {
		super();
		this.name = name;
		this.text = text;
	}

	public CodeItem() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CodeItem [name=" + name + ", text=" + text + ", id=" + id
				+ ", itemId=" + itemId + "]";
	}

}
