package com.codepump.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.service.CodeService;
import com.codepump.util.HibernateUtil;

public class CodeServiceImpl implements CodeService {

	private Session session;
	private Map<Integer, CodeItem> items;
	private int codeCounter; // useless

	private final boolean USE_DATABASE = ServerController.USE_DATABASE;

	public CodeServiceImpl() {

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
		item.setSaveDate(new Date());
		item.setExpireDate(new Date());
		if (USE_DATABASE) {
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
					"from CodeItem where CODE_ID='" + Integer.toString(id)
							+ "'").list();
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
			ArrayList<CodeItem> dataset = new ArrayList<CodeItem>();
			for (CodeItem value : items.values()) {
				if (value.getPrivacy().equals("Public")) {
					dataset.add(value);
				}
			}
			return dataset;
		}

	}

	@Override
	public void editCode(CodeItem item) {
		if (USE_DATABASE) {
			// TODO this
		} else {
			items.get(item.getId()).setText(item.getText());
		}

	}
}
