package com.vmladenov.cook.core.db;

import java.util.ArrayList;

import com.vmladenov.cook.core.converters.ShoppingListConverter;
import com.vmladenov.cook.core.converters.ShoppingListItemConverter;
import com.vmladenov.cook.core.objects.ShoppingList;
import com.vmladenov.cook.core.objects.ShoppingListItem;

public class ShoppingListsRepository {

	private static final String SELECT_SHOPPING_LISTS = "SELECT ID, TITLE, CREATION_DATE FROM SHOPPING_LISTS ORDER BY CREATION_DATE DESC";
	private static final String SELECT_SHOPPING_LIST = "SELECT ID, TITLE, CREATION_DATE FROM SHOPPING_LISTS WHERE ID = %d ";
	private static final String SELECT_SHOPPING_LIST_ITEMS = "SELECT ID, LIST_ID, TITLE, ORDER_NUMBER, IS_CHECKED"
			+ " FROM SHOPPING_LIST_ITEMS WHERE LIST_ID = %d ORDER BY ORDER_NUMBER";

	private static final String UPDATE_LIST_ITEM_CHECKED = "UPDATE SHOPPING_LIST_ITEMS "
			+ "SET IS_CHECKED = %d WHERE ID = %d";

	private static final String DELETE_LIST = "DELETE FROM SHOPPING_LISTS WHERE ID = %d";
	private static final String DELETE_LIST_ITEMS = "DELETE FROM SHOPPING_LIST_ITEMS WHERE LIST_ID = %d";
	private static final String DELETE_LIST_ITEM = "DELETE FROM SHOPPING_LIST_ITEMS WHERE ID = %d";

	private static final String MAX_SHOPPING_LIST_ITEM_ORDER = "SELECT MAX(ORDER_NUMBER) FROM SHOPPING_LIST_ITEMS WHERE LIST_ID = ?";

	private static final String INSERT_SHOPPING_LIST = "INSERT INTO SHOPPING_LISTS "
			+ "(ID, TITLE, CREATION_DATE) "
			+ "VALUES ((SELECT MAX(ID) FROM SHOPPING_LISTS) + 1, ?, datetime('now'))";
	private static final String INSERT_SHOPPING_ITEM = "INSERT INTO SHOPPING_LIST_ITEMS "
			+ "(ID, LIST_ID, TITLE, ORDER_NUMBER, IS_CHECKED) "
			+ "VALUES ((SELECT MAX(ID) FROM SHOPPING_LIST_ITEMS) + 1, ?, ?, ?, 0)";

	private String dbPath;

	public ShoppingListsRepository(String dbPath) {
		this.dbPath = dbPath;
	}

	public void saveShoppingList(ShoppingList list) {
		SQLHelper.ExecuteNonQuery(INSERT_SHOPPING_LIST, dbPath, list.Title);
		int id = SQLHelper.ExecuteScalarIntSql(
				"SELECT MAX(ID) FROM SHOPPING_LISTS", dbPath);
		for (int i = 0; i < list.Items.size(); i++) {
			ShoppingListItem item = list.Items.get(i);
			SQLHelper.ExecuteNonQuery(INSERT_SHOPPING_ITEM, dbPath, id,
					item.Title, i);
		}
	}

	public ArrayList<ShoppingList> getShoppingLists() {
		return SQLHelper.ExecuteSql(SELECT_SHOPPING_LISTS, dbPath,
				new ShoppingListConverter());
	}

	public ShoppingList getShoppingList(long id) {
		String sql = String.format(SELECT_SHOPPING_LIST, id);
		String sqlItem = String.format(SELECT_SHOPPING_LIST_ITEMS, id);
		ShoppingList list = SQLHelper.ExecuteSingleSql(sql, dbPath,
				new ShoppingListConverter());
		list.Items = SQLHelper.ExecuteSql(sqlItem, dbPath,
				new ShoppingListItemConverter());
		return list;
	}

	public void setCheckedListItem(long id, Boolean isChecked) {
		int checked = isChecked ? 1 : 0;
		String sql = String.format(UPDATE_LIST_ITEM_CHECKED, checked, id);
		SQLHelper.ExecuteNonQuery(sql, dbPath);
	}

	public void deleteList(long id) {
		String sql = String.format(DELETE_LIST, id);
		sql = String.format(DELETE_LIST_ITEMS, id);
		SQLHelper.ExecuteNonQuery(sql, dbPath);
		SQLHelper.ExecuteNonQuery(sql, dbPath);
	}

	public void deleteListItem(long id) {
		String sql = String.format(DELETE_LIST_ITEM, id);
		SQLHelper.ExecuteNonQuery(sql, dbPath);
	}

	public void insertListItem(long id, String title) {
		Integer orderNumber = SQLHelper.ExecuteScalarIntSql(
				MAX_SHOPPING_LIST_ITEM_ORDER, dbPath, Long.toString(id));
		if (orderNumber == null)
			orderNumber = 0;
		orderNumber++;
		SQLHelper.ExecuteNonQuery(INSERT_SHOPPING_ITEM, dbPath, id, title,
				orderNumber);
	}

}
