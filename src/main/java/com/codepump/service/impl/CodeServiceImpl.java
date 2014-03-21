package com.codepump.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.codepump.controller.ServerController;
import com.codepump.data.CodeItem;
import com.codepump.data.User;
import com.codepump.service.CodeService;
import com.codepump.tempobject.MyStuffListItem;
import com.codepump.tempobject.RecentItem;
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
					"public static void Hello(String s);", "java", "Public",
					new Date(), new Date(), new User(100, "Test", "dumbass",
							"parool")));
			items.put(2, new CodeItem(2, "bla", "print(\"bla\")", "python",
					"Public", new Date(), new Date(), new User(100, "Test",
							"dumbass", "parool")));
			items.put(3, new CodeItem(3, "haha", "Hello::Hello", "c",
					"Private", new Date(), new Date(), new User(100, "Test",
							"dumbass", "parool")));
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
			List<CodeItem> dataset = session
					.createQuery("from CodeItem where CODE_ID=:id")
					.setParameter("id", id).list();
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
			// This way no SQL injection in this function
			CodeItem code = findItemById(item.getId());
			code.setText(item.getText());
			session.getTransaction().begin();
			session.update(code);
			session.getTransaction().commit();
		} else {
			items.get(item.getId()).setText(item.getText());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RecentItem> getRecentItems() {
		if (USE_DATABASE) {
			List<RecentItem> results = session.getNamedQuery(
					"findRecentItemsInOrder").list();
			// System.out.println(results.toString());
			return results;

		} else {
			// TODO for someone to fix. RecentItem should get values from users
			ArrayList<RecentItem> dataset = new ArrayList<RecentItem>();
			for (CodeItem value : items.values()) {
				if (value.getPrivacy().equals("Public")) {
					RecentItem r = new RecentItem(value.getId(),
							value.getName(), value.getLanguage(),
							value.getSaveDate(), 100,
							"TEST USER NOT FOR REAL AS I CAN'T BE BOTHERED");
					dataset.add(r);
				}
			}
			return dataset;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MyStuffListItem> getAllUserItems(String SID) {
		int userID = ServerController.authenticationServer.getUserWithSID(SID);

		// userID will be set to -1 if no such SID can be found. This is the
		// public user and as such MyStuff should not work
		if (userID == -1) {
			return null;
		}
		if (USE_DATABASE) {
			// Creating a query and setting a parameter after.
			Query q = session.getNamedQuery("thisUserCodeByID");
			q.setParameter("t_id", userID);
			List<MyStuffListItem> dataset = q.list();

//			System.out.println(dataset.toString());
			return dataset;
		} else {
			ArrayList<MyStuffListItem> dataset = new ArrayList<MyStuffListItem>();
			for (CodeItem value : items.values()) {
				if (value.getUser().getId() == userID) {
					dataset.add(new MyStuffListItem(value.getId(), value
							.getName(), value.getLanguage(), value
							.getSaveDate()));
				}
			}
			return dataset;
		}

	}
}
