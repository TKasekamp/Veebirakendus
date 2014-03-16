package datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import data.CodeItem;
import util.HibernateUtil;

public class MemoryStore implements CodeDataProvider {

	private Session session;
	private Map<Integer, CodeItem> items;
	private int codeCounter; // useless

	public static boolean USE_DATABASE = false; // CHANGE THIS WHEN DEPLOYING

	public MemoryStore() {

		if (USE_DATABASE) {
			session = HibernateUtil.currentSession();

		} else {
			items = new HashMap<>();
			items.put(1, new CodeItem(1, "hello",
					"public static void Hello(String s);", "java", "Public"));
			items.put(2, new CodeItem(2, "bla", "print(\"bla\")", "python",
					"Public"));
			items.put(3,
					new CodeItem(3, "haha", "Hello::Hello", "c", "Private"));
			codeCounter = 4;
		}
	}

	@Override
	public void addCode(CodeItem item) {

		if (USE_DATABASE) {
			item.setSaveDate(new Date());
			item.setExpireDate(new Date());

			session.getTransaction().begin();
			session.save(item);
			session.getTransaction().commit();
		} else {
			item.setId(codeCounter); // temp hack
			items.put(codeCounter, item);
			codeCounter++;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public CodeItem findItemById(int id) {

		if (USE_DATABASE) {
			// TODO Guard from SQL injection
			List<CodeItem> dataset = session.createQuery(
					"from CodeItem where ID='" + Integer.toString(id) + "'")
					.list();
			return dataset.get(0);
		} else {
			return items.get(id);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodeItem> findAllItems() {
		if (USE_DATABASE) {
			List<CodeItem> dataset = session.createQuery(
					"from CodeItem where PRIVACY='Public'").list();
			return dataset;
		} else {
			return new ArrayList<>(items.values());
		}

	}

	@Override
	public void editCode(CodeItem item) {
		if (USE_DATABASE) {
		} else {
			items.get(item.getId()).setText(item.getText());
		}

	}
}
