# TUY-LIBRARY
# Author
Group 2
1. Nong Son Tung - 23020571
2. Chu Anh Truong - 23020577
3. Pham Quang Vinh - 23020580
# Description
The application is designed for users and admins, allowing them to both manage the library system and look up books in the app. The application is written in Java and uses the JavaFX library. The application is based on the MVC modelThe application includes two interfaces: one for admins to manage user accounts and books, and the other for users to search for books. The application uses userInfo.db and question.txt to store data.
# UML DIAGRAM
![library](https://github.com/user-attachments/assets/b048e626-40c1-4587-a9fe-ba8949ffba44)
# Installation
1. Clone the project from the repository.
2. Open the project in the IDE.
3. Run the project.
4. To modify data, you can change the userInfo.db and question.txt files.
# Usage
- For admin:
  1. Search for Users using the search bar (name, username, email, …), related users will appear in the table below.
  2. To add a User, fill in details on the right side of the screen, click Save to add or Cancel to discard changes.
  3. To update User information, select a user from the table, update the details on the right side of the screen, click Update to save the changes.
  4. To delete a User, select the user you want to delete from the table, click Delete (Trash icon) or right-click the user and choose Delete.
  5. To search books, use the search bar to look for books in the API and the database, related books will appear in the table below.
  6. To add or increase book quantity in the database, select the book you want, click Save.
  7. If the book you need is not displayed in the related books list, click Add Book in the top-right corner, enter the necessary details and click Save.
  8. To delete a book, select the book you want to delete from the database, click Delete (Trash icon) or right-click the book and choose Delete.
- For client:
  1. Search for books: Use the search bar at the top to search for books by title or author. The app will display a list of matching books.
  2. View book details: Tap or click on a book to see more information, including availability.
  3. Borrow a book: Check the "Available Copies" section. If there are copies, tap the "Mượn sách" button. The app will update the number of copies available and save it to My collection’s Borrowing.
  4. Return a book: Check the books you have borrowed in My Collection's Borrowing, if you want to return them, tap Book Info then tap the "Trả sách" button. The app will update the number of copies available and remove the book from your Borrowing.
  5. Check history: Go to My Collection to check your recent book borrowing and returning activities.
  6. Edit personal information: Go to SETTING then click "Edit infomation" to edit information, besides you can click "Change Avatar" to change avatar and the option to turn on or off background music by clicking "Background music".
  7. Play game: Go to GAME to experience our game, the instructions are clearly presented there, you just need to click "Start Game" button to start the game.
  8. Logout: you just need to click "LOGOUT" it will take you back to login.
# Demo
# Future improvements
1. Optimize book search algorithm.
2. Expand database.
3. Integrated voice search.
4. Read all books in the application.
5. Improve security and provide multiple login options (eg: Google, Facebook, ...)
6. Aim to personalize user interface.
7. Integrate rating and comment features.
8. Integrate AI to optimize user experience.
9. Build a book classification warehouse by topic, content and purpose.
10. Refine the Game section to support increasing knowledge in the best way suitable for each user.
# Contribute
Pull requests are always welcome. For major changes, please open an issue first to discuss what you want to change before making changes. Any contributions to improve the app are always welcome
# Project status
This project is completed.
# Notes
This project is written for educational purposes.
