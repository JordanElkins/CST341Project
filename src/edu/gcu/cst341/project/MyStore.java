package edu.gcu.cst341.project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class MyStore {
	private Scanner sc = new Scanner(System.in);
	private String name; 
	private DBConnect con;

	MyStore (String name){
		this.name = name;
		con = new DBConnect();
	}
	
	//Added admin login
	//Jordan 12/7/20
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
				user = adminLogin();
				if (user != null) {
					System.out.println("Login successful!!");
					admin();
				}
				else {
					System.out.println("Login unsuccessful");
				}
				break;
			default:
				open();
			}
		} while (!exit);
	}

	//Column Names
	//Jordan 12/7/20
	private String login() {
		
		String result = null;

		String [] login = UserInterface.login();

		String sql = "SELECT user_id, user_first_name FROM users WHERE user_name = ? AND user_password = ? AND user_status = 1";

		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, login[0]);
			ps.setString(2, login[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("user_first_name");
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
	
	//Created Admin Login
	//Jordan 12/7/20
	private String adminLogin() {
		
		String result = null;

		String [] login = UserInterface.login();

		String sql = "SELECT user_id, user_first_name FROM users WHERE user_name = ? AND user_password = ? AND admin_status = 1";

		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, login[0]);
			ps.setString(2, login[1]);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("user_first_name");
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
	
	private void createCartItem() {
		System.out.println("Add (Create) item to cart...");
		System.out.println();
	}
	
	private void readCartItems() {
		System.out.println("View (Read) cart...");
		System.out.println();
	}
	
	private void deleteCartItem() {
		System.out.println("Delete from cart...");
		System.out.println();
	}
	
	//Added code
	//Jordan 12/7/20
	private void createProduct() {
		System.out.println("Create product...");
		System.out.println("Type the name of the new product and press enter.");
		String item = sc.nextLine();
		System.out.println("Type the product's price and press enter.");
		Double price = sc.nextDouble();
		sc.nextLine();
		System.out.println("Is this item in stock? Type 1 for yes and 0 for no.");
		int status = sc.nextInt();
		sc.nextLine();
		String sql = "INSERT INTO products (product_name, product_price, stock_status) VALUES (?, ?, ?)";
		try (PreparedStatement ps = con.getConnection().prepareStatement(sql)){
			ps.setString(1, item);
			ps.setDouble(2, price);
			ps.setInt(3, status);
			ps.execute();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		String prntstatus;
		if (status == 1) {
			prntstatus = "In Stock";
		} else {
			prntstatus = "Out of Stock";
		}
		System.out.println("New Item Added Successfully" + "\nProduct Name: " + item + "\nPrice: " + price + "\nStock Status: " +  prntstatus + "\n");
		admin();
	}
	
	private void readProducts() {
		System.out.println("View (Read) all products...");
		System.out.println();
	}
	
	private void updateProduct() {
		System.out.println("Update product...");
		System.out.println();
	}
	
	private void deleteProduct() {
		System.out.println("Delete product...");
		System.out.println();
	}

	//added method to print my name
	//jordan- 11/19/20	
	private void jordan() {
		System.out.println("Jordan");
	}
}

