# To-Do-List
This is a simple to do list connected to a h2 database, the list will be saved in database once the list has been altered.
The list will be sync when refresh the website. If more than five items, item displays in next page.

This ToDoList made use of Thymeleaf, H2 Database, JDBC, Spring Web dependencies. 

To try this, run DemoApplication.Java
and then type localhost on URL in browser (prefer Google Chrome).

The design followed:
Basic Requirements:
1. Users should be able to create, edit and delete to-do items (text content only).
2. Users should be able to browse a list of to-do items.
3. Users should be able to toggle the state of items between pending and completed.
4. Users should be able to hide or show completed items.
5. To-do items should be saved to the database automatically, assuming connectivity is
always available.
6. The app should be visually appealing using plain CSS or the Bootstrap framework.
7. The app should be written using Vue and Spring frameworks only. No other
programming languages or libraries are allowed.
8. The app should run in a web browser and be accessible to all users, regardless of the
device or screen size.
9. The app should provide schema.sql and data.sql as the default schema and sample
data to initialize the database for testing.
10. The programs should have proper comments to explain the code.

Advanced Requirements:
1. Limit each page to display 5 items and provide pagination for more items.
2. Support offline mode. Save to-do items in the client’s local storage and synchronise
with the server’s database when there is connectivity. The client needs to check the
server’s status constantly and indicate the current working mode in the UI.

![image](https://user-images.githubusercontent.com/111900986/233782294-c65254c1-6911-4bd1-947e-1fb213b325a0.png)
