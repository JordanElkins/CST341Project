package edu.gcu.cst341.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MyStore {
	static Scanner sc = new Scanner(System.in);
	private String name; 
	private DBConnect con;
	private int userID;
	private String customer;

	MyStore (String name){
		this.name = name;
		con = new DBConnect();
	}

	public void open() {
		String user = null;
		boolean exit = false;
		do {
			switch (UserInterface.menuMain()) {
			case 0:
				System.out.println("Thank you! Come again!");
				exit = true;
				break;
			case 1:
				user = login();
				if (user != null) {
					System.out.println("Login successful!!");
					shop();
				}
				else {
					System.out.println("Login unsuccessful");
				}
				break;
			case 2:
				admin();
				break;
			default:
				open();
			}
		} while (!exit);
	}

	private String login() {
		String result = null;

		String [] login = UserInterface.login();
//		Altered the select statement to match the database. 
//		Anastasia Sullivan 11/23/2020
		String sql = "SELECT user_id, user_first_name FROM users WHERE user_name = ? AND user_password = ? AND user_status = 1";

		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, login[0]);
			ps.setString(2, login[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("user_first_name");
				userID = rs.getInt("user_id");
				customer = rs.getNString("user_first_name");
				System.out.println("Thank you, " + customer + " you have successfully logged in.");
			}
			else {
				result = null;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void shop() {
		switch (UserInterface.menuShop()) {
		case 0:
			return;
		case 1:
			createCartItem();
			break;
		case 2:
			readCartItems();
			break;
		case 3:
			deleteCartItem();
			break;
		default:
			return;
		}
	}

	private void admin() {
		switch (UserInterface.menuAdmin()) {
		case 0:
			return;
		case 1:
			createProduct();
			break;
		case 2:
			readProducts();
			break;
		case 3:
			updateProduct();
			break;
		case 4:
			deleteProduct();
			break;	
		default:
			open();
		}
	}
//	Method Outline Created 
//	Anastasia Sullivan 12/05/2020
	private void createCartItem() {
		System.out.println("Add (Create) item to cart...");
		readProducts();
		System.out.println("What is the product ID of the item you wish to add?");
		String item = sc.nextLine();
		System.out.println(item);
		System.out.println(userID);
		String sqlInsert = "INSERT INTO giveusyourmoney.shopping_cart (user_id, product_id) VALUES (?, ?)";
		try (PreparedStatement ps = con.getConnection().prepareStatement(sqlInsert)){
			ps.setInt(1, userID);
			ps.setString(2, item);
			ps.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
//	Method Outline Created 
//	Anastasia Sullivan 12/05/2020	
	private void readCartItems() {
		System.out.println("View (Read) cart...");
		System.out.println(customer + ", Here are the contents of your shopping cart.");
		String sql = "SELECT shopping_cart.product_id, products.product_name, products.product_price\n" + 
				"FROM giveusyourmoney.shopping_cart\n" + 
				"JOIN  products on shopping_cart.product_id = products.product_id\n" + 
				"WHERE shopping_cart.user_id = ?";
		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setInt(1, userID);
			ResultSet rs = ps.executeQuery();
			System.out.println("Product ID   Product Name   Product Price");
			System.out.println("------------------------------");
			Double total = 0.00;
			while(rs.next()) {
				System.out.println(rs.getInt("product_id") + " " + rs.getString("product_name")+ " " + rs.getString("product_price"));
				total = total + rs.getDouble("product_price");
			}
			System.out.println("The current total for all your items is: $" + total);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteCartItem() {
		System.out.println("Delete from cart...");
		System.out.println();
	}
	
	private void createProduct() {
		System.out.println("Create product...");
		System.out.println();
	}
//	Method Outline Created 
//	Anastasia Sullivan 12/05/2020
	private void readProducts() {
		System.out.println("View (Read) all products...");
		System.out.println();
		String sql = "SELECT product_id, product_name, product_price FROM giveusyourmoney.products WHERE stock_status = 1";
		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ResultSet rs = ps.executeQuery();
			System.out.println("Product ID   Product Name   Product Price");
			System.out.println("------------------------------");
			while(rs.next()) {
				System.out.println(rs.getInt("product_id") + " " + rs.getString("product_name")+ " " + rs.getString("product_price"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
//	Method Outline Created 
//	Anastasia Sullivan 12/05/2020	
	private void updateProduct() {
		System.out.println("Update product...");
		System.out.println("Which of the following products would you like to update?");
		readProducts();
		System.out.println("Type the Product ID number and press enter.");
		int id = sc.nextInt();
		sc.nextLine();
		System.out.println("Type a new name for this item, and press enter.");
		String item = sc.nextLine();
		System.out.println("Type a new price for this item, and press enter.");
		Double price = sc.nextDouble();
		String sql = "UPDATE products SET product_name = ?, product_price = ? Where product_id = ?"; 
		
		try(PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, item);
			ps.setDouble(2, price);
			ps.setInt(3, id);
		    ps.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
//	Method Outline Created 
//	Anastasia Sullivan 12/05/2020	
	
	private void deleteProduct() {
		System.out.println("Delete product...");
		System.out.println("Here are the current products.");
		readProducts();
		System.out.println("Please enter the product ID number for the item you wish to delete.");
		int id = sc.nextInt();
		sc.nextLine();
		String sql = "UPDATE products SET stock_status = 0 WHERE product_id = ?";
		try(PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setInt(1, id);
		    ps.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
//	added a method to print my name
//	Anastasia Sullivan 11/19/2020
	private void anastasia() {
		System.out.println("Anastasia");
	}

	// added method to print out my name
	// andrew - 11/19/2020
	private void andrew() {
	System.out.println("andrew");
}
	//added method to print my name
	//jordan- 11/19/20	
	private void jordan() {
		System.out.println("Jordan");

	}
}

