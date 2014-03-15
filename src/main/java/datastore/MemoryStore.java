package datastore;

import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import data.CodeItem;
import util.HibernateUtil;

public class MemoryStore implements CodeDataProvider {

	private Session session = HibernateUtil.currentSession();

	public MemoryStore() {

	}

	@Override
	public void addCode(CodeItem item) {

		item.setSaveDate(new Date());
		item.setExpireDate(new Date());

		session.getTransaction().begin();
		session.save(item);
		session.getTransaction().commit();

	}

	@SuppressWarnings("unchecked")
	@Override
	public CodeItem findItemById(int id) {
		// TODO Guard from SQL injection
		List<CodeItem> dataset = session.createQuery(
				"from CodeItem where ID='" + Integer.toString(id) + "'").list();
		return dataset.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodeItem> findAllItems() {
		List<CodeItem> dataset = session.createQuery(
				"from CodeItem where PRIVACY='Public'").list();
		return dataset;
	}

	@Override
	public void editCode(CodeItem item) {
		// items.get(item.getId()).setText(item.getText());

	}
}
